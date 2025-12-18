package io.github.ugaikit.vh

import kotlinx.coroutines.runBlocking

/**
 * A Java-friendly wrapper for VirtualHomeClient that executes requests synchronously.
 * This class is intended to be used from Java code where suspending functions are not easily supported.
 */
class JavaVirtualHomeClient(host: String = "localhost", port: Int = 8080) {
    private val client = VirtualHomeClient(host, port)

    fun check(): VirtualHomeResponse = runBlocking { client.check() }

    fun reset(sceneIndex: Int): VirtualHomeResponse = runBlocking { client.reset(sceneIndex) }

    fun environmentGraph(): Graph = runBlocking { client.environmentGraph() }

    fun expandScene(graph: Graph, config: ExpandSceneConfig = ExpandSceneConfig()): VirtualHomeResponse =
        runBlocking { client.expandScene(graph, config) }

    fun addCharacter(
        characterResource: String = "Chars/Male1",
        position: Position? = null,
        initialRoom: String = ""
    ): VirtualHomeResponse = runBlocking { client.addCharacter(characterResource, position, initialRoom) }

    fun renderScript(
        script: List<String>,
        config: RenderParams = RenderParams()
    ): VirtualHomeResponse = runBlocking { client.renderScript(script, config) }

    fun addCamera(
        position: Position,
        rotation: Position,
        fieldView: Int = 40
    ): VirtualHomeResponse = runBlocking { client.addCamera(position, rotation, fieldView) }

    fun cameraCount(): Int = runBlocking { client.cameraCount() }

    fun cameraData(cameraIndexes: List<Int>): VirtualHomeResponse =
        runBlocking { client.cameraData(cameraIndexes) }

    fun visibleObjects(cameraIndex: Int): Map<String, String> =
        runBlocking { client.visibleObjects(cameraIndex) }

    fun cameraImage(
        cameraIndexes: List<Int>,
        mode: String = "normal",
        imageWidth: Int = 640,
        imageHeight: Int = 320
    ): List<ByteArray> = runBlocking { client.cameraImage(cameraIndexes, mode, imageWidth, imageHeight) }

    fun createRenderParams(
        processingTimeLimit: Int,
        findSolution: Boolean,
        skipAnimation: Boolean,
        recording: Boolean,
        savePoseData: Boolean,
        skipExecution: Boolean
    ): RenderParams {
        return RenderParams(
            processingTimeLimit = processingTimeLimit,
            findSolution = findSolution,
            skipAnimation = skipAnimation,
            recording = recording,
            savePoseData = savePoseData,
            skipExecution = skipExecution
        )
    }
}
