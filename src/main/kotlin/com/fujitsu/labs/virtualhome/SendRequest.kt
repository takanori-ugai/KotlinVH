package com.fujitsu.labs.virtualhome

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.lang.System.currentTimeMillis

class SendRequest(val host: String = "localhost", val port: Int = 8080) {
    /**
     * Configuration of Json converter
     */
    val format = Json {
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

    fun cameraImage(cameraIndexes: List<Int>, mode: String = "normal",
                    image_width : Int= 640, image_height: Int =320) : Response? {
        val data = Request(currentTimeMillis().toInt(), "camera_image",
            intParams= cameraIndexes,stringParams=listOf(format.encodeToString(ImageConfig())))
        println(format.encodeToString(data))
        val res= sendRequest(format.encodeToString(data).toByteArray(Charsets.UTF_8))
        return res
    }

    fun renderScript(script: List<String>, config: RenderParams = RenderParams()): Response? {
        val stringParams = mutableListOf(format.encodeToString(config))
        stringParams.addAll(script)
        val data = Request(currentTimeMillis().toInt(), "render_script", stringParams = stringParams)
        return sendRequest(format.encodeToString(data).toByteArray(Charsets.UTF_8))
    }

    /**
     * Expand the scene with the graph.
     * param config  Configuration
     * param graph   The graph for expanding the scene.
     */
    fun expandScene(graph: Graph, config: Config = Config()): Response? {
        val stringParams = listOf(format.encodeToString(config), format.encodeToString(graph))
        val data = Request(currentTimeMillis().toInt(), "expand_scene", stringParams = stringParams)
        return sendRequest(format.encodeToString(data).toByteArray(Charsets.UTF_8))
    }

    /**
     * Get the environment graph
     */
    fun environmentGraph(): Graph {
        val data = Request(currentTimeMillis().toInt(), "environment_graph")
        var ret: Graph = Graph()
        val res = sendRequest(format.encodeToString(data).toByteArray(Charsets.UTF_8))
        if (res?.message != null) {
            ret = format.decodeFromString<Graph>(res.message)
        }
        return ret
    }

    /**
     * Check the server's status
     */
    fun check(): Response? {
        val data = Request(currentTimeMillis().toInt(), "idle")
        return sendRequest(format.encodeToString(data).toByteArray(Charsets.UTF_8))
    }

    /**
     * Reset the scene.
     * param sceneIndex the index number of the scene.
     */
    fun reset(sceneIndex: Int = 0): Response? {
        val data = Request(currentTimeMillis().toInt(), "reset", listOf(sceneIndex))
        return sendRequest(format.encodeToString(data).toByteArray(Charsets.UTF_8))
    }

    /**
     * Add a character.
     * param characterResource The resource of character to add.
     */
    fun addCharacter(characterResource: String = "Chars/Male1", mode: String = "random"): Response? {
        val data = Request(
            currentTimeMillis().toInt(),
            "add_character",
            stringParams = listOf(
                format.encodeToString(
                    AddCharacter(
                        character_resource = characterResource,
                        mode = mode
                    )
                )
            )
        )
        return sendRequest(format.encodeToString(data).toByteArray(Charsets.UTF_8))
    }

    fun getVisibleObjects(cameraIndex: Int = 0): Response? {
        val data = Request(currentTimeMillis().toInt(), "observation", listOf(cameraIndex))
        return sendRequest(format.encodeToString(data).toByteArray(Charsets.UTF_8))
    }

    private fun readStream(inputStream: InputStream): Response {
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val responseBody = bufferedReader.use { it.readText() }
        bufferedReader.close()
        return Json.decodeFromString<Response>(responseBody)
    }

    fun sendRequest(req: ByteArray): Response? {
        // HttpURLConnectionの作成
        var ret: Response? = null
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
