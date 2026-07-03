package io.github.ugaikit.vh

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName

@OptIn(ExperimentalJsExport::class)
@JsExport
class JsVirtualHomeClient private constructor(
    private val delegate: VirtualHomeClient,
) {
    @JsName("create")
    constructor(
        host: String = "localhost",
        port: Int = 8080,
        timeout: Int = READ_TIMEOUT.toInt(),
    ) : this(VirtualHomeClient(host, port, timeout.toLong()))

    @JsName("fromDelegate")
    internal constructor(delegate: Any) : this(delegate as VirtualHomeClient)

    suspend fun cameraCount(): Int = delegate.cameraCount()

    suspend fun activatePhysics(active: Boolean = true): VirtualHomeResponse = delegate.activatePhysics(active)

    suspend fun check(): VirtualHomeResponse = delegate.check()

    suspend fun checkScript(script: List<String>): VirtualHomeResponse = delegate.checkScript(script)

    suspend fun reset(sceneIndex: Int = 0): VirtualHomeResponse = delegate.reset(sceneIndex)

    suspend fun getObjects(): List<Node> = delegate.getObjects()

    suspend fun environmentGraph(): Graph = delegate.environmentGraph()

    fun close() = delegate.close()
}
