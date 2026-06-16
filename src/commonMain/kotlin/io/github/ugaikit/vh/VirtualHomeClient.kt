package io.github.ugaikit.vh

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

private val logger = KotlinLogging.logger {}

private const val CONNECT_TIMEOUT = 30000L
internal const val READ_TIMEOUT = 600000L

/**
 * A client for interacting with the VirtualHome server.
 *
 * @property host The host of the VirtualHome server. (Default value is "localhost")
 * @property port The port of the VirtualHome server. (Default value is 8080)
 */
open class VirtualHomeClient(
    host: String = "localhost",
    port: Int = 8080,
    timeout: Long = READ_TIMEOUT,
    httpClient: HttpClient? = null,
) : CloseableResource {
    constructor(host: String, port: Int, timeout: Long) : this(host, port, timeout, null)

    private val ownsClient = httpClient == null

    /**
     * Configuration of Json converter
     */
    private val format =
        Json {
            encodeDefaults = true
            @OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
            explicitNulls = false
        }

    private val url = "http://$host:$port"

    private val client = httpClient ?: buildHttpClient(timeout)
    private val initialRooms = listOf("kitchen", "bedroom", "livingroom", "bathroom")

    /**
     * Activates or deactivates physics in the environment.
     * @param active Whether to activate physics or not.
     */
    suspend fun activatePhysics(active: Boolean = true): VirtualHomeResponse {
        val data =
            VirtualHomeRequest(
                action = "activate_physics",
                stringParams = listOf(active.toString()),
            )
        return sendRequest(data)
    }

    /**
     * Checks the validity of a script.
     * @param script The script to check.
     * @return A VirtualHomeResponse indicating whether the script is valid.
     */
    suspend fun checkScript(script: List<String>): VirtualHomeResponse {
        val data = VirtualHomeRequest(action = "check_script", stringParams = script)
        return sendRequest(data)
    }

    /**
     * Releases the underlying HTTP client resources.
     *
     * Call this when the client is no longer needed (e.g., on application shutdown).
     * On JVM targets this also enables usage with `use { ... }` / try-with-resources.
     */
    override fun close() {
        if (ownsClient) {
            client.close()
        }
    }

    /**
     * Retrieves all objects in the scene.
     * @return A list of nodes representing the objects in the scene.
     */
    suspend fun getObjects(): List<Node> {
        val request = VirtualHomeRequest(action = "get_objects")
        val response = sendRequest(request)
        return try {
            if (!response.success || response.message == null) {
                emptyList()
            } else {
                format.decodeFromString(response.message)
            }
        } catch (e: SerializationException) {
            logger.error(e) { "Failed to parse getObjects response" }
            emptyList()
        }
    }

    /**
     * Make images from cameras.
     * @param cameraIndexes Identifiers of the cameras.
     * @param mode The mode of image ("normal", .. )
     * @param imageWidth The width of the created image.
     * @param imageHeight The height of the created image.
     */
    suspend fun cameraImage(
        cameraIndexes: List<Int>,
        mode: String = "normal",
        imageWidth: Int = 640,
        imageHeight: Int = 320,
    ): List<ByteArray> {
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
        return sendRequest(data).messageList?.map { decodeBase64(it) } ?: emptyList()
    }

    /**
     * Retrieves camera data for the specified camera indexes.
     *
     * @param cameraIndexes The list of camera indexes to retrieve data for.
     * @return The response containing the camera data.
     */
    suspend fun cameraData(cameraIndexes: List<Int>): VirtualHomeResponse {
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
    suspend fun renderScript(
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
    suspend fun expandScene(
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
    suspend fun addCamera(
        position: Position = Position(0, 1, 0),
        rotation: Position = Position(0, 0, 0),
        fieldView: Int = 40,
    ): VirtualHomeResponse {
        val camDict = CamDict(position, rotation, fieldView)
        val stringParams = listOf(format.encodeToString(camDict))
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
    suspend fun updateCamera(
        cameraIndex: Int,
        position: Position = Position(0, 1, 0),
        rotation: Position = Position(0, 0, 0),
        fieldView: Int = 40,
    ): VirtualHomeResponse {
        val stringParams = listOf(format.encodeToString(CamDict(position, rotation, fieldView)))
        val data =
            VirtualHomeRequest(
                action = "update_camera",
                intParams = listOf(cameraIndex),
                stringParams = stringParams,
            )
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
    suspend fun updateCharacterCamera(
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
    suspend fun environmentGraph(): Graph {
        val request = VirtualHomeRequest(action = "environment_graph")
        val response = sendRequest(request)
        return response.message?.let { format.decodeFromString(it) } ?: Graph()
    }

    /**
     * Check the server's status
     */
    suspend fun check(): VirtualHomeResponse = sendRequest(VirtualHomeRequest(action = "idle"))

    /**
     * Reset the scene.
     *
     * @param sceneIndex The index number of the scene (default value is 0).
     * @return A VirtualHomeResponse or null if the request fails.
     */
    suspend fun reset(sceneIndex: Int = 0): VirtualHomeResponse {
        val data = VirtualHomeRequest(action = "reset", intParams = listOf(sceneIndex))
        return sendRequest(data)
    }

    /**
     * Adds a character camera to the virtual environment with the specified position, rotation, and name.
     *
     * @param position The position of the camera in the virtual environment. Defaults to (0, 1, 0).
     * @param rotation The rotation of the camera in the virtual environment. Defaults to (0, 0, 0).
     * @param name The name assigned to the new camera. Defaults to "new_camera".
     * @return [VirtualHomeResponse] containing the response data from the virtual environment.
     */
    suspend fun addCharacterCamera(
        position: Position = Position(0, 1, 0),
        rotation: Position = Position(0, 0, 0),
        name: String = "new_camera",
    ): VirtualHomeResponse {
        val stringParams = listOf(format.encodeToString(CamDict(position, rotation, cameraName = name)))
        val data = VirtualHomeRequest(action = "add_character_camera", stringParams = stringParams)
        return sendRequest(data)
    }

    /**
     * Retrieves a list of indices of objects visible from the specified camera's viewpoint.  (since v2.3)
     *
     * @param cameraIndex The index of the camera in the virtual environment.
     * @return A list of object indices that are visible from the specified camera. Returns an empty list
     * if no objects are visible or if the response is null.
     */
    suspend fun getVisibleObjects(cameraIndex: Int): List<Int> {
        val data = VirtualHomeRequest(action = "get_visible_objects", intParams = listOf(cameraIndex))
        val res = sendRequest(data).message
        if (res != null) {
            return format.decodeFromString(res)
        } else {
            return emptyList()
        }
    }

    /**
     * Add a character.
     * @param characterResource The resource of character to add. (default value is "Chars/Male1"
     * @param initialRoom The palace to put the character, which must be kitchen, bedroom, livingroom or bathroom.
     */
    suspend fun addCharacter(
        characterResource: String = "Chars/Male1",
        position: Position? = null,
        initialRoom: String = "",
    ): VirtualHomeResponse {
        val addCharacterConfig =
            if (position != null) {
                AddCharacterConfig(
                    characterResource = characterResource,
                    mode = AddCharacterMode.FixPosition.value,
                    characterPosition = position,
                )
            } else if (initialRooms.contains(initialRoom)) {
                AddCharacterConfig(
                    characterResource = characterResource,
                    mode = AddCharacterMode.FixPosition.value,
                    initialRoom = initialRoom,
                )
            } else {
                AddCharacterConfig(
                    characterResource = characterResource,
                    mode = AddCharacterMode.Random.value,
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
    suspend fun visibleObjects(cameraIndex: Int = 0): Map<String, String> {
        val request = VirtualHomeRequest(action = "observation", intParams = listOf(cameraIndex))
        val response = sendRequest(request)
        if (!response.success || response.message == null) {
            return emptyMap()
        }
        return format.decodeFromString(response.message)
    }

    /**
     * Returns the number of cameras in the scene, including static cameras, and cameras for each character.
     * @return The number of cameras in the scene, including static cameras, and cameras for each character.
     */
    suspend fun cameraCount(): Int {
        val data = VirtualHomeRequest(action = "camera_count")
        return sendRequest(data).value
    }

    /**
     * Retrieves a list of character camera strings from a virtual home environment.
     *
     * This function sends a request to the virtual home system to get the current state
     * of character cameras. It expects a response in JSON format which is then decoded into
     * a list of strings.
     *
     * @return A list of character camera strings if the request is successful and the message is not null,
     *         otherwise an empty list. default values are ["PERSON_FRONT","PERSON_TOP","FIRST_PERSON",
     *         "PERSON_FROM_BACK","PERSON_FROM_LEFT","PERSON_RIGHT","PERSON_LEFT","PERSON_BACK"]
     */
    suspend fun characterCameras(): List<String> =
        sendRequest(VirtualHomeRequest(action = "character_cameras")).let { res ->
            if (res.success) {
                res.message?.let { format.decodeFromString<List<String>>(it) }.orEmpty()
            } else {
                emptyList()
            }
        }

    internal open fun buildHttpClient(timeout: Long): HttpClient = createHttpClient(timeout)

    private fun createHttpClient(timeout: Long): HttpClient =
        HttpClient {
            install(ContentNegotiation) {
                json(format)
            }
            install(HttpTimeout) {
                connectTimeoutMillis = CONNECT_TIMEOUT
                requestTimeoutMillis = timeout
            }
        }

    open suspend fun sendRequest(data: VirtualHomeRequest): VirtualHomeResponse {
        try {
            return client
                .post(url) {
                    contentType(ContentType.Application.Json)
                    setBody(data)
                }.body()
        } catch (exception: Exception) {
            println("Error: $exception")
            return VirtualHomeResponse(0, false, "$exception", 0, null)
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun decodeBase64(value: String): ByteArray = Base64.Default.decode(value)
}
