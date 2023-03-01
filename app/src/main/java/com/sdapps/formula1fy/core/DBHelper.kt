package com.sdapps.formula1fy.core

import android.util.Log

object DBHelper {
    fun createTables(dbHandler: DbHandler){
        try{
            dbHandler.exe("create table if not exists DriverMaster (driverId TEXT, " +
                    "driverCode TEXT, driverName TEXT, driverNumber TEXT)")
        }catch (ex:Exception){
            Log.d("Exception occured", ": $ex")
        }
    }
}