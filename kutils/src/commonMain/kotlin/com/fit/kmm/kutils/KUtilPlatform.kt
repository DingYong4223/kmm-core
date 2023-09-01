package com.fit.kmm.kutils

import kotlin.jvm.JvmStatic

object KUtilPlatform {
    @JvmStatic
    fun deviceID(context: Any) = KUtilDevice.deviceID(context)

    @JvmStatic
    fun versionName(context: Any) = KUtilDevice.versionName(context)

    @JvmStatic
    fun versionCode(context: Any) = KUtilDevice.versionCode(context)
}