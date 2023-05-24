package com.sdapps.formula1fy.f1.landing

import android.content.Context
import android.text.SpannableStringBuilder
import com.sdapps.formula1fy.f1.bo.ConstructorBO
import com.sdapps.formula1fy.f1.bo.DriverBO

interface LandingContractor {

    interface View{
        fun showLoading()
        fun hideLoading()
        fun moveToNextScreen()
        fun initAll()
    }
    interface Presenter{
        fun attachView(view: LandingContractor.View, context: Context)
        fun detachView()
        fun getAppString() : SpannableStringBuilder
        fun fetchAllData()
        fun insertDriverData(list: ArrayList<DriverBO>)
        fun insertConstructorData(list : ArrayList<ConstructorBO>)
    }
}