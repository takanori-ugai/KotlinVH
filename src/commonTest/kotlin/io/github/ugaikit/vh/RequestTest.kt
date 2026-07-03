package io.github.ugaikit.vh

import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * This class contains tests for the VirtualHomeClient's request methods.
 */
@OptIn(ExperimentalSerializationApi::class, ExperimentalEncodingApi::class)
class RequestTest {
    /**
     * Configuration of Json converter
     */
    @ExperimentalSerializationApi
    private val format =
        Json {
            encodeDefaults = true
            explicitNulls = false
        }

    internal class SpyVirtualHomeClient : VirtualHomeClient() {
        var handler: ((VirtualHomeRequest) -> VirtualHomeResponse)? = null

        override suspend fun sendRequest(data: VirtualHomeRequest): VirtualHomeResponse =
            handler?.invoke(data) ?: VirtualHomeResponse(0, false, "Not mocked", 0, null)
    }

    private lateinit var vh: SpyVirtualHomeClient

    @BeforeTest
    fun setup() {
        vh = SpyVirtualHomeClient()
    }

    /**
     * Tests the check method of the VirtualHomeClient class.
     */
    @Test
    @ExperimentalSerializationApi
    fun checkTest() =
        runTest {
            val res = VirtualHomeResponse(1, true, "test", 1, listOf("Test"))
            vh.handler = { req ->
                if (req.action == "idle") res else VirtualHomeResponse(0, false, "Wrong action", 0, null)
            }

            assertTrue(vh.check().success)
        }

    /**
     * Tests the reset method of the VirtualHomeClient class.
     */
    @Test
    fun resetTest() =
        runTest {
            val sceneIndex = 0
            val res = VirtualHomeResponse(1, true, "test", 1, listOf("Test"))
            vh.handler = { req ->
                if (req.action == "reset" && req.intParams == listOf(sceneIndex)) {
                    res
                } else {
                    VirtualHomeResponse(0, false, "Wrong request", 0, null)
                }
            }

            assertTrue(vh.reset(sceneIndex).success)
            assertTrue(vh.reset().success)
        }

    /**
     * Tests the cameraCount method of the VirtualHomeClient class.
     */
    @Test
    fun cameraCountTest() =
        runTest {
            val value = 1
            val res = VirtualHomeResponse(1, true, "test", value, listOf("Test"))
            vh.handler = { req ->
                if (req.action == "camera_count") res else VirtualHomeResponse(0, false, "Wrong action", 0, null)
            }

            assertEquals(value, vh.cameraCount())
        }

    @Test
    fun addCharacterCameraTest() =
        runTest {
            val value = 1
            val res = VirtualHomeResponse(1, true, "test", value, listOf("Test"))
            vh.handler = { req ->
                if (req.action == "add_character_camera") res else VirtualHomeResponse(0, false, "Wrong action", 0, null)
            }

            assertEquals(true, vh.addCharacterCamera().success)

            val customPosition = Position(1, 2, 3)
            val customRotation = Position(4, 5, 6)
            val customName = "custom_camera"
            assertEquals(true, vh.addCharacterCamera(customPosition, customRotation, customName).success)
        }

    @Test
    fun addCameraTest() =
        runTest {
            val value = 1
            val res = VirtualHomeResponse(1, true, "test", value, listOf("Test"))
            vh.handler = { req ->
                if (req.action == "add_camera") res else VirtualHomeResponse(0, false, "Wrong action", 0, null)
            }

            assertEquals(true, vh.addCamera().success)

            val customPosition = Position(1, 2, 3)
            val customRotation = Position(4, 5, 6)
            assertEquals(true, vh.addCamera(customPosition, customRotation, 50).success)
        }

    @Test
    fun getVisibleObjectsTest() =
        runTest {
            val value = 1
            val res = VirtualHomeResponse(1, true, "[1,2,3]", value, listOf("Test"))
            vh.handler = { req ->
                if (req.action == "get_visible_objects" && req.intParams == listOf(1)) {
                    res
                } else {
                    VirtualHomeResponse(0, false, "Wrong request", 0, null)
                }
            }

            assertEquals(listOf(1, 2, 3), vh.getVisibleObjects(1))
        }

    @Test
    fun characterCamerasTest() =
        runTest {
            val value = 1
            val res = VirtualHomeResponse(1, true, """["1","2","3"]""", value, listOf("Test"))
            vh.handler = { req ->
                if (req.action == "character_cameras") res else VirtualHomeResponse(0, false, "Wrong action", 0, null)
            }

            assertEquals(listOf("1", "2", "3"), vh.characterCameras())
        }

