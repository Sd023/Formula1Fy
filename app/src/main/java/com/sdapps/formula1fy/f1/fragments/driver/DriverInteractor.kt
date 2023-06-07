package com.sdapps.formula1fy.f1.fragments.driver

import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.f1.bo.DriverBO
import com.sdapps.formula1fy.f1.bo.RaceScheduleBO

interface DriverInteractor {

    interface View {
        fun showLoading()
        fun hideLoading()
        fun initAll()

    }

    interface Presenter {
        fun getDriverData(db: DbHandler): ArrayList<DriverBO>
        fun attachView(view: View)

        fun detachView()
        fun getNextRound(db: DbHandler): MutableList<RaceScheduleBO>
    }
}