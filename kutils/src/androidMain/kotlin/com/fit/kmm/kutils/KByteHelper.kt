package com.fit.kmm.kutils


class KByteHelper {

    /**
     * 将字符串转换成二进制流
     * */
    fun hex2Bytes(str: String): ByteArray? {
        if (str.length % 2 != 0) {
            return null
        }
        val ret = ByteArray(str.length / 2)
        var i = 0
        while (i < str.length) {
            ret[i / 2] = ((Character.digit(str[i], 16) shl 4)
                    + Character.digit(str[i + 1], 16)).toByte()
            i += 2
        }
        return ret
    }

    fun bytes2Hex(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (b in bytes) {
            sb.append(String.format("%02x", b))
        }
        return sb.toString()
    }

}