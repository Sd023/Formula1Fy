package com.sdapps.formula1fy.f1.fragments.driver.adapter

import android.content.Context
import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.core.models.DataMembers
import com.sdapps.formula1fy.core.utils.CoroutineTools
import com.sdapps.formula1fy.f1.bo.DriverBO


interface BottomModalInteractor{
    interface  View{
        fun sendHashMapData(map: HashMap<String, Int>, list:ArrayList<Int>)

    }
    interface Presenter{
        fun attachView(view: View, context: Context)
        fun getTheData(driverBO: DriverBO)

        fun onError()
    }
}
class BottomPresenter() : BottomModalInteractor.Presenter{

    private lateinit var view: BottomModalInteractor.View
    private lateinit var raceresults: ArrayList<BottomModal.Race>
    private lateinit var db: DbHandler
    private lateinit var context: Context

    override fun attachView(view: BottomModalInteractor.View, context: Context) {
        this.context = context
        this.view = view
        db = DbHandler(context, DataMembers.DB_NAME)

    }

    override fun getTheData(driverBO: DriverBO){
        CoroutineTools.io {
            try{
                db.openDB()
                raceresults = arrayListOf()
                raceresults.clear()
                val cursor = db.selectSql("select distinct season, round, start_position as gridPos, " +
                        "driver_position as endPos from CurrentSeasonResults where driver_code = '${driverBO.driverCode}'")

                if(cursor != null){

                    while(cursor.moveToNext()){
                        val startpo = cursor.getString(2).toInt()
                        val endpo = cursor.getString(3).toInt()
                        raceresults.add(BottomModal.Race(startpo, endpo))

                    }
                    cursor.close()
                }
                if(raceresults != null){
                    val raceWon = raceresults.count { it.endPos == 1 }
                    val topThreeFinish = raceresults.count{it.endPos <= 3}
                    val totalRace = raceresults.size

                    val worstPosition = raceresults.maxByOrNull { it.endPos }
                    val bestPosition  = raceresults.minByOrNull { it.endPos }

                    val list : ArrayList<Int> = ArrayList()

                    for(i in raceresults){
                        list.add(i.endPos)
                    }

                    val myMap = hashMapOf<String, Int>(
                        "total" to totalRace,
                        "won" to raceWon,
                        "top3" to topThreeFinish,
                        "worst" to worstPosition!!.endPos,
                        "best" to bestPosition!!.endPos
                    )
                    view.sendHashMapData(myMap, list)

                }else {
                    onError()
                }


            }catch (ex: Exception){
                ex.printStackTrace()
            }
            onError()
        }
    }

    override fun onError() {

    }
}