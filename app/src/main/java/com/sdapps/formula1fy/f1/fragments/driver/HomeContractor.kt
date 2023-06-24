package com.sdapps.formula1fy.f1.fragments.driver

import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.f1.bo.ConstructorBO
import com.sdapps.formula1fy.f1.bo.DriverBO
import com.sdapps.formula1fy.f1.bo.RaceScheduleBO

interface HomeContractor {

    interface View {
        fun showLoading()
        fun hideLoading()
        fun initAll()

        fun setConstructorAdapter(list: ArrayList<ConstructorBO>)
        fun setDriverAdapter(list: ArrayList<DriverBO>)
    }

    interface Presenter {
        suspend fun getDriverData(db: DbHandler)
        suspend fun getConstructorData(db: DbHandler)
        fun attachView(view: View)

        fun detachView()
        suspend fun getNextRound(db: DbHandler): MutableList<RaceScheduleBO>
    }
}