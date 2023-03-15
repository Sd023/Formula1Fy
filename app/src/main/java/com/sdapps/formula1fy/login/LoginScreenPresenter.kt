package com.sdapps.formula1fy.login

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sdapps.formula1fy.ModelBO.ConstructorBO
import com.sdapps.formula1fy.ModelBO.DriverBO
import com.sdapps.formula1fy.core.DataMembers
import com.sdapps.formula1fy.core.DbHandler
import com.sdapps.formula1fy.core.StringHelper
import com.sdapps.formula1fy.home.HomeScreenPresenter
import org.json.JSONObject

class LoginScreenPresenter(val context: Context) : LoginContractor.Presenter {

    private lateinit var requestQueue: RequestQueue
    private lateinit var db: DbHandler
    private var stringHandler: StringHelper = StringHelper()
    private lateinit var authFirebase: FirebaseAuth
    private var view: LoginContractor.View? = null

    override fun attachView(view: LoginContractor.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun performLogin(email: String, password: String) {
        view?.showLoading()
        authFirebase = Firebase.auth
        try {
            authFirebase.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    fetchDriverData()
                } else {
                    onError(it.exception!!.message)
                }
            }
        } catch (ex: Exception) {
            Log.d("TAG", "Could not able to login! -> $ex")
        }
    }

    override fun fetchDriverData() {
        requestQueue = Volley.newRequestQueue(context)
        val url = "https://ergast.com/api/f1/current/driverStandings.json"
        val driverList = ArrayList<DriverBO>()
        val jsonReq = JsonObjectRequest(Request.Method.GET, url, null,
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

                    insertDriverDatasToDB(driverList)

                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            },
            { error: VolleyError -> error.printStackTrace() })

        requestQueue.add(jsonReq)

        fetchConstructorData()
    }

    override fun fetchConstructorData() {
        val url = "https://ergast.com/api/f1/current/constructorStandings.json"
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

                    insertConstructorDataTODB(constructorList)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            },
            { error: VolleyError -> error.printStackTrace() })

        requestQueue.add(jsonReq)


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

    override fun insertDriverDatasToDB(list: ArrayList<DriverBO>) {
        db = DbHandler(context.applicationContext, DataMembers.DB_NAME)
        db.createDB()
        db.openDB()

        val col =
            "driver_id,driver_code,driver_name,driver_number,driver_constructor,wins,total_points"
        for (i in 0 until list.size) {
            val bo = list.get(i)
            val values = getDriverDetails(bo)
            db.insertSQL(DataMembers.tbl_driverMaster, col, values.toString())
        }
        db.closeDB()

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
        db.closeDB()
        view?.hideLoading()
        view?.moveToNextScreen()
    }

    override fun onError(msg: String?) {
        Log.d("TAG_LOGIN", "Login Error msg -> $msg")
    }


    override fun fetchCircuitData() {
        TODO("Not yet implemented")
    }


}
