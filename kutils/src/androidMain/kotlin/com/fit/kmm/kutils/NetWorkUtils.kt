package com.fit.kmm.kutils

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.telephony.TelephonyManager
import android.util.Log
import com.fit.kmm.kloger.KLoger
import java.io.IOException
import java.net.HttpURLConnection
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.URL

/**
 * Created by siweizhou on 2015/12/25.
 * just do it...
 */
object NetWorkUtils {

    const val TAG = "NetWorkUtils"
    private val PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn")
    var APN_TYPE_CTNET = "ctnet"
    var APN_TYPE_CTWAP = "ctwap"
    var APN_TYPE_CMNET = "cmnet"
    var APN_TYPE_CMWAP = "cmwap"
    var APN_TYPE_UNINET = "uninet"
    var APN_TYPE_UNIWAP = "uniwap"
    var APN_TYPE_3GNET = "3gnet"
    var APN_TYPE_3GWAP = "3gwap"
    fun getApnType(context: Context): String {
        var apntype = "nomatch"
        var c: Cursor? = null
        try {
            c = context.contentResolver.query(
                PREFERRED_APN_URI,
                null, null, null, null
            )
            if (c == null) return apntype
            c.moveToFirst()
            @SuppressLint("Range") val user = c.getString(c.getColumnIndex("user")).toLowerCase()
            if (user.startsWith(APN_TYPE_CTNET)) {
                apntype = APN_TYPE_CTNET
            } else if (user.startsWith(APN_TYPE_CTWAP)) {
                apntype = APN_TYPE_CTWAP
            } else if (user.startsWith(APN_TYPE_CMNET)) {
                apntype = APN_TYPE_CMNET
            } else if (user.startsWith(APN_TYPE_CMWAP)) {
                apntype = APN_TYPE_CMWAP
            } else if (user.startsWith(APN_TYPE_UNINET)) {
                apntype = APN_TYPE_UNINET
            } else if (user.startsWith(APN_TYPE_UNIWAP)) {
                apntype = APN_TYPE_UNIWAP
            }
        } catch (e: Exception) {
            //e.printStackTrace();
        } finally {
            c?.close()
        }
        return apntype
    }

    fun getApnType(string: String?): String {
        var apntype = "nomatch"
        if (string == null) return apntype
        try {
            if (string.startsWith(APN_TYPE_CTNET)) {
                apntype = APN_TYPE_CTNET
            } else if (string.startsWith(APN_TYPE_CTWAP)) {
                apntype = APN_TYPE_CTWAP
            } else if (string.startsWith(APN_TYPE_CMNET)) {
                apntype = APN_TYPE_CMNET
            } else if (string.startsWith(APN_TYPE_CMWAP)) {
                apntype = APN_TYPE_CMWAP
            } else if (string.startsWith(APN_TYPE_UNINET)) {
                apntype = APN_TYPE_UNINET
            } else if (string.startsWith(APN_TYPE_UNIWAP)) {
                apntype = APN_TYPE_UNIWAP
            } else if (string.startsWith(APN_TYPE_3GNET)) {
                apntype = APN_TYPE_3GNET
            } else if (string.startsWith(APN_TYPE_3GWAP)) {
                apntype = APN_TYPE_3GWAP
            }
        } catch (e: Exception) {
            //e.printStackTrace();
        }
        return apntype
    }

