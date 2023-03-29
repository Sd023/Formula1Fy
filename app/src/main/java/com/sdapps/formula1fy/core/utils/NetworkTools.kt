package com.sdapps.formula1fy.core.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities


class NetworkTools() {

    private  lateinit var  connectivityManager : ConnectivityManager

    suspend fun isNetworkAndInternetAvailable(context : Context) : Boolean{
        connectivityManager = context.getSystemService(ConnectivityManager::class.java)
        val activeNetwork = connectivityManager.activeNetwork
        return if(activeNetwork == null){
            false
        }else{
            val cap = connectivityManager.getNetworkCapabilities(activeNetwork)
            cap?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
        }
    }


}