package com.sdapps.formula1fy.core.models

object DataMembers {
    const val tbl_driverMaster: String = "DriverMaster"
    const val tbl_constructorMaster: String = "ConstructorMaster"
    const val DB_NAME: String = "sd_f1.sqlite"

    const val tbl_constructorMasterCols = "season,round,constructor_id,constructor_name,constructor_wins,constructor_points,constructor_position,constructor_nationality"
    const val tbl_driverMasterCols = "season,round,driver_id,driver_code,driver_name,driver_number,driver_constructor,wins,total_points,driver_position,constructor_name"
}

