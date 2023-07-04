package com.sdapps.formula1fy.f1.fragments.driver

import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.f1.bo.DriverBO
import java.sql.Driver

interface DriverInteractor {
    interface View{
        fun setUpDriverData(list: ArrayList<DriverBO>)
        fun onCardClick(driverBO : DriverBO)
        fun showToast(driverBo: DriverBO)
    }

    interface Presenter{
        fun attachView(view : DriverInteractor.View)
        suspend fun getDriverData(db: DbHandler)

        suspend fun handleCardClick(driverBO: DriverBO)
    }
}