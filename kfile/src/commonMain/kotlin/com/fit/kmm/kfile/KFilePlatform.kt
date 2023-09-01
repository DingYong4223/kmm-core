package com.fit.kmm.kfile


/**
 * ~~~~~~
 * Copyright (C), 2015-2022, tencent
 *
 * @FileName: KFilePlatform
 * @Author: delanding
 * @Date: 2023/7/7 16:09
 * @Description Life with Passion, Code with Creativity.
 * ~~~~~~
 */
expect class KFilePlatform {

    companion object {
        /**
         * 返回平台document路径，分别对应如下路径：
         * Android：应用File路径
         * IOS：应用的document路径
         * @param context 上下文环境
         * */
        fun docPath(context: Any): String

        /**
         * 返回平台Cache路径，分别对应如下路径：
         * Android：应用Cache路径
         * IOS：应用的cache路径
         * @param context 上下文环境
         * */
        fun cachePath(context: Any): String
    }

}