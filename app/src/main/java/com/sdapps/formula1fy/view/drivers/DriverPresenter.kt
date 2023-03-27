package com.sdapps.formula1fy.view.drivers

import android.content.Context
import com.sdapps.formula1fy.core.dbUtil.DbHandler

class DriverPresenter(val context: Context) : DriverInteractor.Presenter {

    private var view: DriverInteractor.View? = null
    private lateinit var driverList: ArrayList<DriverBO>

    override fun attachView(view: DriverInteractor.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun getDriverData(db: DbHandler): ArrayList<DriverBO> {
        driverList = ArrayList<DriverBO>()
        try {
            db.openDB()
            val cursor =
                db.selectSql("SELECT driver_id,driver_code,driver_name,driver_number, driver_constructor,wins,total_points,driver_position from DriverMaster ORDER BY driver_position ASC")
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val driverBO = DriverBO()
                    driverBO.driverId = cursor.getString(0)
                    driverBO.driverCode = cursor.getString(1)
                    driverBO.driverName = cursor.getString(2)
                    driverBO.driverNumber = cursor.getInt(3)
                    driverBO.constructorId = cursor.getString(4)
                    driverBO.wins = cursor.getInt(5)
                    driverBO.totalPoints = cursor.getInt(6)
                    driverBO.driverPosition = cursor.getString(7)
                    driverList.add(driverBO)
                }
            }
            cursor.close()
            db.closeDB()

        } catch (ex: Exception) {
            ex.printStackTrace()

        }
        return driverList
    }
}