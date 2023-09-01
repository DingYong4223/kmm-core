package com.fit.kmm.kutils

import android.content.Context
import android.content.SharedPreferences
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

/**
 * Shared preference 存储帮助类，用于简化share存储
 *
 * @author delanding
 */
class SharedPrefHelper(context: Context) {

    private val sp: SharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)

    /**
     * 设置箭为key的share存储，针对uin的私有存储
     * @param uin the user id.
     * @param key the key of the stored.
     * @param objValue the value's type.
     */
    private fun setUValue(uin: String, key: String, objValue: Any) {
        val hashkey = key + "_" + uin
        setValue(hashkey, objValue)
    }

    /**获取箭为key的share存储，针对uin的私有存储
     * @param key the key of the stored.
     * @param defValue the value's type.
     */
    private fun getUValue(uin: String, key: String, defValue: Any): Any {
        val hashkey = key + "_" + uin
        return getValue(hashkey, defValue)
    }

    /**
     * set the key-value to the share.
     * @param key the key of the stored.
     * @param objValue the value's type.
     */
    private fun setValue(key: String, objValue: Any) {
        lock.lock()
        try {
            when (objValue) {
                is Int -> {
                    sp.edit().putInt(key, objValue).commit()
                }
                is Boolean -> {
                    sp.edit().putBoolean(key, objValue).commit()
                }
                is Long -> {
                    sp.edit().putLong(key, objValue).commit()
                }
                is String -> {
                    sp.edit().putString(key, objValue).commit()
                }
            }
        } finally {
            lock.unlock()
        }
    }

    /**
     * get value from shared preference, where key is key and the default value is objvalue
     * @param key the key of the stored value.
     * @param defValue the default value return if there has no value in the share.
     */
    private fun getValue(key: String, defValue: Any): Any {
        var objValue = defValue
        lock.lock()
        return try {
            when (objValue) {
                is Int -> {
                    return sp.getInt(key, objValue)
                }
                is Boolean -> {
                    return sp.getBoolean(key, objValue)
                }
                is Long -> {
                    return sp.getLong(key, objValue)
                }
                is String -> {
                    return sp.getString(key, objValue) ?: defValue
                }
                else -> objValue
            }
        } finally {
            lock.unlock()
        }
    }

    companion object {
        private const val SP_NAME = "lct_config"
        private val lock: Lock = ReentrantLock()

        @JvmStatic
        fun getValue(context: Context, key: String, defValue:Any): Any {
            return SharedPrefHelper(context).getValue(key, defValue)
        }

        @JvmStatic
        fun setValue(context: Context, key: String, objValue:Any) {
            SharedPrefHelper(context).setValue(key, objValue)
        }

        @JvmStatic
        fun getUValue(context: Context, uin: String, key: String, defValue:Any): Any {
            return SharedPrefHelper(context).getUValue(uin, key, defValue)
        }

        @JvmStatic
        fun setUValue(context: Context, uin: String, key: String, objValue:Any) {
            SharedPrefHelper(context).setUValue(uin, key, objValue)
        }

    }

}