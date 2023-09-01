package com.fit.kmm.kutils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException

//gson帮助类，防止gson自动将int数据转换成double类型
object GsonHelper {

    val mGson: Gson by lazy { GsonBuilder().disableHtmlEscaping().create() }

    //将json转换成T
    fun <T> json2Obj(json: String, clazz: Class<T>): T {
        return mGson.fromJson(json, clazz)
    }

    //将json转换成list
    fun <T> json2list(json: String): ArrayList<T> {
        return mGson.fromJson(json, object : TypeToken<ArrayList<T>>() {}.type)
    }

    //将json转换成map
    fun json2map(json: String?): Map<String, Any> {
        return try {
            val gson = GsonBuilder() //重写map的反序列化
                .registerTypeAdapter(
                    object : TypeToken<Map<String?, Any?>?>() {}.type,
                    MapTypeAdapter()
                )
                .disableHtmlEscaping()
                .create()
            gson.fromJson(json, object : TypeToken<Map<String?, Any?>?>() {}.type)
        } catch (e: Exception) {
            HashMap()
        }
    }

    /**
     * 对象转json
     *
     * @param obj
     * @return
     */
    fun toJson(obj: Any?): String {
        return mGson.toJson(obj)
    }

    class MapTypeAdapter : TypeAdapter<Any?>() {
        @Throws(IOException::class)
        override fun read(inVar: JsonReader): Any? {
            val token = inVar.peek()
            return when (token) {
                JsonToken.BEGIN_ARRAY -> {
                    val list: MutableList<Any?> = ArrayList()
                    inVar.beginArray()
                    while (inVar.hasNext()) {
                        list.add(read(inVar))
                    }
                    inVar.endArray()
                    list
                }
                JsonToken.BEGIN_OBJECT -> {
                    val map: MutableMap<String, Any?> =
                        LinkedTreeMap()
                    inVar.beginObject()
                    while (inVar.hasNext()) {
                        map[inVar.nextName()] = read(inVar)
                    }
                    inVar.endObject()
                    map
                }
                JsonToken.STRING -> inVar.nextString()
                JsonToken.NUMBER -> {
                    val dbNum = inVar.nextString()
                    return if (dbNum.contains(".")) {
                        dbNum.toDouble()
                    } else {
                        dbNum.toLong()
                    }
                }
                JsonToken.BOOLEAN -> inVar.nextBoolean()
                JsonToken.NULL -> {
                    inVar.nextNull()
                    null
                }
                else -> throw IllegalStateException()
            }
        }

        @Throws(IOException::class)
        override fun write(out: JsonWriter, value: Any?) {
            // 序列化无需实现
        }
    }
}