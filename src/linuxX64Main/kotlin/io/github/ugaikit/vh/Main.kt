package io.github.ugaikit.vh

import kotlinx.coroutines.runBlocking
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

fun main() {
    runBlocking {
        runMainDemo { image ->
            val path = Path("bfo.png")
            SystemFileSystem.sink(path).buffered().use { sink ->
                sink.write(image)
            }
        }
    }
}
