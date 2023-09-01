package com.fit.kmm.kutils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import com.tencent.vasdolly.helper.ChannelReaderUtil

/**
 * App启动后引用的一些全局信息，如Application的context等
 * 该类仅在初始化后一次性设置，后续在🧍各个module中都可以引用
 * */
class KApkHelper {

    companion object {

        //构建渠道来源，有时候需要根据上报信息定位问题
        const val BUILD_CHANNEL_PUBLISH = "publish"             //外发构建
        const val BUILD_CHANNEL_EXP = "exp"                     //内部体验版本(仅限公司内员工的版本)
        const val BUILD_CHANNEL_DEV = "dev"                     //默认构建

        @JvmStatic
        fun getChannel(context: Context) : String {
            val channelInfo = ChannelReaderUtil.getChannel(context) ?: ""
            // 传递给前端的渠道号
            val shoreChannel: String? = getApkPackageData(context, "sc")
            return if (shoreChannel.isNullOrEmpty()) channelInfo else shoreChannel
        }

        @JvmStatic
        fun getApkPackageData(context: Context, key: String) : String? {
            // 传递给前端的渠道号
            val channelInfo: String = ChannelReaderUtil.getChannel(context) ?: ""
            val uri: Uri = Uri.parse("?$channelInfo")
            return uri.getQueryParameter(key)
        }

        /**
         * get App versionName
         *
         * @param context
         * @return
         */
        @JvmStatic
        fun getVersionName(context: Context): String {
            val packageManager = context.packageManager
            val packageInfo: PackageInfo
            var versionName: String = ""
            try {
                packageInfo = packageManager.getPackageInfo(context.packageName, 0)
                versionName = packageInfo.versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return versionName
        }


        /**
         * get App versionCode
         *
         * @param context
         * @return
         */
        @JvmStatic
        fun getVersionCode(context: Context): Int {
            val packageManager = context.packageManager
            val packageInfo: PackageInfo
            var versionCode = 0
            try {
                packageInfo = packageManager.getPackageInfo(context.packageName, 0)
                versionCode = packageInfo.versionCode
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return versionCode
        }

    }

}