    @Test
    fun getVisibleObjectsTest2() =
        runTest {
            val value = 1
            val res = VirtualHomeResponse(1, true, null, value, listOf("Test"))
            vh.handler = { req ->
                if (req.action == "get_visible_objects") res else VirtualHomeResponse(0, false, "Wrong action", 0, null)
            }

            assertEquals(emptyList<Int>(), vh.getVisibleObjects(1))
        }

    /**
     * Tests the error handling of the VirtualHomeClient class.
     */
    @Test
    fun errorTest() =
        runTest {
            val failureRes = VirtualHomeResponse(0, false, "Error", 0, null)
            vh.handler = { failureRes }

            assertFalse(vh.check().success)
        }

    /**
     * Tests the visibleObjects method of the VirtualHomeClient class.
     */
    @Test
    fun visibleObjectTest() =
        runTest {
            val value = 1
            val map = mapOf("0" to "1")
            val res = VirtualHomeResponse(1, true, format.encodeToString(map), value, listOf("Test"))

            vh.handler = { req ->
                if (req.action == "observation" && req.intParams == listOf(value)) {
                    res
                } else {
                    VirtualHomeResponse(0, false, "Wrong request", 0, null)
                }
            }

            assertEquals(map, vh.visibleObjects(value))
        }

    @Test
    fun `returns empty map when response success is false`() =
        runTest {
            val res =
                VirtualHomeResponse(
                    id = 1,
                    success = false,
                    message = """{"0":"1"}""",
                    value = 1,
                    messageList = listOf("Test"),
                )
            vh.handler = { req ->
                if (req.action == "observation") res else VirtualHomeResponse(0, false, "Wrong action", 0, null)
            }

            val result = vh.visibleObjects(0)
            assertEquals(emptyMap<String, String>(), result)
        }

    @Test
    fun `returns empty map when response success is false and message is null`() =
        runTest {
            val res =
                VirtualHomeResponse(
                    id = 1,
                    success = false,
                    message = null,
                    value = 1,
                    messageList = listOf("Test"),
                )
            vh.handler = { req ->
                if (req.action == "observation") res else VirtualHomeResponse(0, false, "Wrong action", 0, null)
            }

            val result = vh.visibleObjects(0)
            assertEquals(emptyMap<String, String>(), result)
        }

    @Test
    fun `returns empty map when response success is true and message is null`() =
        runTest {
            val res =
                VirtualHomeResponse(
                    id = 1,
                    success = true,
                    message = null,
                    value = 1,
                    messageList = listOf("Test"),
                )
            vh.handler = { req ->
                if (req.action == "observation") res else VirtualHomeResponse(0, false, "Wrong action", 0, null)
            }

            val result = vh.visibleObjects(0)
            assertEquals(emptyMap<String, String>(), result)
        }

    @Test
    fun updateCharacterCameraTest() =
        runTest {
            val value = 1
            val res = VirtualHomeResponse(1, true, "test", value, listOf("Test"))
            vh.handler = { req ->
                if (req.action == "update_character_camera") res else VirtualHomeResponse(0, false, "Wrong action", 0, null)
            }

            assertEquals(true, vh.updateCharacterCamera().success)

            val customPosition = Position(1, 2, 3)
            val customRotation = Position(4, 5, 6)
            val customName = "custom_camera"
            assertEquals(true, vh.updateCharacterCamera(customPosition, customRotation, 1, customName).success)
        }

    @Test
    fun updateCameraTest() =
        runTest {
            val value = 1
            val res = VirtualHomeResponse(1, true, "test", value, listOf("Test"))
            vh.handler = { req ->
                if (req.action == "update_camera") res else VirtualHomeResponse(0, false, "Wrong action", 0, null)
            }

            assertEquals(true, vh.updateCamera(1).success)

            val customPosition = Position(1, 2, 3)
            val customRotation = Position(4, 5, 6)
            assertEquals(true, vh.updateCamera(1, customPosition, customRotation, 1).success)
        }

