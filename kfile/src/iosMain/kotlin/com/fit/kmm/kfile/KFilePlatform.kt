package com.fit.kmm.kfile

import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

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
actual class KFilePlatform {

    actual companion object{
        /**
         * 返回平台document路径，分别对应如下路径：
         * Android：应用File路径
         * IOS：应用的document路径
         * @param context 上下文环境
         * */
        actual fun docPath(context: Any): String {
            val paths = NSSearchPathForDirectoriesInDomains(
                NSDocumentDirectory,
                NSUserDomainMask,
                true
            )
            return (paths.firstOrNull() ?: "") as String
        }

        /**
         * 返回平台Cache路径，分别对应如下路径：
         * Android：应用Cache路径
         * IOS：应用的cache路径
         * @param context 上下文环境
         * */
        actual fun cachePath(context: Any): String {
            val paths = NSSearchPathForDirectoriesInDomains(
                NSCachesDirectory,
                NSUserDomainMask,
                true
            )
            return (paths.firstOrNull() ?: "") as String
        }
    }

}