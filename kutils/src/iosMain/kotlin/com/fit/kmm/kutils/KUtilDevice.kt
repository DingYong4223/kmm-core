package com.fit.kmm.kutils

import platform.Foundation.NSBundle

actual object KUtilDevice {

    actual fun deviceID(context: Any): String {
        return ""
    }

    actual fun versionName(context: Any): String {
        val info = NSBundle.mainBundle.infoDictionary()
        return "${info?.get("CFBundleShortVersionString")}"
    }

    actual fun versionCode(context: Any): Int {
        return 0
    }

}