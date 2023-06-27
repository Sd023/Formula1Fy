package com.sdapps.formula1fy.core.utils

class StringHelper {
    fun getQueryFormat(data: String?): String? {
        return if (data != null) {
            "'$data'"
        } else null
    }

    fun removeWhiteSpaces(data: String?): String? {
        return data?.replace("\\s".toRegex(), "")
    }

    fun convertValueToString(data: Int?): String? {
        return data?.toString()
    }
}
