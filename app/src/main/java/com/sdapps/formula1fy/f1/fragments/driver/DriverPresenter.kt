package com.sdapps.formula1fy.f1.fragments.driver

import android.content.Context
import android.util.Log
import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.f1.bo.DriverBO
import com.sdapps.formula1fy.f1.bo.RaceScheduleBO
import java.time.LocalDate

class DriverPresenter(val context: Context) : DriverInteractor.Presenter {

    private var view: DriverInteractor.View? = null
    private lateinit var driverList: ArrayList<DriverBO>
    private var nextRoundList = mutableListOf<RaceScheduleBO>()

    override fun attachView(view: DriverInteractor.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun getNextRound(db: DbHandler): MutableList<RaceScheduleBO> {
        try {
            val currentDate = LocalDate.now()
            val stringDate = mutableListOf<String>()
            db.openDB()
            val cursor = db.selectSql("SELECT date from RaceScheduleMaster")
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val dateString = cursor.getString(cursor.getColumnIndex("date"))
                    stringDate.add(dateString)
                }
                cursor.close()
            }

            val roundDate = stringDate.mapNotNull { date -> LocalDate.parse(date) }
            val nextRoundNumber =
                if (roundDate.isNotEmpty()) {
                    roundDate.indexOfFirst { it > currentDate } + 1
                } else -1

            val c1 =
                db.selectSql("SELECT * FROM RaceScheduleMaster WHERE round = ${nextRoundNumber}")
            if (c1 != null) {
                while (c1.moveToNext()) {
                    val bo = RaceScheduleBO().apply {
                        season = c1.getString(c1.getColumnIndex("season"))
                        round = c1.getString(c1.getColumnIndex("round"))
                        raceName = c1.getString(c1.getColumnIndex("race_name"))
                        date = c1.getString(c1.getColumnIndex("date"))
                        time = c1.getString(c1.getColumnIndex("time"))
                        circuitId = c1.getString(c1.getColumnIndex("circuit_id"))
                    }
                    nextRoundList.add(bo)
                }
                c1.close()
            }
        } catch (ex: Exception) {
            nextRoundList
        }

        return nextRoundList
    }

    override fun getDriverData(db: DbHandler): ArrayList<DriverBO> {
        driverList = ArrayList<DriverBO>()
        try {
            db.openDB()
            val cursor =
                db.selectSql("SELECT driver_id,driver_code,driver_name,driver_number, driver_constructor,wins,total_points,driver_position,constructor_name from DriverMaster ORDER BY driver_position ASC")
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
                    driverBO.constructorName = cursor.getString(8)
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