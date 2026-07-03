package io.github.ugaikit.vh

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicReference

/**
 * A Java-friendly wrapper for VirtualHomeClient that executes requests synchronously.
 * This class is intended to be used from Java code where suspending functions are not easily supported.
 */
class JavaVirtualHomeClient
    @JvmOverloads
    constructor(
        host: String = "localhost",
        port: Int = 8080,
        private val client: VirtualHomeClient = VirtualHomeClient(host, port),
    ) : CloseableResource {
        private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

        @Volatile
        private var closed = false

        /**
         * Performs a health check of the VirtualHome server.
         *
         * @return The server's response as a [VirtualHomeResponse].
         */
        fun check(): VirtualHomeResponse = blockingCall { client.check() }

        /**
         * Reset the virtual environment to the scene specified by index.
         *
         * @param sceneIndex The index of the scene to reset.
         * @return The VirtualHomeResponse describing the outcome of the reset operation.
         */
        fun reset(sceneIndex: Int): VirtualHomeResponse = blockingCall { client.reset(sceneIndex) }

        /**
         * Fetches the client's current environment graph.
         *
         * @return The environment graph representing the current rooms, objects, and their relationships.
         */
        fun environmentGraph(): Graph = blockingCall { client.environmentGraph() }

        /**
         * Expands the provided scene graph according to the given configuration.
         *
         * @param graph The scene graph to expand.
         * @param config Configuration options that control how the scene is expanded. Defaults to an empty configuration.
         * @return A VirtualHomeResponse containing the expansion result.
         */
        @JvmOverloads
        fun expandScene(
            graph: Graph,
            config: ExpandSceneConfig = ExpandSceneConfig(),
        ): VirtualHomeResponse = blockingCall { client.expandScene(graph, config) }

        /**
         * Adds a character to the scene.
         *
         * @param characterResource Path to the character resource (for example "Chars/Male1").
         * @param position Optional initial world position for the character; if `null`, a default placement is used.
         * @param initialRoom Name of the room to place the character in; an empty string means no explicit room assignment.
         * @return A `VirtualHomeResponse` describing the result of the add-character operation.
         */
        @JvmOverloads
        fun addCharacter(
            characterResource: String = "Chars/Male1",
            position: Position? = null,
            initialRoom: String = "",
        ): VirtualHomeResponse = blockingCall { client.addCharacter(characterResource, position, initialRoom) }

        /**
         * Renders a sequence of scene commands and returns the rendering result.
         *
         * @param script A list of script lines or commands that describe the actions to render in the scene.
         * @param config Rendering options and limits to apply for this render.
         * @return A VirtualHomeResponse containing the render outcome and associated metadata.
         */
        @JvmOverloads
        fun renderScript(
            script: List<String>,
            config: RenderParams = RenderParams(),
        ): VirtualHomeResponse = blockingCall { client.renderScript(script, config) }

        /**
         * Adds a camera to the scene at the specified position and rotation.
         *
         * @param position Camera world position.
         * @param rotation Camera rotation (Euler angles).
         * @param fieldView Camera field of view in degrees (default 40).
         * @return A VirtualHomeResponse describing the result of the add-camera operation.
         */
        @JvmOverloads
        fun addCamera(
            position: Position,
            rotation: Position,
            fieldView: Int = 40,
        ): VirtualHomeResponse = blockingCall { client.addCamera(position, rotation, fieldView) }

        /**
         * Retrieves the number of cameras currently configured in the virtual environment.
         *
         * @return The number of configured cameras.
         */
        fun cameraCount(): Int = blockingCall { client.cameraCount() }

        /**
         * Retrieve camera data for the specified camera indices.
         *
         * @param cameraIndexes The list of camera indices to retrieve data for.
         * @return A [VirtualHomeResponse] containing camera metadata and state for the requested cameras.
         */
        fun cameraData(cameraIndexes: List<Int>): VirtualHomeResponse = blockingCall { client.cameraData(cameraIndexes) }

        /**
         * Retrieve the objects visible to a specific camera.
         *
         * @param cameraIndex Index of the camera whose visible objects to query (zero-based).
         * @return A map from object identifier to its label for all objects visible to the specified camera.
         */
        fun visibleObjects(cameraIndex: Int): Map<String, String> = blockingCall { client.visibleObjects(cameraIndex) }

        /**
         * Capture images from the specified cameras and return their raw byte data.
         *
         * @param cameraIndexes Indices of the cameras to capture; output list order matches this list.
         * @param mode Image capture mode (e.g., "normal").
         * @param imageWidth Desired image width in pixels.
         * @param imageHeight Desired image height in pixels.
         * @return A list of image byte arrays corresponding to each requested camera index in order.
         */
        @JvmOverloads
        fun cameraImage(
            cameraIndexes: List<Int>,
            mode: String = "normal",
            imageWidth: Int = 640,
            imageHeight: Int = 320,
        ): List<ByteArray> = blockingCall { client.cameraImage(cameraIndexes, mode, imageWidth, imageHeight) }

        /**
         * Create a RenderParams configured with the given rendering options.
         *
         * @param processingTimeLimit Maximum processing time (in seconds) allowed for rendering.
         * @param findSolution Whether to search for a valid solution before rendering.
         * @param skipAnimation Whether to omit animations from the render.
         * @param recording Whether to enable recording during rendering.
         * @param savePoseData Whether to persist pose data produced during rendering.
         * @param skipExecution Whether to skip executing actions when rendering.
         * @return A RenderParams instance populated with the provided field values.
         */
        @JvmOverloads
        fun createRenderParams(
            processingTimeLimit: Int = 10,
            findSolution: Boolean = false,
            skipAnimation: Boolean = false,
            recording: Boolean = false,
            savePoseData: Boolean = false,
            skipExecution: Boolean = false,
        ): RenderParams =
            RenderParams(
                processingTimeLimit = processingTimeLimit,
                findSolution = findSolution,
                skipAnimation = skipAnimation,
                recording = recording,
                savePoseData = savePoseData,
                skipExecution = skipExecution,
            )

        override fun close() {
            if (!closed) {
                closed = true
                scope.cancel()
                client.close()
            }
        }

        private fun <T> blockingCall(block: suspend () -> T): T {
            check(!closed) { "JavaVirtualHomeClient is closed" }

            val latch = CountDownLatch(1)
            val result = AtomicReference<BlockingCallResult<T>?>(null)
            val job =
                scope.launch {
                    try {
                        result.set(BlockingCallResult.Success(block()))
                    } catch (exception: Throwable) {
                        result.set(BlockingCallResult.Failure(exception))
                    } finally {
                        latch.countDown()
                    }
                }
            job.invokeOnCompletion { cause ->
                if (cause != null && result.compareAndSet(null, BlockingCallResult.Failure(cause))) {
                    latch.countDown()
                }
            }

            try {
                latch.await()
            } catch (exception: InterruptedException) {
                job.cancel()
                Thread.currentThread().interrupt()
                throw IllegalStateException("Interrupted while waiting for VirtualHome request", exception)
            }

            return when (val outcome = result.get()) {
                is BlockingCallResult.Success -> outcome.value
                is BlockingCallResult.Failure -> throw outcome.cause
                null -> throw IllegalStateException("VirtualHome request did not complete")
            }
        }
    }

private sealed class BlockingCallResult<out T> {
    data class Success<T>(
        val value: T,
    ) : BlockingCallResult<T>()

    data class Failure(
        val cause: Throwable,
    ) : BlockingCallResult<Nothing>()
}
