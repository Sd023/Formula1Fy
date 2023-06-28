package com.sdapps.formula1fy.core.dbUtil

import android.util.Log

object DBHelper {
    fun createTables(dbHandler: DbHandler) {
        try {
            dbHandler.exe(
                "create table if not exists DriverMaster (season TEXT, round TEXT, driver_id TEXT, " +
                        "driver_code TEXT, driverName TEXT, driver_number INTEGER UNIQUE PRIMARY KEY, driver_constructor TEXT,wins INTEGER,total_points INTEGER, driver_position INTEGER, constructor_name TEXT)"
            )

            dbHandler.exe(
                "create table if not exists ConstructorMaster (constructor_id TEXT," +
                        "constructor_name TEXT,constructor_wins TEXT," +
                        "constructor_points TEXT,constructor_position TEXT," +
                        "constructor_nationality TEXT)"
            )
            dbHandler.exe("create table if not exists RaceScheduleMaster (season TEXT, round TEXT, race_name TEXT, date TEXT, time TEXT, circuit_id TEXT,circuit_name TEXT, latitude TEXT, longitude TEXT, locality TEXT, country TEXT)")
            dbHandler.exe("create table if not exists LatestResultMaster (number INTEGER,position INTEGER,driver_id TEXT,permanent_number TEXT,round_point TEXT,start_grid TEXT,total_laps TEXT,status TEXT,rank TEXT,fastest_lap TEXT,fastest_lap_time TEXT,speed_unit TEXT,fastest_lap_avg_speed TEXT)")
        } catch (ex: Exception) {
            Log.d("Exception occurred", ": $ex")
        }
    }
}