package com.fit.kmm.helper

import com.fit.kmm.mmkv.KMMKV

/**
 * 操作KMMKV的帮助类
 * @author delanding
 * */
class KVHelper {

    companion object {

        /**
         * 一级缓存，用于存放全局信息
         * */
        fun with() = KMMKV(null)

        /**
         * 二级缓存，用于存储用户信息等
         * @param id 要存储的文件名，eg. 用户ID etc。
         * */
        fun withId(id: String?) = KMMKV(id)

    }

}