package com.sdapps.formula1fy.f1.fragments.raceschedule

import android.content.Context
import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.core.models.DataMembers
import com.sdapps.formula1fy.core.utils.Commons
import com.sdapps.formula1fy.f1.bo.RaceScheduleBO
import java.time.LocalDate

class RaceSchedulePresenter(context: Context): RaceScheduleInteractor.Presenter{

    private lateinit var view: RaceScheduleInteractor.View
    private lateinit var dbHandler: DbHandler
    private var context: Context = context.applicationContext
    private lateinit var calendarList : ArrayList<RaceScheduleBO>


    override fun attachView(view: RaceScheduleInteractor.View) {
       this.view = view
    }

    override fun getRaceCalendar() {
        try{
            dbHandler = DbHandler(context, DataMembers.DB_NAME)
            dbHandler.openDB()
            calendarList = arrayListOf()
            val c1 = dbHandler.selectSql("select round,race_name,date,locality,country from RaceScheduleMaster")
            if(c1 != null){
                while (c1.moveToNext()){
                    val newRaceBo = RaceScheduleBO().apply {
                        round = c1.getString(0)
                        raceName = c1.getString(1)
                        date = c1.getString(2)
                        locality = c1.getString(3)
                        country = c1.getString(4)
                    }
                    calendarList.add(newRaceBo)
                }
                c1.close()
                dbHandler.closeDB()
            }
            view.setAdapter(calendarList)
        }catch (ex: Exception){
            Commons().printException(ex)
        }

    }

    override fun getCurrentRound(db: DbHandler): Int {
        try{
            var currentRoundNumber: Int
            val currentDate = LocalDate.now()
            val stringDate = mutableListOf<String>()

            db.openDB()
            val cursor = db.selectSql("SELECT date from RaceScheduleMaster")
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val dateString = cursor.getString(0)
                    stringDate.add(dateString)
                }
                cursor.close()
            }

            val roundDate = stringDate.mapNotNull { date -> LocalDate.parse(date) }
            currentRoundNumber =
                if (roundDate.isNotEmpty()) {
                    roundDate.indexOfFirst { it > currentDate }
                } else -1

            return currentRoundNumber

        }catch (ex: Exception){
            Commons().printException(ex)
            return 0
        }

    }
}