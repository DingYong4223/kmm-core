package com.fit.kmm.kfile

import com.fit.kmm.kutils.NSData
import com.fit.kmm.kutils.toByteArray
import platform.Foundation.NSData

/**
 * IOS处理文件读/写封装类
 * 次类仅在IOS工程中调用，KMM工程中直接调用KFile封装
 * 具体参考Ios Demo
 * */
class IosKFile(rootPath: String) : KFile(rootPath) {

    companion object {

        /**
         * 文件关联到Doc路径下
         * */
        fun withDocDir(context: Any) = IosKFile(KFilePlatform.docPath(context))

        /**
         * 文件关联到File路径下
         * */
        fun withCacheDir(context: Any) = IosKFile(KFilePlatform.cachePath(context))

        /**
         * 文件关联到绝对路径
         * */
        fun withAbsolutePath(path: String) = IosKFile(path).apply {
            withFileName("")
        }
    }

    /**
     * 关联子路径
     * */
    override fun withSubDir(subDir: String) = this.apply {
        super.withSubDir(subDir)
    }

    /**
     * 关联子路径
     * */
    override fun withFileName(fileName: String) = this.apply {
        super.withFileName(fileName)
    }

    /**
     * Create new file and write data. Path will be created if not exist.
     * @param nsdata bytes which will be write.
     * @param result write result for the specified file. path callback if success, null otherwise.
     * */
    @kotlinx.cinterop.ExperimentalForeignApi
    fun nSData2File(nsdata: NSData, result: ((String?) -> Unit)? = null) = bytes2File(nsdata.toByteArray(), result)

    /**
     * Read file content
     * @param result read result for the specified file.
     * @return empty byte array if file not exist, or the file content bytes in nsdata.
     * */
    @kotlinx.cinterop.ExperimentalForeignApi
    fun readFile2NSData(result: ((NSData?) -> Unit)?) {
        readFile {
            result?.invoke(it?.NSData())
        }
    }

}