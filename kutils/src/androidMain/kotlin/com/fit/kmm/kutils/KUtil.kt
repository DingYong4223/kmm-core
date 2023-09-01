package com.fit.kmm.kutils

/**
 * 封装一些双端使用的工具类
 * */
actual class KUtil {

    actual companion object {

        /**
         * 系统当前时间戳，毫秒形式
         * */
        actual fun currentTime(): Long {
            return System.currentTimeMillis()
        }
    }

}