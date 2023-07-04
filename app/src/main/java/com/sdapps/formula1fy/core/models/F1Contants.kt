package com.sdapps.formula1fy.core.models

import android.icu.number.IntegerWidth
import com.sdapps.formula1fy.R

object F1Contants {
    const val RED_BULL = "red_bull"
    const val MERCEDES = "mercedes"
    const val FERRARI = "ferrari"
    const val ALPINE = "alpine"
    const val MCLAREN = "mclaren"
    const val ALFA_ROMEO = "alfa"
    const val HAAS = "haas"
    const val ALPHATAURI = "alphatauri"
    const val ASTON_MARTIN = "aston_martin"
    const val WILLIAMS = "williams"

    var teamColorMap : HashMap<String, Int> = HashMap()
    init {
        teamColorMap.put(RED_BULL, R.color.redbull_racing)
        teamColorMap.put(MERCEDES, R.color.mercedes)
        teamColorMap.put(FERRARI, R.color.ferrari_red)
        teamColorMap.put(ALPINE, R.color.alpine)
        teamColorMap.put(MCLAREN, R.color.mclaren)
        teamColorMap.put(ALFA_ROMEO, R.color.alfa_romeo_racing)
        teamColorMap.put(HAAS, R.color.haas)
        teamColorMap.put(ALPHATAURI, R.color.alphatauri)
        teamColorMap.put(ASTON_MARTIN, R.color.aston_martin)
        teamColorMap.put(WILLIAMS, R.color.williams)
    }

    /*DRIVERS*/
    const val ALBON = "albon"
    const val LATIFI = "latifi"
    const val RICCIARDO = "ricciardo"
    const val LECLERC = "leclerc"
    const val SAINZ = "sainz"
    const val HAMILTON = "hamilton"
    const val RUSSELL = "russell"
    const val ALONSO = "alonso"
    const val OCON = "ocon"
    const val ZHOU = "zhou"
    const val BOTTAS = "bottas"
    const val KMAG = "kevin_magnussen"
    const val SCHUMACHER = "mick_schumacher"
    const val NORRIS = "norris"
    const val GASLY = "gasly"
    const val TSUNODA = "tsunoda"
    const val VETTEL = "vettel"
    const val VERSTAPPEN = "max_verstappen"
    const val PEREZ = "perez"
    const val STROLL = "stroll"
    const val NICK_DE_VRIES = "de_vries"
    const val HULKENBERG = "hulkenberg"
    const val sargeant = "sargeant"
    const val piastri = "piastri"
}