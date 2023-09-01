package com.fit.kmm.kutils

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class KUtilsTest {

    @BeforeTest
    fun setUp() {
    }

    @AfterTest
    fun tearDown() {
    }

    @Test
    fun urlDecodeTest() {
        //@ test
        assertEquals(KUrlDecoder.urlDecode("%40"), "@")
        assertEquals(KUrlDecoder.urlEncode("@"), "%40")
    }

    @Test
    fun cookieTest() { //生成cookie格式字符串测试
        val cookieMap = mapOf<String, Any>(
            "key1" to "111",
            "key2" to "222",
            "key3" to listOf("k1", "k2"),
        )
        val cookie = KWebHelper.getWebCookie(cookieMap)
        assertEquals(cookie, "key1=111; key2=222; key3=[k1, k2]; ")
    }

    @Test
    fun base64Test() { //base64 tool test
        val testStrings = listOf<String>(
            "this is delan's test string", //normal string
            "string contains @ char",      //contains special char
        )

        testStrings.forEach {
            val encode = KBase64.encoder.encode(it.encodeToByteArray())
            val decodeStr = KBase64.decoder.decode(encode).decodeToString()
            assertEquals(it, decodeStr)
        }
    }

    @Test
    fun timeTest() { //timestamp test
        println(KUtil.currentTime())
    }


}
