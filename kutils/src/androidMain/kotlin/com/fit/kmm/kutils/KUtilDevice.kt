package com.fit.kmm.kutils

import android.content.Context
import android.provider.Settings.Secure

actual object KUtilDevice {

    actual fun deviceID(context: Any): String {
        return Secure.getString((context as Context).contentResolver, Secure.ANDROID_ID)
//        return Pandora.getAndroidID(context as Context)
    }

    actual fun versionName(context: Any): String {
        return KApkHelper.getVersionName(context as Context)
    }

    actual fun versionCode(context: Any): Int {
        return KApkHelper.getVersionCode(context as Context)
    }

}