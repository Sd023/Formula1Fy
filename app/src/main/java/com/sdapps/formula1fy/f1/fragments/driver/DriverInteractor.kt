package com.sdapps.formula1fy.f1.fragments.driver

import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.f1.bo.DriverBO

interface DriverInteractor {
    interface View{
        fun setUpDriverData(list: ArrayList<DriverBO>)
    }

    interface Presenter{
        fun attachView(view : DriverInteractor.View)
        suspend fun getDriverData(db: DbHandler)
    }
}