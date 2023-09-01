package com.fit.kmm.kloger

import android.app.ActivityManager
import android.content.Context
import android.os.Process

actual fun initLoger(context: Any, debug: Boolean) {
    if (debug) {
        KLoger.logger = KAndroidDebugLoger()
    } else {
        /*System.loadLibrary("c++_shared")
        System.loadLibrary("marsxlog")
        //release包仅内部体验版本日志输出
        val fileDir = (context as Context).filesDir
        val cachePath = File(fileDir, "xlog_cache").absolutePath
        val logPath = File(fileDir, "xlog").absolutePath

        Log.setLogImp(Xlog())
        Log.setConsoleLogOpen(false)
        Log.appenderOpen(Xlog.LEVEL_INFO, Xlog.AppednerModeAsync, cachePath, logPath,
            "lctlog:" + getProcessName(context), 0)
        KLoger.logger = KAndroidReleaseLoger()*/
    }
}

fun getProcessName(cxt: Context): String {
    val pid = Process.myPid()
    val am = cxt.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val runningApps = am.runningAppProcesses ?: return ""
    for (procInfo in runningApps) {
        if (procInfo.pid == pid) {
            return procInfo.processName
        }
    }
    return ""
}