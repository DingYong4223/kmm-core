package com.fit.kmm.kfile

import okio.FileSystem
import okio.Path
import okio.Sink
import okio.Source

actual class OkioWrapper {
    actual fun sink(file: Path): Sink {
        return FileSystem.SYSTEM.sink(file)
    }

    actual fun source(file: Path): Source {
        return FileSystem.SYSTEM.source(file)
    }

    actual fun exists(file: Path): Boolean {
        return FileSystem.SYSTEM.exists(file)
    }

    actual fun createDictionary(dir: Path) {
        FileSystem.SYSTEM.createDirectories(dir)
    }

}