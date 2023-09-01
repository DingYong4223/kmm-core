package com.fit.kmm.kutils

import kotlinx.cinterop.*
import platform.Foundation.NSData
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Foundation.dataUsingEncoding
import platform.posix.memcpy


/**
 * ~~~~~~
 * Copyright (C), 2015-2022, tencent
 *
 * @FileName: FuncExt
 * @Author: delanding
 * @Date: 2023/3/1 16:49
 * @Description Life with Passion, Code with Creativity.
 * some extension for ios data format.
 * ~~~~~~
 */

@kotlinx.cinterop.BetaInteropApi
@kotlinx.cinterop.ExperimentalForeignApi
inline fun ByteArray.NSData(): NSData {
    memScoped {
        return NSData.create(bytes =  allocArrayOf(this@NSData), length = this@NSData.size.toULong())
    }
}

@kotlinx.cinterop.BetaInteropApi
@kotlinx.cinterop.ExperimentalForeignApi
fun NSData.toByteArray(): ByteArray {
    val size = length.toInt()
    return ByteArray(size).apply {
        if (size > 0) usePinned {
            memcpy(it.addressOf(0), bytes, length)
        }
    }
}