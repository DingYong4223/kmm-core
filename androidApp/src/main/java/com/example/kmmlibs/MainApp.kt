package com.example.kmmlibs

import android.app.Application
import com.fit.kmm.kloger.initLoger
import com.fit.kmm.mmkv.initKMMKV

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initLoger(this, true)
        initKMMKV(this)
    }

}
