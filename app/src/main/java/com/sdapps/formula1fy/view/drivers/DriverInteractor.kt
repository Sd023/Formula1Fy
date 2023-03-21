package com.sdapps.formula1fy.view.drivers

import com.sdapps.formula1fy.core.DbHandler

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