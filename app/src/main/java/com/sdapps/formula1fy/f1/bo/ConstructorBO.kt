package com.sdapps.formula1fy.f1.bo

class ConstructorBO {
    var position: String? = null
    var points: String? = null
    var wins: String? = null
    var consId: String? = null
    var name: String? = null
    var nationality: String? = null
    var season : String? = null
    var round: String? = null
}

data class ConstructorNewBO(val teamName: String, val driversOfTeam : ArrayList<String>)