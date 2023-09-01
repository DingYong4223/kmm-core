package com.fit.kmm.kutils

/**
 * ~~~~~~
 * Copyright (C), 2015-2022, tencent
 *
 * @FileName: KUrl
 * @Author: delanding
 * @Date: 2022/1/20 22:19
 * @Description UrlEncode and UrlDecode in kotlin.
 * ~~~~~~
 */
class KUrlDecoder {

    companion object {
        private const val base16EncodeTable = "0123456789ABCDEF"
        fun urlEncode(enStr: String): String {
            val data = enStr.encodeToByteArray()
            if (data.isEmpty()) {
                return ""
            }
            val result = StringBuilder()
            for (i in data.indices) {
                when (val intValue = data[i].toInt()) {
                    //man ascii看ASCII编码对应表
                    //字符   十进制
                    //'0' => 48   |   '9' => 57
                    //'a' => 97   |   'z' => 122
                    //'A' => 65   |   'Z' => 90
                    //'-' => 45   |   '_' => 95
                    //'*' => 42   |   '.' => 46
                    //' ' => 32
                    in 48..57, in 65..90, in 97..122 -> result.append(intValue.toChar())
                    45, 95, 46, 42 -> result.append(intValue.toChar())
                    32 -> result.append('+')
                    else -> {
                        //向右移动4bit，获得高4bit
                        val highByte = intValue shr 4 and 0x0F
                        //与0x0F做位与运算，获得低4bit
                        val lowByte = intValue and 0x0F
                        result.append('%')
                        result.append(base16EncodeTable[highByte])
                        result.append(base16EncodeTable[lowByte])
                    }
                }
            }
            return result.toString()
        }

        //把16进制字符转换成10进制表示的数字
        private fun hex2dec(c: Char): Int {
            return when (c) {
                in '0'..'9' -> c - '0'
                in 'a'..'f' -> c - 'a' + 10
                in 'A'..'F' -> c - 'A' + 10
                else -> 0
            }
        }

        fun urlDecode(input: String): String {
            val inputLength = input.length
            if (inputLength == 0) {
                return ""
            }
            val output = ByteArray(inputLength)
            var outputLength = 0
            var i = 0
            while (i < inputLength) {
                when (val c = input[i]) {
                    '%' -> {
                        val x = input[++i]
                        val y = input[++i]
                        //16进制数字转换为10进制数字的过程
                        output[outputLength++] = (hex2dec(x) * 16 + hex2dec(y)).toByte()
                    }
                    '+' -> {
                        output[outputLength++] = ' '.code.toByte()
                    }
                    else -> {
                        output[outputLength++] = c.code.toByte()
                    }
                }
                i++
            }
            return output.copyOfRange(0, outputLength).decodeToString()
        }
    }

}