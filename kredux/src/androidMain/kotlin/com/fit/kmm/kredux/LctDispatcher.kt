package com.fit.kmm.kredux

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

actual object LctDispatcher {

    @JvmStatic
    actual fun uiDispatcher(): CoroutineContext = Dispatchers.Main

    @JvmStatic
    actual fun defaultDispatcher() : CoroutineContext = Dispatchers.Default

}
