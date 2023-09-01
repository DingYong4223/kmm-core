package com.fit.kmm.kutils

import android.os.Build


/**
 * ~~~~~~
 * Copyright (C), 2015-2022, tencent
 *
 * @FileName: KDeviceUtils
 * @Author: delanding
 * @Date: 2022/10/14 14:11
 * @Description Life with Passion, Code with Creativity.
 * ~~~~~~
 */
class KDeviceUtils {

    companion object {

        @JvmStatic
        fun getModel() = Build.MODEL //getModel(KApp.context as Context)

        @JvmStatic
        fun getBrand() = Build.BRAND

    }

}