package com.sdapps.formula1fy.core.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object CoroutineTools {

    fun main(work: suspend (() -> Unit)) = CoroutineScope(Dispatchers.Main).launch {
        work()
    }

    fun io(work: suspend (() -> Unit)) = CoroutineScope(Dispatchers.IO).launch {
        work()
    }
}