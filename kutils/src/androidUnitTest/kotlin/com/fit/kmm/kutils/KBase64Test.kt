package com.fit.kmm.kutils

import org.junit.Assert.assertArrayEquals
import kotlin.random.Random.Default.nextBytes
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class KBase64Test {

    @BeforeTest
    fun setUp() {
    }

    @AfterTest
    fun tearDown() {
    }

    @Test
    fun urlDecodeTest() {
        (0..100).forEach { i ->
            val input = nextBytes(i * 10)

            val javaEncoded = java.util.Base64.getEncoder().encodeToString(input)
            val kotlinEncoded = String(KBase64.encoder.encode(input))
            assertEquals(javaEncoded, kotlinEncoded)

            assertArrayEquals(input, KBase64.decoder.decode(kotlinEncoded.encodeToByteArray()))
        }
    }


}
