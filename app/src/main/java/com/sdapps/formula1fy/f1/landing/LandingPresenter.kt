package com.sdapps.formula1fy.f1.landing

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sdapps.formula1fy.R
import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.core.models.DataMembers
import com.sdapps.formula1fy.core.models.DataMembers.tbl_constructorMaster
import com.sdapps.formula1fy.core.models.DataMembers.tbl_currentAllResults
import com.sdapps.formula1fy.core.models.DataMembers.tbl_currentAllResultsCols
import com.sdapps.formula1fy.core.models.DataMembers.tbl_raceScheduleMaster
import com.sdapps.formula1fy.core.models.DataMembers.tbl_driverMaster
import com.sdapps.formula1fy.core.utils.Commons
import com.sdapps.formula1fy.core.utils.CoroutineTools
import com.sdapps.formula1fy.core.utils.StringHelper
import com.sdapps.formula1fy.f1.bo.ConstructorBO
import com.sdapps.formula1fy.f1.bo.CurrentSeasonBO
import com.sdapps.formula1fy.f1.bo.DriverBO
import com.sdapps.formula1fy.f1.bo.RaceScheduleBO
import com.sdapps.formula1fy.f1.bo.Results
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class LandingPresenter(val appContext: Context) : LandingContractor.Presenter {

    private lateinit var context: Context
    private var view: LandingContractor.View? = null

    private lateinit var requestQueue: RequestQueue
    private lateinit var db: DbHandler
    private var stringHandler: StringHelper = StringHelper()
    private var seasonString: String? = null
    private var roundString: String? = null
    private lateinit var latestRoundResult: MutableList<Results>
    private lateinit var currentAllRaceResult : ArrayList<CurrentSeasonBO>
    private var flag : Boolean = false
    override fun attachView(view: LandingContractor.View, context: Context) {
        this.view = view
        this.context = context
    }

    override fun detachView() {
        this.view = null
    }


    override fun getAppString(): SpannableStringBuilder {

        val builder: SpannableStringBuilder = SpannableStringBuilder()
        val appFirst: SpannableString = SpannableString("Formula1")
        appFirst.setSpan(ForegroundColorSpan(Color.WHITE), 0, appFirst.length, 0)
        builder.append(appFirst)

        val appSecond: SpannableString = SpannableString("Fy")
        appSecond.setSpan(ForegroundColorSpan(Color.RED), 0, appSecond.length, 0)
        builder.append(appSecond)

        return builder

    }

    override suspend fun fetchDriverData() {
        db = DbHandler(context.applicationContext, DataMembers.DB_NAME)
        requestQueue = Volley.newRequestQueue(context)

        val url = "http://ergast.com/api/f1/current/driverStandings.json"
        val driverList = ArrayList<DriverBO>()

        val jsonReq = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response: JSONObject ->
                try {
                    val driverStandings = response.getJSONObject("MRData")
                        .getJSONObject("StandingsTable")
                        .getJSONArray("StandingsLists")
                        .getJSONObject(0)
                        .getJSONArray("DriverStandings")

                    val result = response.getJSONObject("MRData")
                        .getJSONObject("StandingsTable")
                        .getJSONArray("StandingsLists")

                    val getBo = result.getJSONObject(0)
                    seasonString = getBo.getString("season")
                    roundString = getBo.getString("round")


                    for (i in 0 until driverStandings.length()) {
                        val driverData = driverStandings.getJSONObject(i)
                        val details = driverData.getJSONObject("Driver")
                        val dBO = DriverBO().apply {
                            season = seasonString
                            round = roundString
                            driverPosition = driverData.getString("position")
                            totalPoints = driverData.getString("points").toInt()
                            wins = driverData.getString("wins").toInt()
                            driverId = details.getString("driverId")
                            driverName =
                                details.getString("givenName") + " " + details.getString("familyName")
                            driverDOB = details.getString("dateOfBirth")
                            driverNationality = details.getString("nationality")
                            driverNumber = details.getString("permanentNumber").toInt()
                            driverCode = details.optString("code", "")
                            constructorId =
                                driverData.getJSONArray("Constructors").getJSONObject(0)
                                    .getString("constructorId")
                            constructorName =
                                driverData.getJSONArray("Constructors").getJSONObject(0)
                                    .getString("name")
                        }
                        driverList.add(dBO)
                    }

                    insertDriverData(driverList)


                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            },
            { error: VolleyError -> error.printStackTrace() })

        requestQueue.add(jsonReq)


    }


    fun getDriverDetails(driverBO: DriverBO): StringBuffer {
        val sb = StringBuffer()
        sb.append(stringHandler.getQueryFormat(driverBO.season))
        sb.append("," + stringHandler.getQueryFormat(driverBO.round))
        sb.append("," + stringHandler.getQueryFormat(driverBO.driverId))
        sb.append("," + stringHandler.getQueryFormat(driverBO.driverCode))
        sb.append("," + stringHandler.getQueryFormat(driverBO.driverName))
        sb.append("," + driverBO.driverNumber)
        sb.append("," + stringHandler.getQueryFormat(driverBO.constructorId))
        sb.append("," + driverBO.wins)
        sb.append("," + driverBO.totalPoints)
        sb.append("," + driverBO.driverPosition)
        sb.append("," + stringHandler.getQueryFormat(driverBO.constructorName))
        sb.append("," + stringHandler.getQueryFormat(driverBO.driverDOB))
        sb.append("," + stringHandler.getQueryFormat(driverBO.driverNationality))

        return sb
    }

    override fun insertDriverData(list: ArrayList<DriverBO>) {
        db = DbHandler(context.applicationContext, DataMembers.DB_NAME)
        db.createDB()
        db.openDB()
        if(isCheckDataAvailable(tbl_driverMaster, db)){
            db.deleteSQL(tbl_driverMaster,true)
        }
        for (i in 0 until list.size) {
            val bo = list.get(i)
            val values = getDriverDetails(bo)
            db.insertSQL(
                DataMembers.tbl_driverMaster,
                DataMembers.tbl_driverMasterCols,
                values.toString()
            )
        }
        db.closeDB()
    }


    fun getConstructorDetails(constructorBO: ConstructorBO): StringBuffer {
        val sb = StringBuffer()
        sb.append(stringHandler.getQueryFormat(constructorBO.season))
        sb.append("," + stringHandler.getQueryFormat(constructorBO.round))
        sb.append("," + stringHandler.getQueryFormat(constructorBO.consId))
        sb.append("," + stringHandler.getQueryFormat(constructorBO.name))
        sb.append("," + stringHandler.getQueryFormat(constructorBO.wins))
        sb.append("," + stringHandler.getQueryFormat(constructorBO.points))
        sb.append("," + stringHandler.getQueryFormat(constructorBO.position))
        sb.append("," + stringHandler.getQueryFormat(constructorBO.nationality))


        return sb

    }

    override fun insertConstructorData(list: ArrayList<ConstructorBO>) {
        db = DbHandler(context.applicationContext, DataMembers.DB_NAME)
        db.createDB()
        db.openDB()

        if(isCheckDataAvailable(tbl_constructorMaster, db)){
            db.deleteSQL(tbl_constructorMaster,true)
        }
        for (i in 0 until list.size) {
            val co = list.get(i)
            val constructorValues = getConstructorDetails(co)
            db.insertSQL(
                DataMembers.tbl_constructorMaster,
                DataMembers.tbl_constructorMasterCols,
                constructorValues.toString()
            )

        }
    }

    override suspend fun fetchRaceData() {
        db = DbHandler(context, DataMembers.DB_NAME)
        val url = "http://ergast.com/api/f1/current.json"
        requestQueue = Volley.newRequestQueue(context)

        val raceList = ArrayList<RaceScheduleBO>()
        val jsonReq = JsonObjectRequest(Request.Method.GET, url, null,
            { response: JSONObject ->

                if (response != null) {
                    try {
                        val raceJsonData = response.getJSONObject("MRData")
                            .getJSONObject("RaceTable")
                            .getJSONArray("Races")

                        for (i in 0 until raceJsonData.length()) {
                            val data = raceJsonData.getJSONObject(i)
                            val circuitData = data.getJSONObject("Circuit")
                            val locationData = circuitData.getJSONObject("Location")

                            val raceBO = RaceScheduleBO().apply {
                                season = data.getString("season")
                                round = data.getString("round")
                                raceName = data.getString("raceName")
                                date = data.getString("date")
                                time = data.getString("time")

                                circuitId = circuitData.getString("circuitId")
                                circuitName = circuitData.getString("circuitName")

                                lat = locationData.getString("lat")
                                long = locationData.getString("long")
                                locality = locationData.getString("locality")
                                country = locationData.getString("country")
                            }
                            raceList.add(raceBO)
                        }
                        insertRaceData(raceList)

                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }

                }

            },
            { error: VolleyError -> error.printStackTrace() })
        requestQueue.add(jsonReq)
    }


    override suspend fun fetchConstructorData() {
        db = DbHandler(context, DataMembers.DB_NAME)
        val url = "http://ergast.com/api/f1/current/constructorStandings.json"
        requestQueue = Volley.newRequestQueue(context)

        val constructorList = ArrayList<ConstructorBO>()
        val jsonReq = JsonObjectRequest(Request.Method.GET, url, null,
            { response: JSONObject ->
                if (response != null) {
                    try {
                        val driverStandings = response.getJSONObject("MRData")
                            .getJSONObject("StandingsTable")
                            .getJSONArray("StandingsLists")
                            .getJSONObject(0)
                            .getJSONArray("ConstructorStandings")

                        val result = response.getJSONObject("MRData")
                            .getJSONObject("StandingsTable")
                            .getJSONArray("StandingsLists")

                        val getBo = result.getJSONObject(0)
                        seasonString = getBo.getString("season")
                        roundString = getBo.getString("round")

                        for (i in 0 until driverStandings.length()) {
                            val driverData = driverStandings.getJSONObject(i)
                            val details = driverData.getJSONObject("Constructor")

                            val constBo = ConstructorBO().apply {
                                season = seasonString
                                round = roundString
                                consId = details.getString("constructorId")
                                name = details.getString("name")
                                points = driverData.getString("points")
                                wins = driverData.getString("wins")
                                position = driverData.getString("position")
                                nationality = details.getString("nationality")
                            }
                            constructorList.add(constBo)
                        }
                        insertConstructorData(constructorList)


                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }
            },
            { error: VolleyError -> error.printStackTrace() })

        requestQueue.add(jsonReq)


    }

    override suspend fun fetchLatestResults() {
        db = DbHandler(context, DataMembers.DB_NAME)
        val url = "http://ergast.com/api/f1/current/last/results.json"
        requestQueue = Volley.newRequestQueue(context)
        latestRoundResult = mutableListOf()

        val jsonReq = JsonObjectRequest(Request.Method.GET, url, null,
            {
            if(it != null){
                try{
                    val latestRaceJson = it.getJSONObject("MRData")
                        .getJSONObject("RaceTable")
                        .getJSONArray("Races")
                        .getJSONObject(0)
                        .getJSONArray("Results")

                    for(i in 0 until latestRaceJson.length()){
                        val data = latestRaceJson.getJSONObject(i)
                        val jsonData = data.getJSONObject("FastestLap")

                        val latestBO = Results().apply {
                            driverSeasonNumber = data.getInt("number")
                            position = data.getInt("position")
                            driverPermanentNumber = data.getJSONObject("Driver").getString("permanentNumber")
                            driverId = data.getJSONObject("Driver").getString("driverId")
                            roundPoint = data.getString("points")
                            startGrid = data.getString("grid")
                            totalLaps = data.getString("laps")
                            status = data.getString("status")

                            rank = jsonData.getString("rank")
                            fastestLapOn = jsonData.getString("lap")
                            fastestLapTime = jsonData.getJSONObject("Time").getString("time")
                            speedUnit = jsonData.getJSONObject("AverageSpeed").getString("units")
                            avgSpeed = jsonData.getJSONObject("AverageSpeed").getString("speed")
                        }
                        latestRoundResult.add(latestBO)

                    }
                    insertLatestResultsIntoDB(latestRoundResult)

                }catch (ex: Exception){
                    ex.printStackTrace()
                }
            }

            }, {
                error -> error.printStackTrace()
            })



        requestQueue.add(jsonReq)

    }

    override suspend fun fetchAllCurrentSeasonResult(db: DbHandler) {
        CoroutineTools.io {
           val url = "http://ergast.com/api/f1/current/results.json?limit=300"
            currentAllRaceResult = arrayListOf()
            requestQueue = Volley.newRequestQueue(context)

            val jsonReq = JsonObjectRequest(Request.Method.GET, url, null,
                {
                if(it != null){
                    try{
                        val responseJson = it.getJSONObject("MRData")
                            .getJSONObject("RaceTable")
                            .getJSONArray("Races")

                        for(i in 0 until responseJson.length()){
                            val resultsJson = responseJson.getJSONObject(i)
                            val Bo= CurrentSeasonBO().apply{
                                season = resultsJson.getString("season")
                                round = resultsJson.getString("round")
                                raceName = resultsJson.getString("raceName")
                                date = resultsJson.getString("date")
                                time = resultsJson.getString("time")

                                circuitId = resultsJson.getJSONObject("Circuit").getString("circuitId")
                                circuitName= resultsJson.getJSONObject("Circuit").getString("circuitName")

                                val raceResults = resultsJson.getJSONArray("Results")

                                for(j in 0 until raceResults.length()){
                                    val values = raceResults.getJSONObject(j)

                                    driverNumber = values.getString("number")
                                    driverPosition = values.getString("position")
                                    points = values.getString("points")

                                    driverId = values.getJSONObject("Driver").getString("driverId")
                                    driverCode = values.getJSONObject("Driver").getString("code")
                                    permanentNumber = values.getJSONObject("Driver").getString("permanentNumber")

                                    startPosition = values.getString("grid")

                                    laps = values.getString("laps")
                                    raceStatus = values.getString("status")

                                    driverRaceRank = "1" /*values
                                        .getJSONObject("FastestLap")
                                        .getString("rank")*/

                                    fastestLapSetOn = "1" /*values
                                        .getJSONObject("FastestLap")
                                        .getString("lap")
*/
                                    fastestLapTime =  "1" /*values
                                        .getJSONObject("FastestLap")
                                        .getJSONObject("Time")
                                        .getString("time")*/


                                    fastestSpeedInKPH = "1" /* values
                                        .getJSONObject("FastestLap")
                                        .getJSONObject("AverageSpeed")
                                        .getString("speed")*/

                                }
                            }
                            currentAllRaceResult.add(Bo)
                        }
                        insertRecords(currentAllRaceResult, db)

                    }catch(ex: Exception){
                        view!!.hideLoading()
                        ex.printStackTrace()
                    }
                }
                }, {
                    Commons().printException(it)
                })

            requestQueue.add(jsonReq)

        }
    }

    private fun insertRecords(list: ArrayList<CurrentSeasonBO>, db: DbHandler){
        db.createDB()
        db.openDB()

        try{
            if (isCheckDataAvailable(DataMembers.tbl_currentAllResults, db)) {
                db.deleteSQL(DataMembers.tbl_latestResults, true)
            }
            for(data in list){
                val sb = StringBuffer()
                sb.append(stringHandler.getQueryFormat(data.season))
                sb.append("," + stringHandler.getQueryFormat(data.round))
                sb.append("," + stringHandler.getQueryFormat(data.raceName))
                sb.append("," + stringHandler.getQueryFormat(data.date))
                sb.append("," + stringHandler.getQueryFormat(data.time))
                sb.append("," + stringHandler.getQueryFormat(data.circuitId))
                sb.append("," + stringHandler.getQueryFormat(data.circuitName))
                sb.append("," + stringHandler.getQueryFormat(data.driverNumber))
                sb.append("," + stringHandler.getQueryFormat(data.driverPosition))
                sb.append("," + stringHandler.getQueryFormat(data.points))
                sb.append("," + stringHandler.getQueryFormat(data.driverId))
                sb.append("," + stringHandler.getQueryFormat(data.driverCode))
                sb.append("," + stringHandler.getQueryFormat(data.permanentNumber))
                sb.append("," + stringHandler.getQueryFormat(data.startPosition))
                sb.append("," + stringHandler.getQueryFormat(data.laps))
                sb.append("," + stringHandler.getQueryFormat(data.raceStatus))
                sb.append("," + stringHandler.getQueryFormat(data.driverRaceRank))
                sb.append("," + stringHandler.getQueryFormat(data.fastestLapSetOn))
                sb.append("," + stringHandler.getQueryFormat(data.fastestLapTime))
                sb.append("," + stringHandler.getQueryFormat(data.fastestSpeedInKPH))

                db.insertSQL(tbl_currentAllResults, tbl_currentAllResultsCols, sb.toString())
            }
            doNextSteps()


        }catch (ex: Exception){
            ex.printStackTrace()
        }

    }

    override fun checkIfDataIsAvailable(db: DbHandler){
        try{
            db.openDB()
            val c1 = db.selectSql("SELECT * FROM DriverMaster")
            if(c1 != null){
                while(c1.moveToNext()){
                    flag = true
                }
            }
            val c2 = db.selectSql("SELECT * FROM ConstructorMaster")
            if(c2 != null){
                while(c2.moveToNext()){
                    flag = true
                }
            }

            val c3 = db.selectSql("SELECT * FROM RaceScheduleMaster")
            if(c3 != null){
                while(c3.moveToNext()){
                    flag = true
                }
            }

            val c4 = db.selectSql("SELECT * FROM LatestResultMaster")
            if(c4 != null){
                while(c4.moveToNext()){
                    flag =  true
                }
            }

            if(flag){
                view?.showDialog()
            }else{
                view?.showAlert()
            }
        }catch (ex: Exception){
            Commons().printException(ex)
        }
    }

    override fun insertLatestResultsIntoDB(list: MutableList<Results>) {
        db = DbHandler(context.applicationContext, DataMembers.DB_NAME)
        db.createDB()
        db.openDB()

        try {
            if (isCheckDataAvailable(DataMembers.tbl_latestResults, db)) {
                db.deleteSQL(DataMembers.tbl_latestResults, true)
            }

            for (finalList in list) {
                val sb = StringBuffer()
                sb.append(finalList.driverSeasonNumber)
                sb.append("," + finalList.position)
                sb.append("," + stringHandler.getQueryFormat(finalList.driverId))
                sb.append("," + stringHandler.getQueryFormat(finalList.driverPermanentNumber))
                sb.append("," + stringHandler.getQueryFormat(finalList.roundPoint))
                sb.append("," + stringHandler.getQueryFormat(finalList.startGrid))
                sb.append("," + stringHandler.getQueryFormat(finalList.totalLaps))
                sb.append("," + stringHandler.getQueryFormat(finalList.status))
                sb.append("," + stringHandler.getQueryFormat(finalList.rank))
                sb.append("," + stringHandler.getQueryFormat(finalList.fastestLapOn))
                sb.append("," + stringHandler.getQueryFormat(finalList.fastestLapTime))
                sb.append("," + stringHandler.getQueryFormat(finalList.speedUnit))
                sb.append("," + stringHandler.getQueryFormat(finalList.avgSpeed))

                db.insertSQL(DataMembers.tbl_latestResults, DataMembers.tbl_latestResultCols, sb.toString())

            }
        } catch (ex: Exception) {
            Commons().printException(ex)
        }

    }

    public fun isCheckDataAvailable(tblName: String, db: DbHandler): Boolean {
        try {
            db.openDB()
            val cursor = db.selectSql("Select * from ${tblName}")
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val count = cursor.getInt(0)
                    return count > 0
                }
            }
            cursor.close()
            return false

        } catch (ex: Exception) {
            ex.printStackTrace()
            return false
        }
    }

    fun insertRaceData(list: ArrayList<RaceScheduleBO>) {
        db = DbHandler(context.applicationContext, DataMembers.DB_NAME)
        db.createDB()
        db.openDB()

        if(isCheckDataAvailable(tbl_raceScheduleMaster,db)){
            db.deleteSQL(tbl_raceScheduleMaster, true)
        }
        for (i in 0 until list.size) {
            val rc = list.get(i)
            val raceValues = getRaceDetails(rc)
            db.insertSQL(
                tbl_raceScheduleMaster,
                DataMembers.tbl_raceScheduleCols,
                raceValues.toString()
            )

        }
        db.closeDB()
    }

    fun getRaceDetails(rc: RaceScheduleBO): StringBuffer {
        val sb = StringBuffer()
        sb.append(stringHandler.getQueryFormat(rc.season))
        sb.append("," + stringHandler.getQueryFormat(rc.round))
        sb.append("," + stringHandler.getQueryFormat(rc.raceName))
        sb.append("," + stringHandler.getQueryFormat(rc.date))
        sb.append("," + stringHandler.getQueryFormat(rc.time))
        sb.append("," + stringHandler.getQueryFormat(rc.circuitId))
        sb.append("," + stringHandler.getQueryFormat(rc.circuitName))
        sb.append("," + stringHandler.getQueryFormat(rc.lat))
        sb.append("," + stringHandler.getQueryFormat(rc.long))
        sb.append("," + stringHandler.getQueryFormat(rc.locality))
        sb.append("," + stringHandler.getQueryFormat(rc.country))
        return sb
    }

    fun doNextSteps(){
       CoroutineTools.main {
           view!!.hideLoading()
           view!!.moveToNextScreen()
       }
    }
}