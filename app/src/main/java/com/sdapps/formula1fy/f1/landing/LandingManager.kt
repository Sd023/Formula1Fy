package com.sdapps.formula1fy.f1.landing

import android.content.Context
import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.core.utils.NetworkTools
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LandingManager(val presenter: LandingPresenter, val dbHandler: DbHandler) {

    private lateinit var view : LandingContractor.View

    fun attachManagerview(view: LandingContractor.View){
        this.view = view
    }

    suspend fun getAllNecessaryData(context: Context){
        CoroutineScope(Dispatchers.IO).launch {
            if(NetworkTools().isNetworkAndInternetAvailable(context)){
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

                val fetchAllSeasonData = async(Dispatchers.IO){
                    presenter.fetchAllCurrentSeasonResult(dbHandler)
                }
                fetchAllSeasonData.await()

            }else{
                presenter.checkIfDataIsAvailable(dbHandler)
            }
        }
    }
}