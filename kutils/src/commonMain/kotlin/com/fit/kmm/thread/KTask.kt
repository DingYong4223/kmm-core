package com.fit.kmm.thread

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.jvm.JvmStatic


/**
 * ~~~~~~
 * Copyright (C), 2015-2022, tencent
 *
 * @FileName: KTask
 * @Author: delanding
 * @Date: 2023/8/15 11:06
 * @Description Life with Passion, Code with Creativity.
 * ~~~~~~
 */
class KTask private constructor() {

    private var subscribeScope: CoroutineScope = ioScope
    private var subscribeRun: () -> Unit = {}
    private var delayTime: Long = 0L

    companion object {

        @JvmStatic
        fun post(scope: CoroutineScope, run: () -> Unit) {
            KTask().apply {
                this.subscribeScope = scope
                this.subscribeRun = run
            }.start()
        }

        @JvmStatic
        fun postDelay(scope: CoroutineScope, delay: Long, run: () -> Unit) {
            KTask().apply {
                this.subscribeScope = scope
                this.subscribeRun = run
                this.delayTime = delay
            }.start()
        }

    }

    /**
     * 启动
     * */
    fun start(): Any = if (delayTime > 0) {
        ioScope.launch {
            delay(delayTime)
            execTask()
        }
    } else {
        execTask()
    }

    private inline fun execTask() {
        subscribeScope.launch {
            kotlin.runCatching {
                subscribeRun.invoke()
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

}