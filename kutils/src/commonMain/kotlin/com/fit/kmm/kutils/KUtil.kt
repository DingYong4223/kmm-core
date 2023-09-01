package com.fit.kmm.kutils

/**
 * 封装一些双端使用的工具类
 * */
expect class KUtil {

    companion object {

        /**
         * 系统当前时间戳，毫秒形式
         * */
        fun currentTime(): Long
    }

}