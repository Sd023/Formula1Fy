package com.sdapps.formula1fy.f1.landing

import android.content.Context
import com.sdapps.formula1fy.core.utils.NetworkTools
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LandingManager(val presenter: LandingPresenter) {

    private lateinit var view : LandingContractor.View

    suspend fun getAllNecessaryData(){
        CoroutineScope(Dispatchers.Main).launch {
                val fetchDriverData = async(Dispatchers.IO) {
                    presenter.fetchDriverData()
                }
                fetchDriverData.await()
                val fetchConstructorData = async(Dispatchers.IO) {
                    presenter.fetchConstructorData()
                }
                fetchConstructorData.await()

                val latestRaceJson = async(Dispatchers.IO){
                    presenter.fetchLatestResults()
                }
                latestRaceJson.await()

                val fetchRaceData = async(Dispatchers.IO) {
                    presenter.fetchRaceData()
                }
                fetchRaceData.await()
            }
        }
}