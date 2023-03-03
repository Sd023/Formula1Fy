package com.sdapps.formula1fy.login

interface LoginContractor {
    interface View{
        fun showAlert()
        fun onError()
    }

    interface Presenter{
       fun fetchDriverData()
       fun fetchConstructorData(list:ArrayList<DriverBO>)
       fun fetchCircuitData()
    }

}