package io.github.ugaikit.vh

import kotlinx.coroutines.runBlocking

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
    ) {
        /**
         * Performs a health check of the VirtualHome server.
         *
         * @return The server's response as a [VirtualHomeResponse].
         */
        fun check(): VirtualHomeResponse = runBlocking { client.check() }

        /**
         * Reset the virtual environment to the scene specified by index.
         *
         * @param sceneIndex The index of the scene to reset.
         * @return The VirtualHomeResponse describing the outcome of the reset operation.
         */
        fun reset(sceneIndex: Int): VirtualHomeResponse = runBlocking { client.reset(sceneIndex) }

        /**
         * Fetches the client's current environment graph.
         *
         * @return The environment graph representing the current rooms, objects, and their relationships.
         */
        fun environmentGraph(): Graph = runBlocking { client.environmentGraph() }

        /**
         * Expands the provided scene graph according to the given configuration.
         *
         * @param graph The scene graph to expand.
         * @param config Configuration options that control how the scene is expanded. Defaults to an empty configuration.
         * @return A VirtualHomeResponse containing the expansion result.
         */
        fun expandScene(
            graph: Graph,
            config: ExpandSceneConfig = ExpandSceneConfig(),
        ): VirtualHomeResponse = runBlocking { client.expandScene(graph, config) }

        /**
         * Adds a character to the scene.
         *
         * @param characterResource Path to the character resource (for example "Chars/Male1").
         * @param position Optional initial world position for the character; if `null`, a default placement is used.
         * @param initialRoom Name of the room to place the character in; an empty string means no explicit room assignment.
         * @return A `VirtualHomeResponse` describing the result of the add-character operation.
         */
        fun addCharacter(
            characterResource: String = "Chars/Male1",
            position: Position? = null,
            initialRoom: String = "",
        ): VirtualHomeResponse = runBlocking { client.addCharacter(characterResource, position, initialRoom) }

        /**
         * Renders a sequence of scene commands and returns the rendering result.
         *
         * @param script A list of script lines or commands that describe the actions to render in the scene.
         * @param config Rendering options and limits to apply for this render.
         * @return A VirtualHomeResponse containing the render outcome and associated metadata.
         */
        fun renderScript(
            script: List<String>,
            config: RenderParams = RenderParams(),
        ): VirtualHomeResponse = runBlocking { client.renderScript(script, config) }

        /**
         * Adds a camera to the scene at the specified position and rotation.
         *
         * @param position Camera world position.
         * @param rotation Camera rotation (Euler angles).
         * @param fieldView Camera field of view in degrees (default 40).
         * @return A VirtualHomeResponse describing the result of the add-camera operation.
         */
        fun addCamera(
            position: Position,
            rotation: Position,
            fieldView: Int = 40,
        ): VirtualHomeResponse = runBlocking { client.addCamera(position, rotation, fieldView) }

        /**
         * Retrieves the number of cameras currently configured in the virtual environment.
         *
         * @return The number of configured cameras.
         */
        fun cameraCount(): Int = runBlocking { client.cameraCount() }

        /**
         * Retrieve camera data for the specified camera indices.
         *
         * @param cameraIndexes The list of camera indices to retrieve data for.
         * @return A [VirtualHomeResponse] containing camera metadata and state for the requested cameras.
         */
        fun cameraData(cameraIndexes: List<Int>): VirtualHomeResponse = runBlocking { client.cameraData(cameraIndexes) }

        /**
         * Retrieve the objects visible to a specific camera.
         *
         * @param cameraIndex Index of the camera whose visible objects to query (zero-based).
         * @return A map from object identifier to its label for all objects visible to the specified camera.
         */
        fun visibleObjects(cameraIndex: Int): Map<String, String> = runBlocking { client.visibleObjects(cameraIndex) }

        /**
         * Capture images from the specified cameras and return their raw byte data.
         *
         * @param cameraIndexes Indices of the cameras to capture; output list order matches this list.
         * @param mode Image capture mode (e.g., "normal").
         * @param imageWidth Desired image width in pixels.
         * @param imageHeight Desired image height in pixels.
         * @return A list of image byte arrays corresponding to each requested camera index in order.
         */
        fun cameraImage(
            cameraIndexes: List<Int>,
            mode: String = "normal",
            imageWidth: Int = 640,
            imageHeight: Int = 320,
        ): List<ByteArray> = runBlocking { client.cameraImage(cameraIndexes, mode, imageWidth, imageHeight) }

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
        fun createRenderParams(
            processingTimeLimit: Int,
            findSolution: Boolean,
            skipAnimation: Boolean,
            recording: Boolean,
            savePoseData: Boolean,
            skipExecution: Boolean,
        ): RenderParams =
            RenderParams(
                processingTimeLimit = processingTimeLimit,
                findSolution = findSolution,
                skipAnimation = skipAnimation,
                recording = recording,
                savePoseData = savePoseData,
                skipExecution = skipExecution,
            )
    }
