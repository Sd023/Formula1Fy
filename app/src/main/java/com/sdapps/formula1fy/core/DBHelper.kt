package com.sdapps.formula1fy.core

import android.util.Log

object DBHelper {
    fun createTables(dbHandler: DbHandler){
        try{
            dbHandler.exe("create table if not exists DriverMaster (driverId TEXT, " +
                    "driverCode TEXT, driverName TEXT, driverNumber TEXT)")

            dbHandler.exe("create table if not exists ConstructorMaster (constructor_id TEXT," +
                    "constructor_name TEXT,constructor_wins TEXT," +
                    "constructor_points TEXT,constructor_position TEXT," +
                    "constructor_nationality TEXT)")

        }catch (ex:Exception){
            Log.d("Exception occured", ": $ex")
        }
    }
}