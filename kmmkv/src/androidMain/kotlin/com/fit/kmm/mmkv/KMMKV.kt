package com.fit.kmm.mmkv

import com.tencent.mmkv.MMKV

actual class KMMKV actual constructor(file: String?) {

    private val mKv: MMKV by lazy { MMKV.mmkvWithID(if (file.isNullOrEmpty()) "global" else file, MMKV.MULTI_PROCESS_MODE) }

    actual fun putString(key: String, value: String) {
        mKv.putString(key, value)
    }

    actual fun putInt(key: String, value: Int) {
        mKv.putInt(key, value)
    }

    actual fun putLong(key: String, value: Long) {
        mKv.putLong(key, value)
    }

    actual fun putFloat(key: String, value: Float) {
        mKv.putFloat(key, value)
    }

    actual fun putBoolean(key: String, value: Boolean) {
        mKv.putBoolean(key, value)
    }

    actual fun remove(key: String) {
        mKv.remove(key)
    }

    actual fun clear(){
        mKv.clear()
    }

    actual fun getString(key: String, defValue: String?): String? {
        return mKv.getString(key, defValue)
    }

    actual fun getInt(key: String, defValue: Int) = mKv.getInt(key, defValue)

    actual fun getLong(key: String, defValue: Long) = mKv.getLong(key, defValue)

    actual fun getFloat(key: String, defValue: Float) = mKv.getFloat(key, defValue)

    actual fun getBoolean(key: String, defValue: Boolean) = mKv.getBoolean(key, defValue)

    actual fun contains(key: String) = mKv.contains(key)

    actual fun allKeys(): List<String> {
        val keys = mutableListOf<String>()
        mKv.allKeys()?.forEach {
            keys.add(it)
        }
        return keys
    }
}