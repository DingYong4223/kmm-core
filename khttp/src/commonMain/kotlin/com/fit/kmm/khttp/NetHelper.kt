package com.fit.kmm.khttp

import kotlin.jvm.JvmStatic


/**
 * ~~~~~~
 * Copyright (C), 2015-2022, tencent
 *
 * @FileName: NetHelper
 * @Author: delanding
 * @Date: 2023/8/8 10:54
 * @Description Life with Passion, Code with Creativity.
 * ~~~~~~
 */
class NetHelper : INetHelper by NetPlatImpl.platNet() {

    companion object {

        const val NET_ERROR_TIP = KtorHelper.NET_ERROR_TIP

        @JvmStatic
        fun with(): NetHelper {
            return NetHelper()
        }

    }

}