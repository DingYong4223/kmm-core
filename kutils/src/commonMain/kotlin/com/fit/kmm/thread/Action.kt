package com.fit.kmm.thread


/**
 * ~~~~~~
 * Copyright (C), 2015-2022, tencent
 *
 * @FileName: Action
 * @Author: delanding
 * @Date: 2023/8/15 11:15
 * @Description Life with Passion, Code with Creativity.
 * ~~~~~~
 */
interface Action<T> {
    fun onSuccess(t: T?)
    fun onError(e: Throwable) = Unit
}