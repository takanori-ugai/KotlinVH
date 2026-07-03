package io.github.ugaikit.vh

import kotlinx.coroutines.runBlocking
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

internal suspend fun runJvmMain(writeImage: suspend (ByteArray) -> Unit) {
    runMainDemoInvoker(writeImage)
}

internal var runMainDemoInvoker: suspend (suspend (ByteArray) -> Unit) -> Unit = ::runMainDemo

fun main() {
    runBlocking {
        runJvmMain { image ->
            val path = Path("bfo.png")
            SystemFileSystem.sink(path).buffered().use { sink ->
                sink.write(image)
            }
        }
    }
}
