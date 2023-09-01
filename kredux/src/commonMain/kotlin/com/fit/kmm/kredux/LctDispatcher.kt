package com.fit.kmm.kredux

import kotlin.coroutines.CoroutineContext

expect object LctDispatcher {
    fun uiDispatcher(): CoroutineContext
    fun defaultDispatcher(): CoroutineContext
}