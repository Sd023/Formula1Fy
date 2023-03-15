package com.sdapps.formula1fy.login

import com.sdapps.formula1fy.ModelBO.ConstructorBO
import com.sdapps.formula1fy.ModelBO.DriverBO

interface LoginContractor {
    interface View {
        fun showAlert()
        fun onError()

        fun showLoading()
        fun hideLoading()
    }

    interface Presenter {
        fun fetchDriverData()
        fun insertDriverDatasToDB(list: ArrayList<DriverBO>)
        fun insertConstructorDataTODB(list: ArrayList<ConstructorBO>)
        fun fetchCircuitData()

        fun fetchConstructorData()

        fun performLogin(email: String, password: String)

        fun onError(msg: String?);

        fun attachView(view: LoginContractor.View)

        fun detachView()
    }

}