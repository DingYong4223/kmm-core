package com.fit.kmm.mmkv

import cocoapods.MMKV.MMKV

actual class KMMKV actual constructor(file: String?) {

    private val mKv: MMKV by lazy { mmkvWithId(if (file.isNullOrEmpty()) "global" else file) }

    private fun mmkvWithId(configName: String): MMKV {
        return MMKV.mmkvWithID(configName)
            ?: throw Exception("create mmkv with id $configName failed")
    }

    actual fun putString(key: String, value: String) {
        mKv.setString(value, key)
    }

    actual fun putInt(key: String, value: Int) {
        mKv.setInt32(value, key)
    }

    actual fun putLong(key: String, value: Long) {
        mKv.setInt64(value, key)
    }

    actual fun putFloat(key: String, value: Float) {
        mKv.setFloat(value, key)
    }

    actual fun putBoolean(key: String, value: Boolean) {
        mKv.setBool(value, key)
    }

    actual fun remove(key: String) {
        mKv.removeValueForKey(key)
    }

    actual fun clear() {
        mKv.clearAll()
    }

    actual fun getString(key: String, defValue: String?): String? {
        return mKv.getStringForKey(key, defValue) ?: defValue
    }

    actual fun getInt(key: String, defValue: Int): Int {
        return mKv.getInt32ForKey(key, defValue)
    }

    actual fun getLong(key: String, defValue: Long): Long {
        return mKv.getInt64ForKey(key, defValue)
    }

    actual fun getFloat(key: String, defValue: Float): Float {
        return mKv.getFloatForKey(key, defValue)
    }

    actual fun getBoolean(key: String, defValue: Boolean): Boolean {
        return mKv.getBoolForKey(key, defValue)
    }

    actual fun contains(key: String): Boolean {
        return mKv.containsKey(key)
    }

    actual fun allKeys(): List<String> {
        return mKv.allKeys() as List<String>
    }
}