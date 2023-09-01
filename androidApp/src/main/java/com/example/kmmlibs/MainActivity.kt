package com.example.kmmlibs

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fit.kmm.kfile.KFile
import com.fit.kmm.khttp.INet
import com.fit.kmm.khttp.INetDownload
import com.fit.kmm.khttp.KtorHelper
import com.fit.kmm.khttp.NetHelper
import com.fit.kmm.kloger.KLoger
import com.fit.kmm.mmkv.KMMKV
import com.fit.kmm.thread.KTask
import com.fit.kmm.thread.ioScope
import com.fit.kmm.thread.uiScope


class MainActivity : AppCompatActivity() {

    private val mainView: LinearLayout by lazy { findViewById(R.id.main_view) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initTest()
    }

    private fun initTest() {
        mainView.addView(Button(this@MainActivity).apply {
            text = "Log Test"
            setOnClickListener {
                KLoger.i("this is log test......")
            }
        })
        mainView.addView(Button(this@MainActivity).apply {
            text = "Http GET Test"
            setOnClickListener {
                val url =
                    "https://suggest.taobao.com/sug?code=utf-8&q=%E8%A1%A3%E6%9C%8D&callback=cb"
                NetHelper.with().get(url, object : INet {
                    override fun back(
                        retCode: Int,
                        header: Map<String, List<String>>,
                        body: String
                    ) {
                        KLoger.v("thread: " + Thread.currentThread().name)
                        showAlert(body)
                    }
                })
            }
        })
        mainView.addView(Button(this@MainActivity).apply {
            text = "Http TIMEOUT GET Test"
            setOnClickListener {
                val url =
                    "https://suggest.taobao.com/sug?code=utf-8&q=%E8%A1%A3%E6%9C%8D&callback=cb"
                NetHelper.with().withTimeout(100).get(url, object : INet {
                    override fun back(
                        retCode: Int,
                        header: Map<String, List<String>>,
                        body: String
                    ) {
                        KLoger.v("thread id: " + Thread.currentThread().name)
                        showAlert(body)
                    }
                })
            }
        })
        mainView.addView(Button(this@MainActivity).apply {
            text = "Http POST Test"
            setOnClickListener {
                val url = "https://api.androidhive.info/volley/person_object.json"
                val header = mutableMapOf<String, String>().apply {
                    this["header1"] = "00000"
                    this["header2"] = "11111"
                }
                val body = mutableMapOf<String, String>().apply {
                    this["key"] = "value"
                }
                NetHelper.with()
                    .withHeader(header)
                    .jsonPost(url, body, object : INet {
                        override fun back(
                            retCode: Int,
                            header: Map<String, List<String>>,
                            body: String
                        ) {
                            KLoger.v("thread id: " + Thread.currentThread().name)
                            showAlert("retCode: $retCode, header: $header, body: $body")
                        }
                    })
            }
        })
        mainView.addView(Button(this@MainActivity).apply {
            text = "Http Download Test"
            setOnClickListener {
                val url =
                    "https://inews.gtimg.com/om_bt/OI3Uod_dCKpBvNO-lHCcG9rkw6nufFuFJqm1aGTeWs0gAAA/1000"
                KtorHelper.with()
                    .download(url, object : INetDownload {
                        override fun back(
                            retCode: Int,
                            header: Map<String, List<String>>,
                            body: ByteArray
                        ) {
                            if (body.isNotEmpty()) {
                                KFile.withCacheDir(this@MainActivity)
                                    .withSubDir("test")
                                    .withFileName("test.webp")
                                    .bytes2File(body) {
                                        showAlert("文件已保存: $it")
                                    }
                            }
                        }
                    })
            }
        })
        mainView.addView(Button(this@MainActivity).apply {
            text = "MMKV Test"
            setOnClickListener {
                val kv = KMMKV("delan_test")
                kv.putString("key_test", "value_test------")

                val getStr = KMMKV("delan_test").getString("key_test", "")
                if (getStr == "value_test------") {
                    Toast.makeText(this@MainActivity, "value: $getStr", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "get fail: $getStr", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
        mainView.addView(Button(this@MainActivity).apply {
            text = "KFile文件写测试"
            setOnClickListener {
                val sb = """这是测试文字which will 被写入
                    |换行测试
                    测试测试文字"""
                KFile //写Doc文件
                    .withDocDir(this@MainActivity)
                    .withSubDir("test")
                    .withFileName("a.txt")
                    .bytes2File(sb.encodeToByteArray()) { filePath ->
                        if (!filePath.isNullOrEmpty()) {
                            KFile  //读文件
                                .withAbsolutePath(filePath)
                                .readFile() {
                                    showAlert("Doc文件匹配情况：${it?.decodeToString() == sb}, filePath: $filePath")
                                }
                        }
                    }

                KFile //写Cache文件
                    .withCacheDir(this@MainActivity)
                    .withSubDir("test")
                    .withFileName("a.txt")
                    .bytes2File(sb.encodeToByteArray()) { filePath ->
                        if (!filePath.isNullOrEmpty()) {
                            KFile  //读文件
                                .withAbsolutePath(filePath)
                                .readFile {
                                    showAlert("Cache文件匹配情况：${it?.decodeToString() == sb}, filePath: $filePath")
                                }
                        }
                    }
            }
        })
        mainView.addView(Button(this@MainActivity).apply {
            text = "异步任务测试"
            setOnClickListener {
                KTask.post(ioScope) {
                    KLoger.i("task run: " + Thread.currentThread().name)
                }
                KTask.postDelay(ioScope, 5000) {
                    KLoger.i("task delay run: " + Thread.currentThread().name)
                }
                KTask.postDelay(uiScope, 7000) {
                    KLoger.i("task delay run: " + Thread.currentThread().name)
                }
                Toast.makeText(this@MainActivity, "异步任务已执行，查看日志打印", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showAlert(msg: String) {
        AlertDialog.Builder(this)
            .setTitle("Tip")
            .setMessage(msg) // Specifying a listener allows you to take an action before dismissing the dialog.
            .setPositiveButton(android.R.string.yes,
                DialogInterface.OnClickListener { dialog, which ->
                }) // A null listener allows the button to dismiss the dialog and take no further action.
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

}
