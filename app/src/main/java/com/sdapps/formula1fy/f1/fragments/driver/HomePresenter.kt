package com.sdapps.formula1fy.f1.fragments.driver

import android.content.Context
import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.f1.bo.ConstructorBO
import com.sdapps.formula1fy.f1.bo.DriverBO
import com.sdapps.formula1fy.f1.bo.RaceScheduleBO
import java.time.LocalDate

class HomePresenter(val context: Context) : HomeContractor.Presenter {

    private var view: HomeContractor.View? = null
    private lateinit var driverList: ArrayList<DriverBO>
    private var nextRoundList = mutableListOf<RaceScheduleBO>()
    private lateinit var constructorList: ArrayList<ConstructorBO>

    override fun attachView(view: HomeContractor.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override suspend fun getNextRound(db: DbHandler): MutableList<RaceScheduleBO> {
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

    override suspend fun getDriverData(db: DbHandler) {

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

                view?.setDriverAdapter(driverList)


            }
            cursor.close()
            db.closeDB()

        } catch (ex: Exception) {
            ex.printStackTrace()

        }


    }

    override suspend fun getConstructorData(db: DbHandler) {
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
                    constructorList.add(bo)
                }

                view?.setConstructorAdapter(constructorList)

            }
            c!!.close()
            db.closeDB()

        } catch (ex: Exception) {
            ex.printStackTrace()
            db.closeDB()
        }

    }
}