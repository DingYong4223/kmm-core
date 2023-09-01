package com.fit.kmm.thread

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Runnable
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_global_queue
import platform.darwin.dispatch_get_main_queue
import kotlin.coroutines.CoroutineContext

actual val uiScope: CoroutineScope
    get() = CoroutineScope(object: CoroutineDispatcher() {
        override fun dispatch(context: CoroutineContext, block: Runnable) {
            dispatch_async(dispatch_get_main_queue()) { block.run() }
        }
    })

actual val ioScope: CoroutineScope
    get() = CoroutineScope(object: CoroutineDispatcher() {
        override fun dispatch(context: CoroutineContext, block: Runnable) {
            val ul = 0u
            val ulong = ul.toULong()
            dispatch_async(dispatch_get_global_queue(0, ulong)) { block.run() }
        }
    })