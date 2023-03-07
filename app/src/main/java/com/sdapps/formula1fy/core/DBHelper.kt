package com.sdapps.formula1fy.core

import android.util.Log

object DBHelper {
    fun createTables(dbHandler: DbHandler) {
        try {
            dbHandler.exe(
                "create table if not exists DriverMaster (driver_id TEXT, " +
                        "driver_code TEXT, driverName TEXT, driver_number INTEGER UNIQUE PRIMARY KEY, driver_constructor TEXT,wins INTEGER,total_points INTEGER)"
            )

            dbHandler.exe(
                "create table if not exists ConstructorMaster (constructor_id TEXT," +
                        "constructor_name TEXT,constructor_wins TEXT," +
                        "constructor_points TEXT,constructor_position TEXT," +
                        "constructor_nationality TEXT)"
            )
        } catch (ex: Exception) {
            Log.d("Exception occurred", ": $ex")
        }
    }
}