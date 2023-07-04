package com.sdapps.formula1fy.f1.fragments.home

import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.f1.bo.ConstructorBO
import com.sdapps.formula1fy.f1.bo.DriverBO
import com.sdapps.formula1fy.f1.bo.LatestResult
import com.sdapps.formula1fy.f1.bo.RaceScheduleBO

interface HomeContractor {

    interface View {
        fun showLoading()
        fun hideLoading()
        fun initAll()

        fun setConstructorAdapter(list: ArrayList<ConstructorBO>, map : HashMap<String, ArrayList<String>>)
        fun setDriverAdapter(list: ArrayList<DriverBO>)

        fun setNextRaceAdapter(list : MutableList<RaceScheduleBO>)
        fun setLatestResults(list: MutableList<LatestResult>)
    }

    interface Presenter {
        suspend fun getDriverData(db: DbHandler)
        suspend fun getConstructorData(db: DbHandler)
        fun attachView(view: View)

        fun detachView()
        suspend fun getNextRound(db: DbHandler)
        suspend fun getLatestRound(db: DbHandler)
    }
}