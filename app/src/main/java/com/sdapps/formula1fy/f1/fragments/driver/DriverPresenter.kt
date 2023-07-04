package com.sdapps.formula1fy.f1.fragments.driver

import android.content.Context
import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.f1.bo.DriverBO

class DriverPresenter(val context : Context) : DriverInteractor.Presenter{
    private lateinit var view: DriverInteractor.View
    private lateinit var driverList: ArrayList<DriverBO>

    override fun attachView(view: DriverInteractor.View) {
        this.view = view
    }

    override suspend fun getDriverData(db: DbHandler) {
        driverList = ArrayList<DriverBO>()
        try {
            db.openDB()
            val cursor =
                db.selectSql("SELECT driver_id,driver_code,driver_name,driver_number,driver_constructor,wins,total_points,driver_position," +
                        " constructor_name,date_of_birth,nationality FROM DriverMaster ORDER BY driver_position ASC")
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val driverBO = DriverBO().apply {
                        driverId = cursor.getString(0)
                        driverCode = cursor.getString(1)
                        driverName = cursor.getString(2)
                        driverNumber = cursor.getInt(3)
                        constructorId = cursor.getString(4)
                        wins = cursor.getInt(5)
                        totalPoints = cursor.getInt(6)
                        driverPosition = cursor.getString(7)
                        constructorName = cursor.getString(8)
                        driverDOB = cursor.getString(9)
                        driverNationality = cursor.getString(10)
                    }

                    driverList.add(driverBO)
                }

                view.setUpDriverData(driverList)


            }
            cursor.close()
            db.closeDB()

        } catch (ex: Exception) {
            ex.printStackTrace()

        }
    }

    override suspend fun handleCardClick(driverBO: DriverBO) {
        view.showToast(driverBO)
    }
}