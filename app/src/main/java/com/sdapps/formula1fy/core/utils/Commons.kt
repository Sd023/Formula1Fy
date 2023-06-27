package com.sdapps.formula1fy.core.utils

import android.util.Log

class Commons {
    fun print(data: String?) {
        Log.d(this.javaClass.name, data!!)
    }

    fun printException(data: Throwable?){
        Log.e(this.javaClass.name, data.toString())
    }
}
