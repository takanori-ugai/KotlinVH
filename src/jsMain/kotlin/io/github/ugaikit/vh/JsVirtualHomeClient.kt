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
    ) : this(createDelegate(host, port, timeout))

    @JsName("fromDelegate")
    internal constructor(delegate: Any) : this(delegate as VirtualHomeClient)

    private var closed = false

    suspend fun cameraCount(): Int {
        ensureOpen()
        return delegate.cameraCount()
    }

    suspend fun activatePhysics(active: Boolean = true): VirtualHomeResponse {
        ensureOpen()
        return delegate.activatePhysics(active)
    }

    suspend fun check(): VirtualHomeResponse {
        ensureOpen()
        return delegate.check()
    }

    suspend fun checkScript(script: List<String>): VirtualHomeResponse {
        ensureOpen()
        return delegate.checkScript(script)
    }

    suspend fun reset(sceneIndex: Int = 0): VirtualHomeResponse {
        ensureOpen()
        return delegate.reset(sceneIndex)
    }

    suspend fun getObjects(): List<Node> {
        ensureOpen()
        return delegate.getObjects()
    }

    suspend fun environmentGraph(): Graph {
        ensureOpen()
        return delegate.environmentGraph()
    }

    @Suppress("NON_EXPORTABLE_TYPE")
    suspend fun use(block: suspend (JsVirtualHomeClient) -> Unit) {
        try {
            block(this)
        } finally {
            close()
        }
    }

    fun close() {
        if (!closed) {
            closed = true
            delegate.close()
        }
    }

    private fun ensureOpen() {
        kotlin.check(!closed) { "JsVirtualHomeClient is closed" }
    }

    companion object {
        private fun createDelegate(
            host: String,
            port: Int,
            timeout: Int,
        ): VirtualHomeClient {
            require(host.isNotBlank()) { "host must not be blank" }
            require(host.none { it.isWhitespace() || it == '/' || it == '\\' || it == '?' || it == '#' }) {
                "host must be a plain host name"
            }
            require(port in 1..65535) { "port must be between 1 and 65535" }
            require(timeout > 0) { "timeout must be positive" }
            return VirtualHomeClient(host, port, timeout.toLong())
        }
    }
}
