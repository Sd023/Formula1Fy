package com.sdapps.formula1fy.login

import com.sdapps.formula1fy.ModelBO.ConstructorBO
import com.sdapps.formula1fy.ModelBO.DriverBO

interface LoginContractor {
    interface View{
        fun showAlert()
        fun onError()
    }

    interface Presenter{
       fun fetchDriverData()
       fun insertDriverDatasToDB(list:ArrayList<DriverBO>)
       fun insertConstructorDataTODB(list: ArrayList<ConstructorBO>)
       fun fetchCircuitData()

       fun fetchConstructorData()

    }

}