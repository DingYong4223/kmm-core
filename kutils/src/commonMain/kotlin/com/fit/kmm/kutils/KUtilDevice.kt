package com.fit.kmm.kutils

expect object KUtilDevice {
    fun deviceID(context: Any): String
    fun versionName(context: Any): String
    fun versionCode(context: Any): Int
}