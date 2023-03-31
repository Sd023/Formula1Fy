package com.sdapps.formula1fy.f1.drivers

import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.f1.bo.DriverBO

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
    }
}