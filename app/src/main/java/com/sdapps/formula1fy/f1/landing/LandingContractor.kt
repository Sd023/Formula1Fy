package com.sdapps.formula1fy.f1.landing

import android.content.Context
import android.text.SpannableStringBuilder
import com.sdapps.formula1fy.f1.bo.ConstructorBO
import com.sdapps.formula1fy.f1.bo.DriverBO
import com.sdapps.formula1fy.f1.bo.Results

interface LandingContractor {

    interface View {
        fun showLoading()
        fun hideLoading()
        fun moveToNextScreen()
        fun initAll()
    }

    interface Presenter {
        fun attachView(view: LandingContractor.View, context: Context)
        fun detachView()
        fun getAppString(): SpannableStringBuilder
        suspend fun fetchDriverData()
        fun insertDriverData(list: ArrayList<DriverBO>)
        fun insertConstructorData(list: ArrayList<ConstructorBO>)

        suspend fun fetchRaceData()
        suspend fun fetchConstructorData()

        suspend fun fetchLatestResults()
        fun insertLatestResultsIntoDB(list: MutableList<Results>)
    }
}