package com.fit.kmm.kutils

import kotlinx.datetime.Clock

/**
 * 封装一些双端使用的工具类
 * */
actual class KUtil {

    actual companion object {

        /**
         * 系统当前时间戳，毫秒形式
         * */
        actual fun currentTime(): Long {
            //Clock类不适用与Android7.0以下系统（java.lang.ClassNotFoundException: Didn't find class "java.time.Instant"），暂时仅用于IOS端
            return Clock.System.now().toEpochMilliseconds()
        }
    }

}