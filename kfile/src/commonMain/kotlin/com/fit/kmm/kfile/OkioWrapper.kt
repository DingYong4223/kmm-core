package com.fit.kmm.kfile

import okio.Path
import okio.Sink
import okio.Source

expect class OkioWrapper() {
    fun source(file: Path): Source
    fun sink(file: Path): Sink
    fun exists(file: Path): Boolean
    fun createDictionary(dir: Path)
}