    @Test
    fun addCharacterTest() =
        runTest {
            val value = 1
            val res = VirtualHomeResponse(1, true, "test", value, listOf("Test"))
            vh.handler = { req ->
                if (req.action == "add_character") res else VirtualHomeResponse(0, false, "Wrong action", 0, null)
            }

            assertEquals(true, vh.addCharacter().success)
            assertEquals(true, vh.addCharacter("Chars/Female1").success)
            val customPosition = Position(1, 2, 3)
            assertEquals(true, vh.addCharacter("Chars/Female1", customPosition).success)
            assertEquals(true, vh.addCharacter("Chars/Female1", initialRoom = "kitchen").success)
            assertEquals(true, vh.addCharacter("Chars/Female1", initialRoom = "InitialRoom").success)
        }

    @Test
    fun expandtSceneTest() =
        runTest {
            val value = 1
            val res = VirtualHomeResponse(1, true, format.encodeToString(Graph()), value, listOf("Test"))
            vh.handler = { req ->
                if (req.action == "expand_scene") res else VirtualHomeResponse(0, false, "Wrong action", 0, null)
            }

            assertEquals(true, vh.expandScene(Graph()).success)
            assertEquals(true, vh.expandScene(Graph(), ExpandSceneConfig()).success)
        }

    @Test
    fun environmentGraphTest() =
        runTest {
            val value = 1
            val res = VirtualHomeResponse(1, true, format.encodeToString(Graph()), value, listOf("Test"))
            vh.handler = { req ->
                if (req.action == "environment_graph") res else VirtualHomeResponse(0, false, "Wrong action", 0, null)
            }

            assertEquals(Graph(), vh.environmentGraph())
        }

    @Test
    fun cameraImageTest() =
        runTest {
            val value = 1
            val res = VirtualHomeResponse(1, true, "test", value, listOf("Test"))
            vh.handler = { req ->
                if (req.action == "camera_image") res else VirtualHomeResponse(0, false, "Wrong action", 0, null)
            }

            // vh.cameraImage returns List<ByteArray> by calling decodeBase64Bytes on each item in messageList.
            // messageList is listOf("Test"). "Test" decoded is some bytes.
            // We verify by encoding it back to string using Base64.
            val bytes = vh.cameraImage(listOf(1))[0]
            assertEquals("Test", Base64.Default.encode(bytes))
        }

    @Test
    fun cameraDataTest() =
        runTest {
            val value = 1
            val cameraIndexes = listOf(1, 2)
            val res = VirtualHomeResponse(1, true, "Test", value, listOf("Test"))
            vh.handler = { req ->
                if (req.action == "camera_data" && req.intParams == cameraIndexes) {
                    res
                } else {
                    VirtualHomeResponse(0, false, "Wrong request", 0, null)
                }
            }

            assertTrue(vh.cameraData(cameraIndexes).success)
        }

    @Test
    fun `renderScript sends correct request and returns response`() =
        runTest {
            val script = listOf("action1", "action2")
            val config = RenderParams(skipAnimation = false)
            val expectedResponse = VirtualHomeResponse(0, true, "ok", 0, listOf("result"))
            vh.handler = { req ->
                if (req.action == "render_script") {
                    val payload = req.stringParams?.firstOrNull() ?: ""
                    assertTrue(payload.contains("\"skip_animation\":false"))
                    assertTrue(payload.contains("\"save_scene_states\":false"))
                    expectedResponse
                } else {
                    VirtualHomeResponse(0, false, "Wrong action", 0, null)
                }
            }

            val response = vh.renderScript(script, config)
            assertEquals(expectedResponse, response)
        }

    @Test
    fun `renderScript works with default config`() =
        runTest {
            val script = listOf("foo")
            val expectedResponse = VirtualHomeResponse(1, true, "default", 0, listOf("bar"))
            vh.handler = { req ->
                if (req.action == "render_script") expectedResponse else VirtualHomeResponse(0, false, "Wrong action", 0, null)
            }

            val response = vh.renderScript(script)
            assertEquals(expectedResponse, response)
        }

    @Test
    fun `renderScript handles empty script`() =
        runTest {
            val script = emptyList<String>()
            val expectedResponse = VirtualHomeResponse(2, true, "empty", 0, emptyList())
            vh.handler = { req ->
                if (req.action == "render_script") expectedResponse else VirtualHomeResponse(0, false, "Wrong action", 0, null)
            }

            val response = vh.renderScript(script)
            assertEquals(expectedResponse, response)
        }

    @Test
    fun `renderScript throws or returns error on server error`() =
        runTest {
            val failureRes = VirtualHomeResponse(0, false, "Server Error", 0, null)
            vh.handler = { failureRes }

            assertFalse(vh.renderScript(listOf("fail")).success)
        }
}
