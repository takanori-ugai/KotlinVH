package com.fujitsu.labs.virtualhome

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.System.currentTimeMillis
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.abs

private val logger = KotlinLogging.logger {}

class VirtualHomeClient(host: String = "localhost", port: Int = 8080) {
    /**
     * Configuration of Json converter
     */
    private val format = Json {
        encodeDefaults = true
        explicitNulls = false
    }

    private val url = URL("http://$host:$port")

    /**
     * This action is not implemented
     *
     * fun checkScript(script: List<String>) : Response? {
     * val data = Request(currentTimeMillis().toInt(), "check_script", stringParams=script)
     * val res= sendRequest(format.encodeToString(data).toByteArray(Charsets.UTF_8))
     * return res
     * }
     */

    /**
     * Make images from cameras.
     * @param cameraIndexes Identifiers of the cameras.
     * @param mode The mode of image ("normal", .. )
     * @param imageWidth The width of the created image.
     * @param imageHeight The height of the created image.
     */
    fun cameraImage(
        cameraIndexes: List<Int>,
        mode: String = "normal",
        imageWidth: Int = 640,
        imageHeight: Int = 320
    ): VirtualHomeResponse? {
        val data = VirtualHomeRequest(
            abs(currentTimeMillis().toInt()),
            "camera_image",
            intParams = cameraIndexes,
            stringParams = listOf(
                format.encodeToString(
                    ImageConfig(
                        mode = mode,
                        imageWidth = imageWidth.toString(),
                        imageHeight = imageHeight.toString()
                    )
                )
            )
        )
//        logger.info { format.encodeToString(data) }
        return sendRequest(format.encodeToString(data).toByteArray(Charsets.UTF_8))
    }

    fun renderScript(script: List<String>, config: RenderParams = RenderParams()): VirtualHomeResponse? {
        val stringParams = mutableListOf(format.encodeToString(config))
        stringParams.addAll(script)
        val data = VirtualHomeRequest(abs(currentTimeMillis().toInt()), "render_script", stringParams = stringParams)
        return sendRequest(format.encodeToString(data).toByteArray(Charsets.UTF_8))
    }

    /**
     * Expand the scene with the graph.
     * @param config  Configuration
     * @param graph   The graph for expanding the scene.
     */
    fun expandScene(graph: Graph, config: ExpandSceneConfig = ExpandSceneConfig()): VirtualHomeResponse? {
        val stringParams = listOf(format.encodeToString(config), format.encodeToString(graph))
        val data = VirtualHomeRequest(abs(currentTimeMillis().toInt()), "expand_scene", stringParams = stringParams)
        return sendRequest(format.encodeToString(data).toByteArray(Charsets.UTF_8))
    }

    /**
     * Get the environment graph
     */
    fun environmentGraph(): Graph {
        val data = VirtualHomeRequest(abs(currentTimeMillis().toInt()), "environment_graph")
        val res = sendRequest(format.encodeToString(data).toByteArray(Charsets.UTF_8))
        if (res?.message != null) {
            return format.decodeFromString(res.message)
        }
        return Graph()
    }

    /**
     * Check the server's status
     */
    fun check(): VirtualHomeResponse? {
        val data = VirtualHomeRequest(abs(currentTimeMillis().toInt()), "idle")
        return sendRequest(format.encodeToString(data).toByteArray(Charsets.UTF_8))
    }

    /**
     * Reset the scene.
     * @param sceneIndex the index number of the scene. (default value is 0)
     */
    fun reset(sceneIndex: Int = 0): VirtualHomeResponse? {
        val data = VirtualHomeRequest(abs(currentTimeMillis().toInt()), "reset", listOf(sceneIndex))
        return sendRequest(format.encodeToString(data).toByteArray(Charsets.UTF_8))
//        val data = VirtualHomeRequest(abs(currentTimeMillis().toInt()), "clear", listOf(4))
//        println(format.encodeToString(data))
//        println(sendRequest(format.encodeToString(data).toByteArray(Charsets.UTF_8)))
//        val data0 = VirtualHomeRequest(abs(currentTimeMillis().toInt()), "environment", listOf(sceneIndex))
//        return sendRequest(format.encodeToString(data0).toByteArray(Charsets.UTF_8))
    }

    val initialRooms = listOf("kitchen", "bedroom", "livingroom", "bathroom")

    /**
     * Add a character.
     * @param characterResource The resource of character to add. (default value is "Chars/Male1"
     * @param initialRoom The palace to put the character, which must be kitchen, bedroom, livingroom or bathroom.
     */
    fun addCharacter(
        characterResource: String = "Chars/Male1",
        position: Position? = null,
        initialRoom: String = ""
    ): VirtualHomeResponse? {
        val addCharacterConfig =
            if (position != null) {
                AddCharacterConfig(
                    characterResource = characterResource,
                    mode = AddCharacterMode.FixPosition.toString(),
                    characterPosition = position
                )
            } else if (initialRooms.contains(initialRoom)) {
                AddCharacterConfig(
                    characterResource = characterResource,
                    mode = AddCharacterMode.FixPosition.toString(),
                    initialRoom = initialRoom
                )
            } else {
                AddCharacterConfig(
                    characterResource = characterResource,
                    mode = AddCharacterMode.Random.toString()
                )
            }
        val data = VirtualHomeRequest(
            abs(currentTimeMillis().toInt()),
            "add_character",
            stringParams = listOf(
                format.encodeToString(addCharacterConfig)
            )
        )
        return sendRequest(format.encodeToString(data).toByteArray(Charsets.UTF_8))
    }

    /**
     * Get the visible objects from a camera.
     * @param cameraIndex The index number of the camera.
     * @return The visible objects (map of id and the class name)
     */
    fun visibleObjects(cameraIndex: Int = 0): Map<String, String> {
        val data = VirtualHomeRequest(abs(currentTimeMillis().toInt()), "observation", listOf(cameraIndex))
        val res = sendRequest(format.encodeToString(data).toByteArray(Charsets.UTF_8))
        if (res != null && res.success && res.message != null) {
            return format.decodeFromString(res.message)
        }
        return mapOf()
    }

    /**
     * Returns the number of cameras in the scene, including static cameras, and cameras for each character.
     * @return The number of cameras in the scene, including static cameras, and cameras for each character.
     */
    fun cameraCount(): Int {
        val data = VirtualHomeRequest(abs(currentTimeMillis().toInt()), "camera_count")
        return sendRequest(format.encodeToString(data).toByteArray(Charsets.UTF_8))!!.value
    }

    private fun readStream(inputStream: InputStream): VirtualHomeResponse {
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val responseBody = bufferedReader.use { it.readText() }
        bufferedReader.close()
        return Json.decodeFromString(responseBody)
    }

    fun sendRequest(req: ByteArray): VirtualHomeResponse? {
        // HttpURLConnectionの作成
        var ret: VirtualHomeResponse? = null
        val connection = url.openConnection() as HttpURLConnection
        try {
            connection.connectTimeout = 30000
            connection.readTimeout = 60000
            connection.doOutput = true
            connection.setChunkedStreamingMode(0)
            connection.setRequestProperty("Content-type", "application/json; charset=utf-8")
            // Bodyの書き込み
            val outputStream = connection.outputStream
            outputStream.write(req)
            outputStream.flush()
            outputStream.close()

            // Responseの読み出し
            val statusCode = connection.responseCode
            if (statusCode == HttpURLConnection.HTTP_OK) {
                ret = readStream(connection.inputStream)
            }
        } catch (exception: Exception) {
            println("Error: $exception")
        } finally {
            connection.disconnect()
        }
        return ret
    }
}
