package com.fit.kmm.kutils

class KWebHelper {

    companion object {

        /**
         * 生成Http头格式的cookie字符串（带格式的）
         * */
        fun getWebCookie(cookies: Map<String, Any>): String {
            val sb = StringBuilder()
            val keys = cookies.keys.iterator()
            while (keys.hasNext()) {
                val key = keys.next()
                sb.append("$key=${cookies[key]}; ") //需要加一个空格，不然后台会校验失败
            }
            return sb.toString()
        }

    }

}