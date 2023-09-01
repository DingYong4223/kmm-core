package com.fit.kmm.kloger

import com.fit.kmm.kloger.event.LogEvent
import org.greenrobot.eventbus.EventBus

class KAndroidDebugLoger : IKLoger {

    companion object {
        const val LOGTAG = "lct>> "
        const val STACK_INDEX = 5
        private val logger: IKLoger by lazy { KDebugLoger() }
    }

    override fun v(msg: String) {
        val logInfo = getLogTag() + msg
        logger.v(logInfo)
        EventBus.getDefault().post(LogEvent(logInfo))
    }

    override fun i(msg: String) {
        val logInfo = getLogTag() + msg
        logger.i(logInfo)
        EventBus.getDefault().post(LogEvent(logInfo))
    }

    override fun d(msg: String) {
        val logInfo = getLogTag() + msg
        logger.d(logInfo)
        EventBus.getDefault().post(LogEvent(logInfo))
    }

    override fun e(msg: String) {
        val logInfo = getLogTag() + msg
        logger.e(logInfo)
        EventBus.getDefault().post(LogEvent(logInfo))
    }

    private fun getLogTag(): String {
        val stackTrace = Thread.currentThread().stackTrace
        if (stackTrace.size > STACK_INDEX) {
            val stacks = stackTrace[STACK_INDEX]
            val elFormat = String.format(".(%s:%s)", stacks.fileName, stacks.lineNumber)
            return String.format(
                "$LOGTAG%s[%s]{t%d}",
                elFormat,
                stacks.methodName,
                Thread.currentThread().id
            )
        }
        return "$LOGTAG"
    }

}