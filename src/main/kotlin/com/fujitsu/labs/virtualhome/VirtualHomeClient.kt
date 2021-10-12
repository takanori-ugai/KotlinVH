package com.fujitsu.labs.virtualhome

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.System.currentTimeMillis
import java.net.HttpURLConnection
import java.net.URL

class VirtualHomeClient(private val host: String = "localhost", private val port: Int = 8080) {
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

     fun checkScript(script: List<String>) : Response? {
     val data = Request(currentTimeMillis().toInt(), "check_script", stringParams=script)
     val res= sendRequest(format.encodeToString(data).toByteArray(Charsets.UTF_8))
     return res

     }
     */

    /**
     * Make images from cameras.
     * @param cameraIndexes Identifiers of the cameras.
     * @param mode The mode of image ("normal", .. )
     * @param image_width The width of the created image.
     * @param image_height The hight of the created image.
     */
    fun cameraImage(
        cameraIndexes: List<Int>,
        mode: String = "normal",
        image_width: Int = 640,
        image_height: Int = 320
    ): VirtualHomeResponse? {
        val data = VirtualHomeRequest(
            currentTimeMillis().toInt(),
            "camera_image",
            intParams = cameraIndexes,
            stringParams = listOf(
                format.encodeToString(
                    ImageConfig(
                        mode = mode,
                        image_width = image_width.toString(),
                        image_height = image_height.toString()
                    )
                )
            )
        )
        println(format.encodeToString(data))
        val res = sendRequest(format.encodeToString(data).toByteArray(Charsets.UTF_8))
        return res
    }

    fun renderScript(script: List<String>, config: RenderParams = RenderParams()): VirtualHomeResponse? {
        val stringParams = mutableListOf(format.encodeToString(config))
        stringParams.addAll(script)
        val data = VirtualHomeRequest(currentTimeMillis().toInt(), "render_script", stringParams = stringParams)
        return sendRequest(format.encodeToString(data).toByteArray(Charsets.UTF_8))
    }

    /**
     * Expand the scene with the graph.
     * @param config  Configuration
     * @param graph   The graph for expanding the scene.
     */
    fun expandScene(graph: Graph, config: ExpandSceneConfig = ExpandSceneConfig()): VirtualHomeResponse? {
        val stringParams = listOf(format.encodeToString(config), format.encodeToString(graph))
        val data = VirtualHomeRequest(currentTimeMillis().toInt(), "expand_scene", stringParams = stringParams)
        return sendRequest(format.encodeToString(data).toByteArray(Charsets.UTF_8))
    }

    /**
     * Get the environment graph
     */
    fun environmentGraph(): Graph {
        val data = VirtualHomeRequest(currentTimeMillis().toInt(), "environment_graph")
        var ret = Graph()
        val res = sendRequest(format.encodeToString(data).toByteArray(Charsets.UTF_8))
        if (res?.message != null) {
            ret = format.decodeFromString<Graph>(res.message)
        }
        return ret
    }

    /**
     * Check the server's status
     */
    fun check(): VirtualHomeResponse? {
        val data = VirtualHomeRequest(currentTimeMillis().toInt(), "idle")
        return sendRequest(format.encodeToString(data).toByteArray(Charsets.UTF_8))
    }

    /**
     * Reset the scene.
     * @param sceneIndex the index number of the scene. (default value is 0)
     */
    fun reset(sceneIndex: Int = 0): VirtualHomeResponse? {
        val data = VirtualHomeRequest(currentTimeMillis().toInt(), "reset", listOf(sceneIndex))
        return sendRequest(format.encodeToString(data).toByteArray(Charsets.UTF_8))
    }

    /**
     * Add a character.
     * @param characterResource The resource of character to add. (default value is "Chars/Male1"
     */
    fun addCharacter(characterResource: String = "Chars/Male1", mode: String = "random"): VirtualHomeResponse? {
        val data = VirtualHomeRequest(
            currentTimeMillis().toInt(),
            "add_character",
            stringParams = listOf(
                format.encodeToString(
                    AddCharacterConfig(
                        character_resource = characterResource,
                        mode = mode
                    )
                )
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
        val data = VirtualHomeRequest(currentTimeMillis().toInt(), "observation", listOf(cameraIndex))
        val res = sendRequest(format.encodeToString(data).toByteArray(Charsets.UTF_8))
        if (res != null && res.success && res.message != null) {
            return format.decodeFromString<Map<String, String>>(res.message)
        }
        return mapOf()
    }

    private fun readStream(inputStream: InputStream): VirtualHomeResponse {
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val responseBody = bufferedReader.use { it.readText() }
        bufferedReader.close()
        return Json.decodeFromString<VirtualHomeResponse>(responseBody)
    }

    fun sendRequest(req: ByteArray): VirtualHomeResponse? {
        // HttpURLConnectionの作成
        var ret: VirtualHomeResponse? = null
        val connection = url.openConnection() as HttpURLConnection
        try {
            connection.connectTimeout = 30000
            connection.readTimeout = 30000
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
            println("Error: " + exception.toString())
        } finally {
            connection.disconnect()
        }
        return ret
    }
}
