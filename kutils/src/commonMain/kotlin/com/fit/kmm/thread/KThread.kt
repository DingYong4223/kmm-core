package com.fit.kmm.thread

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.jvm.JvmStatic


/**
 * ~~~~~~
 * Copyright (C), 2015-2022, tencent
 *
 * @FileName: KThread
 * @Author: delanding
 * @Date: 2023/8/15 11:06
 * @Description Life with Passion, Code with Creativity.
 * ~~~~~~
 */
class KThread<T> private constructor() {

    private var subscribeScope: CoroutineScope = ioScope
    private lateinit var subscribeRun: () -> T?
    private var observerScope: CoroutineScope = ioScope
    private var observerAction: Action<T>? = null

    companion object {

        @JvmStatic
        fun <T> withSubscribe(scope: CoroutineScope, run: () -> T?): KThread<T> {
            return KThread<T>().apply {
                this.subscribeScope = scope
                this.subscribeRun = run
            }
        }
    }

    /**
     * 订阅者
     * */
    fun withObserver(scope: CoroutineScope, action: Action<T>): KThread<T> {
        return this.apply {
            observerScope = scope
            observerAction = action
        }
    }

    /**
     * 启动
     * */
    fun start() {
        subscribeScope.launch {
            kotlin.runCatching {
                val result = subscribeRun.invoke()
                observerScope.launch {
                    observerAction?.onSuccess(result)
                }
            }.onFailure {
                observerScope.launch {
                    observerAction?.onError(it)
                }
            }
        }
    }

}