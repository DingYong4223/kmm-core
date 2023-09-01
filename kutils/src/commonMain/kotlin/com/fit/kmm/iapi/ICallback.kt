package com.fit.kmm.iapi

interface ICallback<T> {
    fun onCallback(success: Boolean, code: Int, message: String?, result: T?)
}