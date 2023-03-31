package com.sdapps.formula1fy.f1.home

import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.f1.bo.ConstructorBO
import com.sdapps.formula1fy.f1.bo.DriverBO

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
        fun isCheckDataAvailable(flag : Boolean, db: DbHandler): Boolean


    }
}