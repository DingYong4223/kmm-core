package com.fit.kmm.kredux

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import kotlin.coroutines.CoroutineContext

actual object LctDispatcher {

    actual fun uiDispatcher(): CoroutineContext = IosMainDispatcher

    actual fun defaultDispatcher(): CoroutineContext = IosMainDispatcher
}

/**
 * iOS doesn't have a default UI thread dispatcher like [Dispatchers.Main], so we have to implement it ourself.
 */
private object IosMainDispatcher : CoroutineDispatcher() {

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        dispatch_async(dispatch_get_main_queue()) { block.run() }
    }
}