package com.sdapps.formula1fy.core.models

object DataMembers {
    const val DB_NAME: String = "sd_f1.sqlite"

    const val tbl_driverMaster: String = "DriverMaster"
    const val tbl_constructorMaster: String = "ConstructorMaster"
    const val tbl_raceScheduleMaster: String = "RaceScheduleMaster"
    const val tbl_latestResults : String = "LatestResultMaster"
    const val tbl_currentAllResults : String = "CurrentSeasonResults"

    const val tbl_constructorMasterCols = "season,round,constructor_id,constructor_name,constructor_wins,constructor_points,constructor_position,constructor_nationality"
    const val tbl_driverMasterCols = "season,round,driver_id,driver_code,driver_name,driver_number,driver_constructor,wins,total_points,driver_position,constructor_name,date_of_birth,nationality"
    const val tbl_raceScheduleCols = "season,round,race_name,date,time,circuit_id,circuit_name,latitude,longitude,locality,country"
    const val tbl_latestResultCols = "number,position,driver_id,permanent_number,round_point,start_grid,total_laps,status,rank,fastest_lap,fastest_lap_time,speed_unit,fastest_lap_avg_speed"
    const val tbl_currentAllResultsCols = "season,round,race_name,date,time,circuit_id,circuit_name,driver_number,driver_position,points,driver_id,driver_code,driver_per_number,start_position,laps,race_status,driver_rank,fastest_lap_set_on,fastest_lap_time,fastest_speed"
}

