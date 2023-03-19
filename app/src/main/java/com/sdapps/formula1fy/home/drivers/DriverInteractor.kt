package com.sdapps.formula1fy.home.drivers

import com.sdapps.formula1fy.ModelBO.DriverBO
import com.sdapps.formula1fy.core.DbHandler

interface DriverInteractor {

    interface View{
        fun showLoading()
        fun hideLoading()

        fun initAll()

    }

    interface Presenter{
        fun getDriverData(db: DbHandler): ArrayList<DriverBO>
        fun attachView(view: View)

        fun detachView()
    }
}