    @Throws(IOException::class)
    fun getConnectionWithDefaultProxy(
        url: String?,
        defaultHost: String?, defaultPort: Int
    ): HttpURLConnection {
        val conn: HttpURLConnection
        val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress(defaultHost, defaultPort))
        val proxyURL = URL(url)
        conn = proxyURL.openConnection(proxy) as HttpURLConnection
        return conn
    }

    @Throws(IOException::class)
    fun getConnectionWithXOnlineHost(
        url: String,
        defaultHost: String, defaultPort: Int
    ): HttpURLConnection {
        val conn: HttpURLConnection
        val hostUrl: URL
        var host: String? = null
        var path: String? = null
        val hostIndex = "http://".length
        val pathIndex = url.indexOf('/', hostIndex)
        if (pathIndex < 0) {
            host = url.substring(hostIndex)
            path = ""
        } else {
            host = url.substring(hostIndex, pathIndex)
            path = url.substring(pathIndex)
        }
        hostUrl =
            if (defaultPort != 80) URL("http://$defaultHost:$defaultPort$path") else URL(
                "http://$defaultHost$path"
            )
        conn = hostUrl.openConnection() as HttpURLConnection
        conn.setRequestProperty("X-Online-Host", host)
        return conn
    }

    @JvmStatic
    fun getExrea(context: Context): String? {
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = cm.activeNetworkInfo
            info!!.extraInfo
        } catch (e: Exception) {
            null
        }
    }

    @JvmStatic
    fun isMobileNetWork(context: Context): Boolean {
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = cm.activeNetworkInfo
            isMobileNetworkInfo(info)
        } catch (e: Exception) {
            false
        }
    }

    /**判断此网络是否为mobile
     * 注：适应三星新定义的双卡双待mobile类型
     * @param netInfo
     * @return
     */
    fun isMobileNetworkInfo(netInfo: NetworkInfo?): Boolean {
        return if (null == netInfo) {
            false
        } else ConnectivityManager.TYPE_MOBILE == netInfo.type
                || ConnectivityManager.TYPE_MOBILE + 50 == netInfo.type
    }

    /**
     * 判断当前网络连接是否为wifi
     * @param context
     * @return
     */
    fun isWifiEnabled(context: Context): Boolean {
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = cm.activeNetworkInfo
            val typeName = info!!.typeName.toLowerCase() // WIFI/MOBILE
            typeName == "wifi"
        } catch (e: Exception) {
            false
        }
    }

    fun isNetSupport(context: Context?): Boolean {
        if (context == null) {
            return false
        }
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            ?: return false

        //  return cm.getActiveNetworkInfo().isAvailable();
        try {
            val info = cm.allNetworkInfo
            if (info != null) {
                for (i in info.indices) {
                    if (info[i].state == NetworkInfo.State.CONNECTED) return true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun getNetworkType(context: Context): Int {
        var type = -1
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (cm != null) {
            val networkInfo = cm.activeNetworkInfo
            if (networkInfo != null) {
                type = networkInfo.type
            }
        }
        return type
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connMgr = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connMgr.activeNetworkInfo
        return info != null && info.isAvailable
    }

    fun isWifiConnected(context: Context?): Boolean {
        if (context == null) {
            return false
        }
        try {
            val connMgr = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connMgr.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI
        } catch (e: Exception) {
        }
        return false
    }

    fun getActiveNetworkInfo(context: Context): NetworkInfo? {
        val connMgr = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connMgr.activeNetworkInfo
    }

    fun getApn(context: Context): String? {
        val conManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (conManager != null) {
            val ni = conManager.activeNetworkInfo
            if (ni != null && ni.type == ConnectivityManager.TYPE_MOBILE) {
                ni.extraInfo
            } else {
                null
            }
        } else {
            null
        }
    }

    /**
     * 网络字序转ip
     * @param int_addr
     * @return
     */
    fun IntAddr2Ip(int_addr: Int): String {
        val sb = StringBuffer()
        sb.append(int_addr and 0xFF)
            .append(".")
            .append(int_addr shr 8 and 0xFF)
            .append(".")
            .append(int_addr shr 16 and 0xFF)
            .append(".")
            .append(int_addr shr 24 and 0xFF)
        return sb.toString()
    }

    /**
     * 获取当前的网络类型(WIFI,2G,3G,4G)
     *
     * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
     *
     * @param context 上下文
     * @return 网络类型
     *
     *  * [.NETWORK_WIFI] = 1;
     *  * [.NETWORK_4G] = 4;
     *  * [.NETWORK_3G] = 3;
     *  * [.NETWORK_2G] = 2;
     *  * [.NETWORK_UNKNOWN] = 5;
     *
     */
    fun getNetWorkType(context: Context): Int {
        var netType = NETWORK_UNKNOWN
        val info = getActiveNetworkInfo(context)
        if (info != null && info.isAvailable) {
            netType = if (info.type == ConnectivityManager.TYPE_WIFI) {
                NETWORK_WIFI
            } else if (info.type == ConnectivityManager.TYPE_MOBILE) {
                when (info.subtype) {
                    NETWORK_TYPE_GSM, TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> NETWORK_2G
                    NETWORK_TYPE_TD_SCDMA, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> NETWORK_3G
                    NETWORK_TYPE_IWLAN, TelephonyManager.NETWORK_TYPE_LTE -> NETWORK_4G
                    TelephonyManager.NETWORK_TYPE_NR -> NETWORK_5G
                    else -> {
                        val subtypeName = info.subtypeName
                        if (subtypeName.equals("TD-SCDMA", ignoreCase = true)
                            || subtypeName.equals("WCDMA", ignoreCase = true)
                            || subtypeName.equals("CDMA2000", ignoreCase = true)
                        ) {
                            NETWORK_3G
                        } else {
                            NETWORK_UNKNOWN
                        }
                    }
                }
            } else {
                NETWORK_UNKNOWN
            }
        }
        return netType
    }

    /**
     * 获取当前的网络类型(WIFI,2G,3G,4G)
     *
     * 依赖上面的方法
     *
     * @param context 上下文
     * @return 网络类型名称
     *
     *  * NETWORK_WIFI
     *  * NETWORK_4G
     *  * NETWORK_3G
     *  * NETWORK_2G
     *  * NETWORK_UNKNOWN
     *  * NETWORK_NO
     *
     */
    fun getNetWorkTypeName(context: Context): String {
        try {
            return when (getNetWorkType(context)) {
                NETWORK_WIFI -> "wifi"
                NETWORK_4G -> "4G"
                NETWORK_3G -> "3G"
                NETWORK_2G -> "2G"
                NETWORK_UNKNOWN -> "NETWORK_UNKNOWN"
                else -> "unknow"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("getNetWorkTypeName", "e = " + e.message)
        }
        return "unknow"
    }

    const val NETWORK_WIFI = 1 // wifi network
    const val NETWORK_2G = 2 // "2G" networks
    const val NETWORK_3G = 3 // "3G" networks
    const val NETWORK_4G = 4 // "4G" networks
    const val NETWORK_5G = 5 // "5G" networks
    const val NETWORK_6G = 6 // "6G" networks
    const val NETWORK_UNKNOWN = 100 // unknown network
    private const val NETWORK_TYPE_GSM = 16
    private const val NETWORK_TYPE_TD_SCDMA = 17
    private const val NETWORK_TYPE_IWLAN = 18

    fun getNetWorkOperatorName(context: Context): String {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val networkOperator = tm.networkOperator
        if (tm.phoneType == TelephonyManager.PHONE_TYPE_CDMA) {
            Log.i(TAG, "CDMA，not valid")
            return "unknown"
        }
        //phoneType map
        //0 -> NO_PHONE
        //1 -> GSM_PHONE
        //2 -> CDMA_PHONE
        //3 -> SIP_PHONE
        //4 -> THIRD_PARTY_PHONE
        //5 -> IMS_PHONE
        //6 -> CDMA_LTE_PHONE
        KLoger.i("tm.phoneType: ${tm.phoneType}, name: ${tm.networkOperatorName}")
        return if ("46001" == networkOperator || "46006" == networkOperator || "46009" == networkOperator) {
                "中国联通"
            } else if ("46000" == networkOperator || "46002" == networkOperator || "46004" == networkOperator || "46007" == networkOperator) {
                "中国移动"
            } else if ("46003" == networkOperator || "46005" == networkOperator || "46011" == networkOperator) {
                "中国电信"
            } else {
                "unknown"
            }
    }

}