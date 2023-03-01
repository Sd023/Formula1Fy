package com.sdapps.formula1fy.login

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sdapps.formula1fy.core.DbHandler
import org.json.JSONObject

class LoginScreenPresenter(val context: Context) : LoginContractor.Presenter {

    private var driverList: ArrayList<DriverBO>? = null
    private lateinit var requestQueue: RequestQueue
    private lateinit var db: DbHandler

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
                            totalPoints = driverData.getString("points")
                            wins = driverData.getString("wins")
                            driverId = details.getString("driverId")
                            driverName = details.getString("givenName") + " " + details.getString("familyName")
                            driverDOB = details.getString("dateOfBirth")
                            driverNationality = details.getString("nationality")
                            driverNumber = details.getString("permanentNumber")
                            driverCode = details.optString("code", "")
                            constructorName = driverData.getJSONArray("Constructors").getJSONObject(0).getString("name")
                        }

                        driverList.add(driver)
                    }

                    fetchConstructorData(driverList)
                    Log.d("LIST SIZE", "driverList: -> " + driverList.size)

                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            },
            { error: VolleyError -> error.printStackTrace() })

        requestQueue.add(jsonReq)



    }

    override fun fetchConstructorData(list:ArrayList<DriverBO>) {
        db = DbHandler(context.applicationContext,"F1db.sqlite")
        db.createDB()
        db.openDB()
        val col = "driverId"
        for(driverDetails in list)
        {
            val values = driverDetails.driverId
            db.insertSQL("DriverMaster",col,values)
            Log.d("DHANUSH_TEST", "values : $values")
        }

        db.closeDB()
    }



    override fun fetchCircuitData() {
        TODO("Not yet implemented")
    }

    override fun getDriverValues(tblName: String, columns: String, values: String): String {
        TODO("Not yet implemented")
    }


}