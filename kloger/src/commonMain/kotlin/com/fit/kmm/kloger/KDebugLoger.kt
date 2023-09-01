package com.fit.kmm.kloger

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class KDebugLoger : IKLoger {

    companion object {
        private val initor by lazy { Napier.base(DebugAntilog()) }
    }

    init {
        this.i("log init: $initor")
    }

    override fun v(msg: String) = Napier.v(msg)

    override fun i(msg: String) = Napier.i(msg)

    override fun d(msg: String) = Napier.d(msg)

    override fun e(msg: String) = Napier.e(msg)

}