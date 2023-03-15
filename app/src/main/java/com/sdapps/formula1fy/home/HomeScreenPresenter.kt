package com.sdapps.formula1fy.home

import android.content.Context
import com.sdapps.formula1fy.ModelBO.ConstructorBO
import com.sdapps.formula1fy.ModelBO.DriverBO
import com.sdapps.formula1fy.core.DBHelper
import com.sdapps.formula1fy.core.DataMembers
import com.sdapps.formula1fy.core.DbHandler

class HomeScreenPresenter(val context: Context) : HomeScreenInteractor.Presenter {

    private var view: HomeScreenInteractor.View? = null
    private lateinit var driverList: ArrayList<DriverBO>
    private var constructorList: ArrayList<ConstructorBO>? = null
    override fun setupView(view: HomeScreenInteractor.View) {
        this.view = view
    }

    override fun getDriverData(dbHandler: DbHandler): ArrayList<DriverBO> {
        driverList = ArrayList<DriverBO>()
        try {
            dbHandler.openDB()
            val cursor =
                dbHandler.selectSql("SELECT driver_id,driver_code,driver_name,driver_number, driver_constructor,wins,total_points from DriverMaster ORDER BY total_points DESC")
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
                    driverList!!.add(driverBO)
                }
            }
            cursor.close()
            dbHandler.closeDB()

        } catch (ex: Exception) {
            ex.printStackTrace()

        }
        return driverList
    }

    override fun getConstructorData(db: DbHandler): ArrayList<ConstructorBO>? {
        constructorList = ArrayList<ConstructorBO>()
        try {
            db.openDB()
            val c =
                db.selectSql("SELECT constructor_id,constructor_name,constructor_wins,constructor_points,constructor_position,constructor_nationality from ConstructorMaster")
            if (c != null) {
                while (c.moveToNext()) {
                    val bo = ConstructorBO()
                    bo.consId = c.getString(0)
                    bo.name = c.getString(1)
                    bo.wins = c.getString(2)
                    bo.points = c.getString(3)
                    bo.position = c.getString(4)
                    bo.nationality = c.getString(5)
                    constructorList!!.add(bo)
                }
            }
            c!!.close()
            db.closeDB()

        } catch (ex: Exception) {
            ex.printStackTrace()
            db.closeDB()
        }
        return constructorList
    }


}