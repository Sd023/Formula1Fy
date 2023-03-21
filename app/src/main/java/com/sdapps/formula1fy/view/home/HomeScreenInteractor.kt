package com.sdapps.formula1fy.view.home

import com.sdapps.formula1fy.core.DbHandler
import com.sdapps.formula1fy.view.constructors.ConstructorBO
import com.sdapps.formula1fy.view.drivers.DriverBO

interface HomeScreenInteractor {
    interface View {
        fun loadScreen()
        fun getMessageFromDead()

        fun moveToNextScreen(isDriver: Boolean)

        fun showLoading()
        fun hideLoading()
        fun onError()


    }

    interface Presenter {
        fun setupView(view: View)
        fun fetchDriverData()
        fun insertDriverDatasToDB(list: ArrayList<DriverBO>)
        fun insertConstructorDataTODB(list: ArrayList<ConstructorBO>)

        fun fetchConstructorData()
        fun isCheckDataAvailable(db: DbHandler): Boolean


    }
}