package com.sdapps.formula1fy.home

import com.sdapps.formula1fy.ModelBO.ConstructorBO
import com.sdapps.formula1fy.ModelBO.DriverBO
import com.sdapps.formula1fy.core.DbHandler

interface HomeScreenInteractor {
    interface View{
        fun loadScreen()
        fun showLoading()
        fun hideLoading()

    }

    interface Presenter{
        fun getDriverData(db:DbHandler): ArrayList<DriverBO>
        fun getConstructorData(db: DbHandler) : ArrayList<ConstructorBO>?

        fun setupView(view : View)

    }
}