package com.sdapps.formula1fy.f1.landing

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.core.models.DataMembers
import com.sdapps.formula1fy.core.utils.Commons
import com.sdapps.formula1fy.core.utils.CoroutineTools
import com.sdapps.formula1fy.core.utils.StringHelper
import com.sdapps.formula1fy.f1.bo.ConstructorBO
import com.sdapps.formula1fy.f1.bo.DriverBO
import kotlinx.coroutines.delay
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class LandingPresenter(val appContext: Context): LandingContractor.Presenter{

    private lateinit var context: Context
    private var view : LandingContractor.View? = null

    private lateinit var requestQueue: RequestQueue
    private lateinit var db: DbHandler
    private var stringHandler: StringHelper = StringHelper()
    private  var seasonString : String? = null
    private  var roundString: String? = null

    override fun attachView(view: LandingContractor.View, context: Context) {
      this.view = view
      this.context = context
    }

    override fun detachView() {
        this.view = null
    }


    override fun getAppString() : SpannableStringBuilder {

        val builder : SpannableStringBuilder = SpannableStringBuilder()
        val appFirst : SpannableString = SpannableString("Formula1")
        appFirst.setSpan(ForegroundColorSpan(Color.WHITE), 0, appFirst.length,0)
        builder.append(appFirst)

        val appSecond : SpannableString = SpannableString("Fy")
        appSecond.setSpan(ForegroundColorSpan(Color.RED), 0, appSecond.length,0)
        builder.append(appSecond)

        return builder

    }

    override suspend fun fetchAllData() {
        view?.showLoading()
        CoroutineTools.io {
            db = DbHandler(context.applicationContext, DataMembers.DB_NAME)
            requestQueue = Volley.newRequestQueue(context)
            val url = "https://ergast.com/api/f1/current/driverStandings.json"
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
                                constructorName = driverData.getJSONArray("Constructors").getJSONObject(0).getString("name")
                            }
                            driverList.add(dBO)
                        }

                        if (!isCheckDataAvailable(true, db)) {
                            insertDriverData(driverList)
                        } else {
                            updateDb(driverList, null, true)
                        }


                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                },
                { error: VolleyError -> error.printStackTrace() })

            requestQueue.add(jsonReq)

            fetchConstructorDataFromServer()

        }
    }


    fun getDriverDetails(driverBO: DriverBO) : StringBuffer{
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

        return sb
    }

    override fun insertDriverData(list: ArrayList<DriverBO>) {
        db = DbHandler(context.applicationContext, DataMembers.DB_NAME)
        db.createDB()
        db.openDB()

        for (i in 0 until list.size) {
            val bo = list.get(i)
            val values = getDriverDetails(bo)
            db.insertSQL(DataMembers.tbl_driverMaster, DataMembers.tbl_driverMasterCols, values.toString())
        }
        db.closeDB()
    }



    suspend fun fetchConstructorDataFromServer(){
        CoroutineTools.io {
            val url = "https://ergast.com/api/f1/current/constructorStandings.json"
            requestQueue = Volley.newRequestQueue(context)
            db = DbHandler(context, DataMembers.DB_NAME)
            val constructorList = ArrayList<ConstructorBO>()
            val jsonReq = JsonObjectRequest(Request.Method.GET, url, null,
                { response: JSONObject ->
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
                                season  = seasonString
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


                        if (!isCheckDataAvailable(false, db)) {
                            insertConstructorData(constructorList)
                        } else {
                            updateDb(null, constructorList, false)
                        }

                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                },
                { error: VolleyError -> error.printStackTrace() })

            requestQueue.add(jsonReq)
        }
        view!!.moveToNextScreen()
    }

    fun getConstructorDetails(constructorBO: ConstructorBO): StringBuffer{
        val sb = StringBuffer()
        sb.append(stringHandler.getQueryFormat(constructorBO.season))
        sb.append("," + stringHandler.getQueryFormat(constructorBO.round))
        sb.append(","+ stringHandler.getQueryFormat(constructorBO.consId))
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

        for (i in 0 until list.size) {
            val co = list.get(i)
            val constructorValues = getConstructorDetails(co)
            db.insertSQL(DataMembers.tbl_constructorMaster, DataMembers.tbl_constructorMasterCols, constructorValues.toString())

        }
        db.closeDB()
        view!!.hideLoading()
    }

    private fun updateDb(
        list: ArrayList<DriverBO>?,
        consList: ArrayList<ConstructorBO>?,
        isDriver: Boolean
    ) {

        CoroutineTools.io {
            if (isDriver) {
                for (i in 0 until list!!.size) {
                    val points = list[i].totalPoints
                    val wins = list[i].wins
                    val position = list[i].driverPosition
                    val code = list[i].driverCode
                    val season = list[i].season
                    val round = list[i].round
                    try {
                        val sql = "UPDATE ${DataMembers.tbl_driverMaster} set season = ${stringHandler.getQueryFormat(season)}, round = ${stringHandler.getQueryFormat(round)}, wins = ${stringHandler.getQueryFormat(wins.toString())}" +
                                ", total_points = ${stringHandler.getQueryFormat(points.toString())}, " +
                                "driver_position = ${stringHandler.getQueryFormat(position)} WHERE driver_code = ${
                                    stringHandler.getQueryFormat(
                                        code
                                    )
                                }"
                        db.updateSQL(sql)

                    } catch (exception: Exception) {
                        Commons().print(exception.message)
                    }

                }
            } else {
                for (i in 0 until consList!!.size) {
                    val points = consList[i].points
                    val wins = consList[i].wins
                    val position = consList[i].position
                    val code = consList[i].consId
                    val season = consList[i].season
                    val round = consList[i].round

                    try {
                        val sql =
                            "UPDATE ${DataMembers.tbl_constructorMaster} set season = ${stringHandler.getQueryFormat(season)}, round = ${stringHandler.getQueryFormat(round)}, constructor_wins = ${
                                stringHandler.getQueryFormat(wins.toString())
                            }" +
                                    ", constructor_points = ${stringHandler.getQueryFormat(points.toString())}, " +
                                    "constructor_position = ${stringHandler.getQueryFormat(position)} WHERE constructor_id = ${
                                        stringHandler.getQueryFormat(
                                            code
                                        )
                                    }"
                        db.updateSQL(sql)

                    } catch (exception: Exception) {
                        Commons().print(exception.message)
                    }


                }
            }


        }
        view!!.hideLoading()
    }

    private fun isCheckDataAvailable(flag: Boolean, db: DbHandler): Boolean {
        db.openDB()
        if (flag) {
            val cursor = db.selectSql("Select * from DriverMaster")
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    return true
                }
            }
            cursor.close()
        } else {
            val cursor1 = db.selectSql("Select * from ConstructorMaster")
            if (cursor1 != null) {
                while (cursor1.moveToNext()) {
                    return true
                }
            }
            cursor1.close()
        }
        db.closeDB()

        return false
    }
}