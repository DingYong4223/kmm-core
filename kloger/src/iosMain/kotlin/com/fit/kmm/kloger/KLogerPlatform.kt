package com.fit.kmm.kloger

actual fun initLoger(context: Any, debug: Boolean) {
    if (debug) {
        KLoger.logger = KDebugLoger()
    } else {
        KLoger.logger = KIOSReleaseLoger()
    }
}