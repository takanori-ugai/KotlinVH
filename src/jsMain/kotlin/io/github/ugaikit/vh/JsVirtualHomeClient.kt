package io.github.ugaikit.vh

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
class JsVirtualHomeClient(
    private val host: String = "localhost",
    private val port: Int = 8080,
    private val timeout: Long = READ_TIMEOUT,
) {
    private val delegate = VirtualHomeClient(host, port, timeout)

    suspend fun cameraCount(): Int = delegate.cameraCount()

    suspend fun activatePhysics(active: Boolean = true): VirtualHomeResponse = delegate.activatePhysics(active)

    suspend fun check(): VirtualHomeResponse = delegate.check()

    suspend fun checkScript(script: List<String>): VirtualHomeResponse = delegate.checkScript(script)

    suspend fun reset(sceneIndex: Int = 0): VirtualHomeResponse = delegate.reset(sceneIndex)

    suspend fun getObjects(): List<Node> = delegate.getObjects()

    suspend fun environmentGraph(): Graph = delegate.environmentGraph()

    fun close() = delegate.close()
}
