package com.fit.kmm.thread

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

actual val uiScope: CoroutineScope
    get() = CoroutineScope(Dispatchers.Main)

actual val ioScope: CoroutineScope
    get() = CoroutineScope(Dispatchers.Default)
