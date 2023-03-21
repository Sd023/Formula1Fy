package com.sdapps.formula1fy.view.login

import com.sdapps.formula1fy.view.constructors.ConstructorBO
import com.sdapps.formula1fy.view.drivers.DriverBO

interface LoginContractor {
    interface View {
        fun showAlert()
        fun onError()

        fun showLoading()
        fun hideLoading()

        fun moveToNextScreen(userBO: UserBO)

        fun checkCurrentUser()
    }

    interface Presenter {
        fun fetchDriverData(userBO: UserBO)
        fun insertDriverDatasToDB(list: ArrayList<DriverBO>)
        fun insertConstructorDataTODB(list: ArrayList<ConstructorBO>, userBO: UserBO)
        fun fetchCircuitData()

        fun fetchConstructorData(userBO: UserBO)

        fun performLogin(email: String, password: String)

        fun onError(msg: String?);

        fun attachView(view: View)

        fun detachView()
    }

}