package com.fit.kmm.kutils

import java.util.concurrent.ConcurrentHashMap

/**
 * 方法调用帮助类
 * 部分方法会被短时间内连续调用，此类用于防抖处理
 * */
class MethodHelper {

    companion object {

        //保存箭值对应的时间戳
        private val map: MutableMap<String, Long> = ConcurrentHashMap()

        /**
         * 判断在span时间段内是否可以执行
         * @param key 业务函数对应的箭
         * @param span 时间段，ms类型
         * @return 当上一次执行时间与当前时间小于span时返回false，true otherwise.
         * */
        @JvmStatic
        fun isInvokeInSpan(key: String, span: Long): Boolean {
            val timeOfKey: Long = map[key] ?: 0
            val ret = System.currentTimeMillis() - timeOfKey > span
            map[key] = System.currentTimeMillis()
            return ret
        }

    }

}