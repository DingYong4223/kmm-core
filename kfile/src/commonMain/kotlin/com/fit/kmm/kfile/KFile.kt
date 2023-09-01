package com.fit.kmm.kfile

import com.fit.kmm.thread.Action
import com.fit.kmm.thread.KThread
import com.fit.kmm.thread.ioScope
import com.fit.kmm.thread.uiScope
import okio.Path
import okio.Path.Companion.toPath
import okio.buffer
import okio.use

/**
 * 处理文件读/写封装类
 * */
open class KFile(private val rootPath: String) {

    //相对于根路径的子路径
    private var subDir: String = ""
    //文件名
    private var fileName: String? = null

    companion object {

        /**
         * 文件关联到Doc路径下
         * */
        fun withDocDir(context: Any) = KFile(KFilePlatform.docPath(context))

        /**
         * 文件关联到File路径下
         * */
        fun withCacheDir(context: Any) = KFile(KFilePlatform.cachePath(context))

        /**
         * 文件关联到绝对路径
         * */
        fun withAbsolutePath(path: String) = KFile(path).apply {
            fileName = ""
        }
    }

    /**
     * 关联子路径
     * */
    open fun withSubDir(subDir: String) = this.apply {
        this.subDir = subDir
    }

    /**
     * 关联文件名
     * */
    open fun withFileName(fileName: String) = this.apply {
        this.fileName = fileName
    }

    /**
     * 返回整个完整路径 = 根路径 + subDir
     * */
    fun getFullPathWithNoFileName(): String {
        val dir = if(subDir.isNotEmpty()) {
            rootPath + Path.DIRECTORY_SEPARATOR + subDir
        } else {
            rootPath
        }
        return dir.replace(
            "${Path.DIRECTORY_SEPARATOR}${Path.DIRECTORY_SEPARATOR}",
            Path.DIRECTORY_SEPARATOR
        )
    }

    //生成文件全路径名，带文件名
    fun getFullPathWithFileName(): String {
        val dir = getFullPathWithNoFileName()
        val fullPath = if(fileName!!.isNotEmpty()) {
            dir + Path.DIRECTORY_SEPARATOR + fileName
        } else {
            dir
        }
        return fullPath.replace(
            "${Path.DIRECTORY_SEPARATOR}${Path.DIRECTORY_SEPARATOR}",
            Path.DIRECTORY_SEPARATOR
        )
    }

    /**
     * Create new file and write data. Path will be created if not exist.
     * @param bytes bytes which will be write.
     * @param result write result for the operation，return path of the file if success, null otherwise.
     * @return file path of the saving file
     * */
    fun bytes2File(bytes: ByteArray, result: ((String?) -> Unit)? = null) {
        if(fileName == null) { //文件名不允许为空
            throw RuntimeException("file name must be specified!")
        }
        KThread.withSubscribe(ioScope) {
            val fullDir = getFullPathWithNoFileName()
            val okioWrapper = OkioWrapper()
            if (!okioWrapper.exists(fullDir.toPath())) {
                okioWrapper.createDictionary(fullDir.toPath())
            }
            val filePath = getFullPathWithFileName()
            filePath.toPath().apply {
                val sink = okioWrapper.sink(this)
                sink.use {
                    sink.buffer().use {
                        it.write(bytes)
                    }
                }
            }
            return@withSubscribe filePath.toPath().toString()
        }.withObserver(uiScope, object : Action<String> {
            override fun onSuccess(t: String?) {
                result?.invoke(t)
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                result?.invoke(null)
            }
        }).start()
    }

    /**
     * Read file content
     * @return empty byte array if file not exist, or the file content bytes.
     * */
    fun readFile(result: ((ByteArray?) -> Unit)?) {
        if(fileName == null) { //文件名不允许为空
            throw RuntimeException("file name must be specified!")
        }
        KThread.withSubscribe(ioScope) {
            val fullDir = getFullPathWithNoFileName()
            val okioWrapper = OkioWrapper()
            if (!okioWrapper.exists(fullDir.toPath())) {
                return@withSubscribe null
            }
            val filePath = getFullPathWithFileName()
            val source = okioWrapper.source(filePath.toPath())
            source.buffer().use {
                it.readByteArray().apply {
                    return@withSubscribe this
                }
            }
        }.withObserver(uiScope, object : Action<ByteArray> {
            override fun onSuccess(t: ByteArray?) {
                result?.invoke(t)
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                result?.invoke(null)
            }
        }).start()
    }
}