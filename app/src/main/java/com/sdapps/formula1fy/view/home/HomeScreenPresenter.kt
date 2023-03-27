package com.sdapps.formula1fy.view.home

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sdapps.formula1fy.core.Commons
import com.sdapps.formula1fy.core.DataMembers
import com.sdapps.formula1fy.core.DbHandler
import com.sdapps.formula1fy.core.StringHelper
import com.sdapps.formula1fy.view.constructors.ConstructorBO
import com.sdapps.formula1fy.view.drivers.DriverBO
import org.json.JSONObject

class HomeScreenPresenter(val context: Context) : HomeScreenInteractor.Presenter {

    private lateinit var requestQueue: RequestQueue
    private lateinit var db: DbHandler
    private var stringHandler: StringHelper = StringHelper()

    private var view: HomeScreenInteractor.View? = null
    override fun setupView(view: HomeScreenInteractor.View) {
        this.view = view
    }

    override fun fetchDriverData() {
        db = DbHandler(context.applicationContext, DataMembers.DB_NAME)
        view?.showLoading()
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

                    for (i in 0 until driverStandings.length()) {
                        val driverData = driverStandings.getJSONObject(i)
                        val details = driverData.getJSONObject("Driver")

                        val driver = DriverBO().apply {
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
                        }

                        driverList.add(driver)
                    }

                    if (!isCheckDataAvailable(db)) {
                        insertDriverDatasToDB(driverList)
                    } else {
                        updateDb(driverList, null, true)
                    }


                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            },
            { error: VolleyError -> error.printStackTrace() })

        requestQueue.add(jsonReq)
    }

    override fun insertDriverDatasToDB(list: ArrayList<DriverBO>) {
        db = DbHandler(context.applicationContext, DataMembers.DB_NAME)
        db.createDB()
        db.openDB()

        val col =
            "driver_id,driver_code,driver_name,driver_number,driver_constructor,wins,total_points,driver_position"
        for (i in 0 until list.size) {
            val bo = list.get(i)
            val values = getDriverDetails(bo)
            db.insertSQL(DataMembers.tbl_driverMaster, col, values.toString())
        }
        db.closeDB()
        view?.hideLoading()
        moveToNextScreen(true)
    }

    override fun insertConstructorDataTODB(list: ArrayList<ConstructorBO>) {
        db = DbHandler(context.applicationContext, DataMembers.DB_NAME)
        db.createDB()
        db.openDB()

        val col =
            "constructor_id,constructor_name,constructor_wins,constructor_points,constructor_position,constructor_nationality"
        for (i in 0 until list.size) {
            val co = list.get(i)
            val constructorValues = getConstructorDetails(co)
            db.insertSQL(DataMembers.tbl_constructorMaster, col, constructorValues.toString())

        }
        view?.hideLoading()
        db.closeDB()
        moveToNextScreen(false)
    }


    override fun fetchConstructorData() {
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

                    for (i in 0 until driverStandings.length()) {
                        val driverData = driverStandings.getJSONObject(i)
                        val details = driverData.getJSONObject("Constructor")

                        val constructor = ConstructorBO().apply {
                            consId = details.getString("constructorId")
                            name = details.getString("name")
                            points = driverData.getString("points")
                            wins = driverData.getString("wins")
                            position = driverData.getString("position")
                            nationality = details.getString("nationality")
                        }

                        constructorList.add(constructor)
                    }

                    if (!isCheckDataAvailable(db)) {
                        insertConstructorDataTODB(constructorList)
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

    override fun isCheckDataAvailable(db: DbHandler): Boolean {
        db.openDB()
        val cursor = db.selectSql("Select * from DriverMaster")
        if (cursor != null) {
            while (cursor.moveToNext()) {
                return true
            }
        }
        cursor.close()

        val cursor1 = db.selectSql("Select * from ConstructorMaster")
        if (cursor1 != null) {
            while (cursor1.moveToNext()) {
                return true
            }
        }
        cursor1.close()
        db.closeDB()

        return false
    }

    private fun moveToNextScreen(isDriver: Boolean) {
        view?.moveToNextScreen(isDriver)
    }

    private fun getDriverDetails(driverBO: DriverBO): StringBuffer {
        val sb = StringBuffer()
        sb.append(stringHandler.getQueryFormat(driverBO.driverId))
        sb.append("," + stringHandler.getQueryFormat(driverBO.driverCode))
        sb.append("," + stringHandler.getQueryFormat(driverBO.driverName))
        sb.append("," + driverBO.driverNumber)
        sb.append("," + stringHandler.getQueryFormat(driverBO.constructorId))
        sb.append("," + driverBO.wins)
        sb.append("," + driverBO.totalPoints)
        sb.append("," + driverBO.driverPosition)

        return sb
    }

    private fun getConstructorDetails(constructorBO: ConstructorBO): StringBuffer {
        val sb = StringBuffer()
        sb.append(stringHandler.getQueryFormat(constructorBO.consId))
        sb.append("," + stringHandler.getQueryFormat(constructorBO.name))
        sb.append("," + stringHandler.getQueryFormat(constructorBO.wins))
        sb.append("," + stringHandler.getQueryFormat(constructorBO.points))
        sb.append("," + stringHandler.getQueryFormat(constructorBO.position))
        sb.append("," + stringHandler.getQueryFormat(constructorBO.nationality))


        return sb

    }

    private fun updateDb(
        list: ArrayList<DriverBO>?,
        consList: ArrayList<ConstructorBO>?,
        isDriver: Boolean
    ) {

        if (isDriver) {
            for (i in 0 until list!!.size) {
                val points = list[i].totalPoints
                val wins = list[i].wins
                val position = list[i].driverPosition
                val code = list[i].driverCode
                try {
                    val sql = "UPDATE ${DataMembers.tbl_driverMaster} set wins = ${
                        stringHandler.getQueryFormat(wins.toString())
                    }" +
                            ", total_points = ${stringHandler.getQueryFormat(points.toString())}, " +
                            "driver_position = ${stringHandler.getQueryFormat(position)} WHERE driver_code = ${
                                stringHandler.getQueryFormat(
                                    code
                                )
                            }"
                    db.updateSQL(sql)

                } catch (exception: Exception) {
                    view?.onError()
                    Commons().print(exception.message)
                }

            }
        } else {
            for (i in 0 until consList!!.size) {
                val points = consList[i].points
                val wins = consList[i].wins
                val position = consList[i].position
                val code = consList[i].consId

                try {
                    val sql = "UPDATE ${DataMembers.tbl_constructorMaster} set constructor_wins = ${
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
                    view?.onError()
                    Commons().print(exception.message)
                }


            }

        }

        view?.hideLoading()
    }

}