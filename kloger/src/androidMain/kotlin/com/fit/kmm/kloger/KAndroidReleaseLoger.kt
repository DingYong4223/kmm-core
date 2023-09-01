package com.fit.kmm.kloger

import com.tencent.mars.xlog.Log

class KAndroidReleaseLoger : IKLoger {

    override fun v(msg: String) = Log.v(getLogTag(), msg)

    override fun i(msg: String) = Log.i(getLogTag(), msg)

    override fun d(msg: String) = Log.d(getLogTag(), msg)

    override fun e(msg: String) = Log.e(getLogTag(), msg)

    private fun getLogTag(): String {
        return "AKLogger"
    }

}