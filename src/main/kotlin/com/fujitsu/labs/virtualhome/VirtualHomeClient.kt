package com.fujitsu.labs.virtualhome

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

private val logger = KotlinLogging.logger {}

/**
 * A client for interacting with the VirtualHome server.
 *
 * @property host The host of the VirtualHome server. (Default value is "localhost")
 * @property port The port of the VirtualHome server. (Default value is 8080)
 */
class VirtualHomeClient(host: String = "localhost", port: Int = 8080) {
    /**
     * Configuration of Json converter
     */
    private val format =
        Json {
            encodeDefaults = true
            @ExperimentalSerializationApi
            explicitNulls = false
        }

    private val url = URL("http://$host:$port")
    private val initialRooms = listOf("kitchen", "bedroom", "livingroom", "bathroom")

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
        imageHeight: Int = 320,
    ): VirtualHomeResponse {
        val data =
            VirtualHomeRequest(
                action = "camera_image",
                intParams = cameraIndexes,
                stringParams =
                    listOf(
                        format.encodeToString(
                            ImageConfig(
                                mode = mode,
                                imageWidth = imageWidth.toString(),
                                imageHeight = imageHeight.toString(),
                            ),
                        ),
                    ),
            )
//        logger.info { format.encodeToString(data) }
        return sendRequest(data)
    }

    /**
     * Retrieves camera data for the specified camera indexes.
     *
     * @param cameraIndexes The list of camera indexes to retrieve data for.
     * @return The response containing the camera data.
     */
    fun cameraData(cameraIndexes: List<Int>): VirtualHomeResponse {
        val data =
            VirtualHomeRequest(
                action = "camera_data",
                intParams = cameraIndexes,
            )
//        logger.info { format.encodeToString(data) }
        return sendRequest(data)
    }

    /**
     * Renders a script with the given list of strings.
     *
     * @param script The list of strings representing the script to render.
     * @param config The RenderParams configuration (optional).
     * @return A VirtualHomeResponse or null if the request fails.
     */
    fun renderScript(
        script: List<String>,
        config: RenderParams = RenderParams(),
    ): VirtualHomeResponse {
        val stringParams = mutableListOf(format.encodeToString(config))
        stringParams.addAll(script)
        val data = VirtualHomeRequest(action = "render_script", stringParams = stringParams)
        return sendRequest(data)
    }

    /**
     * Expand the scene with the graph.
     *
     * @param config  Configuration
     * @param graph   The graph for expanding the scene.
     */
    fun expandScene(
        graph: Graph,
        config: ExpandSceneConfig = ExpandSceneConfig(),
    ): VirtualHomeResponse {
        val stringParams = listOf(format.encodeToString(config), format.encodeToString(graph))
        val data = VirtualHomeRequest(action = "expand_scene", stringParams = stringParams)
        return sendRequest(data)
    }

    /**
     * This function is used to add a camera to the virtual home environment.
     *
     * @param position The position of the camera in the 3D space. Default value is Position(0,1,0).
     * @param rotation The rotation of the camera in the 3D space. Default value is Position(0,0,0).
     * @param fieldView The field of view of the camera. Default value is 40.
     *
     * @return VirtualHomeResponse Returns the response from the virtual home after adding the camera.
     */
    fun addCamera(
        position: Position = Position(0, 1, 0),
        rotation: Position = Position(0, 0, 0),
        fieldView: Int = 40,
    ): VirtualHomeResponse {
        val stringParams = listOf(format.encodeToString(CamDict(position, rotation, fieldView)))
        val data = VirtualHomeRequest(action = "add_camera", stringParams = stringParams)
        return sendRequest(data)
    }

    /**
     * Updates the camera in the virtual home environment.
     *
     * @param cameraIndex The index of the camera to update.
     * @param position The new position of the camera. Default is Position(0, 1, 0).
     * @param rotation The new rotation of the camera. Default is Position(0, 0, 0).
     * @param fieldView The new field of view of the camera. Default is 40.
     * @return The response from the virtual home environment.
     */
    fun updateCamera(
        cameraIndex: Int,
        position: Position = Position(0, 1, 0),
        rotation: Position = Position(0, 0, 0),
        fieldView: Int = 40,
    ): VirtualHomeResponse {
        val stringParams = listOf(format.encodeToString(CamDict(position, rotation, fieldView)))
        val data = VirtualHomeRequest(action = "update_camera", intParams = listOf(cameraIndex), stringParams = stringParams)
        return sendRequest(data)
    }

    /**
     * Updates the character's camera in the virtual home environment.
     *
     * @param position The new position of the camera. Default is Position(0, 1, 0).
     * @param rotation The new rotation of the camera. Default is Position(0, 0, 0).
     * @param fieldView The new field of view of the camera. Default is 60.
     * @param name The name of the character. Default is "PERSON_FRONT".
     * @return The response from the virtual home environment.
     */
    fun updateCharacterCamera(
        position: Position = Position(0, 1, 0),
        rotation: Position = Position(0, 0, 0),
        fieldView: Int = 60,
        name: String = "PERSON_FRONT",
    ): VirtualHomeResponse {
        val stringParams = listOf(format.encodeToString(CamDict(position, rotation, fieldView, name)))
        val data = VirtualHomeRequest(action = "update_character_camera", stringParams = stringParams)
        return sendRequest(data)
    }

    /**
     * Get the environment graph
     */
    fun environmentGraph(): Graph {
        val data = VirtualHomeRequest(action = "environment_graph")
        val res = sendRequest(data)
        if (res.message != null) {
            return format.decodeFromString(res.message)
        }
        return Graph()
    }

    /**
     * Check the server's status
     */
    fun check(): VirtualHomeResponse {
        val data = VirtualHomeRequest(action = "idle")
        return sendRequest(data)
    }

    /**
     * Reset the scene.
     *
     * @param sceneIndex The index number of the scene (default value is 0).
     * @return A VirtualHomeResponse or null if the request fails.
     */
    fun reset(sceneIndex: Int = 0): VirtualHomeResponse {
        val data = VirtualHomeRequest(action = "reset", intParams = listOf(sceneIndex))
        return sendRequest(data)
    }

    /**
     * Add a character.
     * @param characterResource The resource of character to add. (default value is "Chars/Male1"
     * @param initialRoom The palace to put the character, which must be kitchen, bedroom, livingroom or bathroom.
     */
    fun addCharacter(
        characterResource: String = "Chars/Male1",
        position: Position? = null,
        initialRoom: String = "",
    ): VirtualHomeResponse {
        val addCharacterConfig =
            if (position != null) {
                AddCharacterConfig(
                    characterResource = characterResource,
                    mode = AddCharacterMode.FixPosition.toString(),
                    characterPosition = position,
                )
            } else if (initialRooms.contains(initialRoom)) {
                AddCharacterConfig(
                    characterResource = characterResource,
                    mode = AddCharacterMode.FixPosition.toString(),
                    initialRoom = initialRoom,
                )
            } else {
                AddCharacterConfig(
                    characterResource = characterResource,
                    mode = AddCharacterMode.Random.toString(),
                )
            }
        val data =
            VirtualHomeRequest(
                action = "add_character",
                stringParams =
                    listOf(
                        format.encodeToString(addCharacterConfig),
                    ),
            )
        return sendRequest(data)
    }

    /**
     * Get the visible objects from a camera.
     * @param cameraIndex The index number of the camera.
     * @return The visible objects (map of id and the class name)
     */
    fun visibleObjects(cameraIndex: Int = 0): Map<String, String> {
        val data = VirtualHomeRequest(action = "observation", intParams = listOf(cameraIndex))
        val res = sendRequest(data)
        if (res.success && res.message != null) {
            return format.decodeFromString(res.message)
        }
        return mapOf()
    }

    /**
     * Returns the number of cameras in the scene, including static cameras, and cameras for each character.
     * @return The number of cameras in the scene, including static cameras, and cameras for each character.
     */
    fun cameraCount(): Int {
        val data = VirtualHomeRequest(action = "camera_count")
        return sendRequest(data).value
    }

    private fun readStream(inputStream: InputStream): VirtualHomeResponse {
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val responseBody = bufferedReader.use { it.readText() }
        bufferedReader.close()
        return Json.decodeFromString(responseBody)
    }

    fun sendRequest(data: VirtualHomeRequest): VirtualHomeResponse {
        return sendRequest(format.encodeToString(data).toByteArray(Charsets.UTF_8))
    }

    /**
     * Sends a request to the VirtualHome server.
     *
     * @param req The request to send as a byte array.
     * @return A VirtualHomeResponse or null if the request fails.
     */
    fun sendRequest(req: ByteArray): VirtualHomeResponse {
        // HttpURLConnectionの作成
        var ret = VirtualHomeResponse(0, false, "", 0, null)
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
            return VirtualHomeResponse(0, false, "$exception", 0, null)
        } finally {
            connection.disconnect()
        }
        return ret
    }
}
