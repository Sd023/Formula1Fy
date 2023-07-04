package com.sdapps.formula1fy.f1.fragments.home

import android.content.Context
import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.core.utils.Commons
import com.sdapps.formula1fy.f1.bo.ConstructorBO
import com.sdapps.formula1fy.f1.bo.DriverBO
import com.sdapps.formula1fy.f1.bo.LatestResult
import com.sdapps.formula1fy.f1.bo.RaceScheduleBO
import java.sql.Driver
import java.time.LocalDate

class HomePresenter(val context: Context) : HomeContractor.Presenter {

    private var view: HomeContractor.View? = null
    private lateinit var driverList: ArrayList<DriverBO>
    private var nextRoundList = arrayListOf<RaceScheduleBO>()
    private lateinit var constructorList: ArrayList<ConstructorBO>
    private lateinit var latestList: MutableList<LatestResult>
    private lateinit var constructorMap : HashMap<String, ArrayList<String>>

    override fun attachView(view: HomeContractor.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    fun roundListNumber(start: Int, count: Int): String{
        //generating a list of number from the start value. purpose is to get race list data
        val numberList = List(count){index -> start + index }
        return numberList.joinToString(",")
    }
    override suspend fun getNextRound(db: DbHandler) {
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

            /*calculating the next round number by parsing date */
            val roundDate = stringDate.mapNotNull { date -> LocalDate.parse(date) }
            val nextRoundNumber =
                if (roundDate.isNotEmpty()) {
                    roundDate.indexOfFirst { it > currentDate } + 1
                } else -1

            val nextRoundValues = roundListNumber(nextRoundNumber, 5)
            val c1 =
                db.selectSql("SELECT * FROM RaceScheduleMaster WHERE round IN ($nextRoundValues)")
            if (c1 != null) {
                while (c1.moveToNext()) {
                    val bo = RaceScheduleBO().apply {
                        season = c1.getString(c1.getColumnIndex("season"))
                        round = c1.getString(c1.getColumnIndex("round"))
                        raceName = c1.getString(c1.getColumnIndex("race_name"))
                        date = c1.getString(c1.getColumnIndex("date"))
                        time = c1.getString(c1.getColumnIndex("time"))
                        circuitId = c1.getString(c1.getColumnIndex("circuit_name"))
                    }
                    nextRoundList.add(bo)
                }
                c1.close()
            }
        } catch (ex: Exception) {
            Commons().printException(ex)
        }
        view!!.setNextRaceAdapter(nextRoundList)
    }

    override suspend fun getLatestRound(db: DbHandler) {
        latestList = mutableListOf()
        try{
            db.openDB()
            val cursor =
               db.selectSql("SELECT DISTINCT DM.driver_name,DM.constructor_name,LRM.start_grid,LRM.position,LRM.round_point,LRM.fastest_lap_time,LRM.fastest_lap,LRM.fastest_lap_avg_speed,LRM.speed_unit,LRM.status FROM LatestResultMaster LRM INNER JOIN DRIVERMASTER DM ON DM.driver_id = LRM.driver_id  ORDER BY position ASC;")
            if(cursor != null){
                while(cursor.moveToNext()){
                    val resultBo = LatestResult().apply {
                        driverName = cursor.getString(0)
                        teamName = cursor.getString(1)
                        latestRaceGridStart = cursor.getString(2)
                        latestRaceFinish = cursor.getString(3)
                        latestRoundPoints = cursor.getString(4)
                        latestRoundFLTime = cursor.getString(5)
                        latestRoundFLOn = cursor.getString(6)
                        latestRoundFLSpeed = cursor.getString(7)
                        speedUnits = cursor.getString(8)
                        latestRaceStatus = cursor.getString(9)
                    }
                    latestList.add(resultBo)
                }
                view?.setLatestResults(latestList)
            }
            cursor.close()
            db.closeDB()

        }catch (ex: Exception){
            Commons().printException(ex)
            db.closeDB()
        }


    }

    override suspend fun getDriverData(db: DbHandler) {

        driverList = ArrayList<DriverBO>()
        try {
            db.openDB()
            val cursor =
                db.selectSql("SELECT driver_id,driver_code,driver_name,driver_number,driver_constructor,wins,total_points,driver_position," +
                        " constructor_name FROM DriverMaster ORDER BY driver_position ASC")
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
                    }

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
        constructorMap = HashMap()
        constructorList = ArrayList<ConstructorBO>()
        try {
            db.openDB()
            try{
                val c = db.selectSql("SELECT constructor_id,constructor_name,constructor_wins,constructor_points,constructor_position,constructor_nationality FROM ConstructorMaster")
                if(c != null){
                    while(c.moveToNext()){
                        val constructorBO = ConstructorBO().apply {
                            consId = c.getString(0)
                            name = c.getString(1)
                            wins = c.getString(2)
                            points = c.getString(3)
                            position = c.getString(4)
                            nationality = c.getString(5)
                        }
                        constructorList.add(constructorBO)
                    }
                    c.close()
                }
            }catch (ex: Exception){
                Commons().printException(ex)
            }

            val c1 =
                db.selectSql("SELECT DM.driver_name,CM.constructor_id,CM.constructor_name,CM.constructor_wins,CM.constructor_points," +
                        "CM.constructor_position,CM.constructor_nationality from ConstructorMaster CM INNER JOIN DriverMaster DM ON CM.constructor_id = DM.driver_constructor ORDER BY CM.constructor_position ASC ")
            if (c1 != null) {
                var driverLists = arrayListOf<String>()
                var constructorName = String()
                var constructorNewBO = ConstructorBO()
                while (c1.moveToNext()) {
                    val driverName = c1.getString(0)
                    if(constructorName != c1.getString(2)){
                        if(constructorName.isNotEmpty()){
                            constructorMap[constructorName] = driverLists
                            driverLists = arrayListOf<String>()
                            driverLists.add(driverName)
                            constructorName = c1.getString(2)
                        }else{
                            driverLists.add(driverName)
                            constructorName = c1.getString(2)
                        }
                    }else{
                        driverLists.add(driverName)
                    }


                }
                if(driverLists.size > 0){
                    constructorMap[constructorName] = driverLists
                }

                view?.setConstructorAdapter(constructorList ,constructorMap)
                c1.close()
                db.closeDB()
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
            db.closeDB()
        }

    }
    public fun getDriverConstructorMap(): HashMap<String, ArrayList<String>>{
        return constructorMap
    }
}