package com.fujitsu.labs.virtualhome

import com.github.tomakehurst.wiremock.client.WireMock.matchingJsonPath
import com.github.tomakehurst.wiremock.client.WireMock.okJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.serverError
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.Base64

/**
 * This class contains tests for the VirtualHomeClient's request methods.
 */
@WireMockTest
@ExperimentalSerializationApi
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

    private lateinit var vh: VirtualHomeClient

    @BeforeEach
    fun setup(info: WireMockRuntimeInfo) {
        val port = info.httpPort
        vh = VirtualHomeClient(port = port)
    }

    /**
     * Tests the check method of the VirtualHomeClient class.
     */
    @Test
    @ExperimentalSerializationApi
    fun checkTest() {
        val res = VirtualHomeResponse(1, true, "test", 1, listOf("Test"))
        stubFor(
            post("/")
                .withRequestBody(matchingJsonPath("$[?(@.action == 'idle')]"))
                .willReturn(okJson(format.encodeToString(res))),
        )
        assertTrue(vh.check().success)
    }

    /**
     * Tests the reset method of the VirtualHomeClient class.
     */
    @Test
    fun resetTest() {
        val sceneIndex = 0
        val res = VirtualHomeResponse(1, true, "test", 1, listOf("Test"))
        stubFor(
            post("/")
                .withRequestBody(
                    matchingJsonPath("$[?(@.action == 'reset')]")
                        .and(matchingJsonPath("$[?(@.intParams[0] == $sceneIndex)]")),
                ).willReturn(okJson(format.encodeToString(res))),
        )
        assertTrue(vh.reset(sceneIndex).success)
        assertTrue(vh.reset().success)
    }

    /**
     * Tests the cameraCount method of the VirtualHomeClient class.
     */
    @Test
    fun cameraCountTest() {
        val value = 1
        val res = VirtualHomeResponse(1, true, "test", value, listOf("Test"))
        stubFor(
            post("/")
                .withRequestBody(matchingJsonPath("$[?(@.action == 'camera_count')]"))
                .willReturn(okJson(format.encodeToString(res))),
        )
        assertEquals(value, vh.cameraCount())
    }

    @Test
    fun addCharacterCameraTest() {
        val value = 1
        val res = VirtualHomeResponse(1, true, "test", value, listOf("Test"))
        stubFor(
            post("/")
                .withRequestBody(matchingJsonPath("$[?(@.action == 'add_character_camera')]"))
                .willReturn(okJson(format.encodeToString(res))),
        )
        assertEquals(true, vh.addCharacterCamera().success)

        val customPosition = Position(1, 2, 3)
        val customRotation = Position(4, 5, 6)
        val customName = "custom_camera"
        assertEquals(true, vh.addCharacterCamera(customPosition, customRotation, customName).success)
    }

    @Test
    fun addCameraTest() {
        val value = 1
        val res = VirtualHomeResponse(1, true, "test", value, listOf("Test"))
        stubFor(
            post("/")
                .withRequestBody(matchingJsonPath("$[?(@.action == 'add_camera')]"))
                .willReturn(okJson(format.encodeToString(res))),
        )
        assertEquals(true, vh.addCamera().success)

        val customPosition = Position(1, 2, 3)
        val customRotation = Position(4, 5, 6)
        assertEquals(true, vh.addCamera(customPosition, customRotation, 50).success)
    }

    @Test
    fun getVisibleObjectsTest() {
        val value = 1
        val res = VirtualHomeResponse(1, true, "[1,2,3]", value, listOf("Test"))
        stubFor(
            post("/")
                .withRequestBody(matchingJsonPath("$[?(@.action == 'get_visible_objects')]"))
                .willReturn(okJson(format.encodeToString(res))),
        )
        assertEquals(listOf(1, 2, 3), vh.getVisibleObjects(1))
    }

    @Test
    fun characterCamerasTest() {
        val value = 1
        val res = VirtualHomeResponse(1, true, """["1","2","3"]""", value, listOf("Test"))
        stubFor(
            post("/")
                .withRequestBody(matchingJsonPath("$[?(@.action == 'character_cameras')]"))
                .willReturn(okJson(format.encodeToString(res))),
        )
        assertEquals(listOf("1", "2", "3"), vh.characterCameras())
    }

    @Test
    fun getVisibleObjectsTest2() {
        val value = 1
        val res = VirtualHomeResponse(1, true, null, value, listOf("Test"))
        println(Json { encodeDefaults = true }.encodeToString(res))
        stubFor(
            post("/")
                .withRequestBody(matchingJsonPath("$[?(@.action == 'get_visible_objects')]"))
                .willReturn(okJson(Json { encodeDefaults = true }.encodeToString(res))),
        )
        assertEquals(emptyList<Int>(), vh.getVisibleObjects(1))
    }

    /**
     * Tests the error handling of the VirtualHomeClient class.
     */
    @Test
    fun errorTest(info: WireMockRuntimeInfo) {
        val vh = VirtualHomeClient(port = info.httpPort + 100)
        assertFalse(vh.check().success)
    }

    /**
     * Tests the visibleObjects method of the VirtualHomeClient class.
     */
    @Test
    fun visibleObjectTest() {
        val value = 1
        val map = mapOf("0" to "1")
        val res = VirtualHomeResponse(1, true, format.encodeToString(map), value, listOf("Test"))
        stubFor(
            post("/")
                .withRequestBody(
                    matchingJsonPath("$[?(@.action == 'observation')]")
                        .and(matchingJsonPath("$[?(@.intParams[0] == $value)]")),
                ).willReturn(okJson(format.encodeToString(res))),
        )
        assertEquals(map, vh.visibleObjects(value))
    }

    @Test
    fun `returns empty map when response success is false`() {
        val res =
            VirtualHomeResponse(
                id = 1,
                success = false,
                message = """{"0":"1"}""",
                value = 1,
                messageList = listOf("Test"),
            )
        stubFor(
            post("/")
                .withRequestBody(matchingJsonPath("$[?(@.action == 'observation')]"))
                .willReturn(okJson(format.encodeToString(res))),
        )
        val result = vh.visibleObjects(0)
        assertEquals(emptyMap<String, String>(), result)
    }

    @Test
    fun `returns empty map when response success is false and message is null`() {
        val res =
            VirtualHomeResponse(
                id = 1,
                success = false,
                message = null,
                value = 1,
                messageList = listOf("Test"),
            )
        stubFor(
            post("/")
                .withRequestBody(matchingJsonPath("$[?(@.action == 'observation')]"))
                .willReturn(okJson(format.encodeToString(res))),
        )
        val result = vh.visibleObjects(0)
        assertEquals(emptyMap<String, String>(), result)
    }

    @Test
    fun `returns empty map when response success is true and message is null`() {
        val res =
            VirtualHomeResponse(
                id = 1,
                success = true,
                message = null,
                value = 1,
                messageList = listOf("Test"),
            )
        stubFor(
            post("/")
                .withRequestBody(matchingJsonPath("$[?(@.action == 'observation')]"))
                .willReturn(okJson(Json { encodeDefaults = true }.encodeToString(res))),
        )
        val result = vh.visibleObjects(0)
        assertEquals(emptyMap<String, String>(), result)
    }

    @Test
    fun updateCharacterCameraTest() {
        val value = 1
        val res = VirtualHomeResponse(1, true, "test", value, listOf("Test"))
        stubFor(
            post("/")
                .withRequestBody(matchingJsonPath("$[?(@.action == 'update_character_camera')]"))
                .willReturn(okJson(format.encodeToString(res))),
        )
        assertEquals(true, vh.updateCharacterCamera().success)

        val customPosition = Position(1, 2, 3)
        val customRotation = Position(4, 5, 6)
        val customName = "custom_camera"
        assertEquals(true, vh.updateCharacterCamera(customPosition, customRotation, 1, customName).success)
    }

    @Test
    fun updateCameraTest() {
        val value = 1
        val res = VirtualHomeResponse(1, true, "test", value, listOf("Test"))
        stubFor(
            post("/")
                .withRequestBody(matchingJsonPath("$[?(@.action == 'update_camera')]"))
                .willReturn(okJson(format.encodeToString(res))),
        )
        assertEquals(true, vh.updateCamera(1).success)

        val customPosition = Position(1, 2, 3)
        val customRotation = Position(4, 5, 6)
        assertEquals(true, vh.updateCamera(1, customPosition, customRotation, 1).success)
    }

    @Test
    fun addCharacterTest() {
        val value = 1
        val res = VirtualHomeResponse(1, true, "test", value, listOf("Test"))
        stubFor(
            post("/")
                .withRequestBody(matchingJsonPath("$[?(@.action == 'add_character')]"))
                .willReturn(okJson(format.encodeToString(res))),
        )
        assertEquals(true, vh.addCharacter().success)
        assertEquals(true, vh.addCharacter("Chars/Female1").success)
        val customPosition = Position(1, 2, 3)
        assertEquals(true, vh.addCharacter("Chars/Female1", customPosition).success)
        assertEquals(true, vh.addCharacter("Chars/Female1", initialRoom = "kitchen").success)
        assertEquals(true, vh.addCharacter("Chars/Female1", initialRoom = "InitialRoom").success)
    }

    @Test
    fun expandtSceneTest() {
        val value = 1
        val res = VirtualHomeResponse(1, true, format.encodeToString(Graph()), value, listOf("Test"))
        stubFor(
            post("/")
                .withRequestBody(matchingJsonPath("$[?(@.action == 'expand_scene')]"))
                .willReturn(okJson(format.encodeToString(res))),
        )
        assertEquals(true, vh.expandScene(Graph()).success)
        assertEquals(true, vh.expandScene(Graph(), ExpandSceneConfig()).success)
    }

    @Test
    fun environmentGraphTest() {
        val value = 1
        val res = VirtualHomeResponse(1, true, format.encodeToString(Graph()), value, listOf("Test"))
        stubFor(
            post("/")
                .withRequestBody(matchingJsonPath("$[?(@.action == 'environment_graph')]"))
                .willReturn(okJson(format.encodeToString(res))),
        )
        assertEquals(Graph(), vh.environmentGraph())
    }

    @Test
    fun cameraImageTest() {
        val value = 1
        val encoder = Base64.getEncoder()
        val res = VirtualHomeResponse(1, true, "test", value, listOf("Test"))
        stubFor(
            post("/")
                .withRequestBody(matchingJsonPath("$[?(@.action == 'camera_image')]"))
                .willReturn(okJson(format.encodeToString(res))),
        )
        assertEquals("Test", encoder.encodeToString(vh.cameraImage(listOf(1))[0]))
    }

    @Test
    fun cameraDataTest() {
        val value = 1
        val cameraIndexes = listOf(1, 2)
        val res = VirtualHomeResponse(1, true, "Test", value, listOf("Test"))
        stubFor(
            post("/")
                .withRequestBody(
                    matchingJsonPath("$[?(@.action == 'camera_data')]")
                        .and(matchingJsonPath("$[?(@.intParams[0] == $value)]")),
                ).willReturn(okJson(format.encodeToString(res))),
        )
        assertTrue(vh.cameraData(cameraIndexes).success)
    }

    @Test
    fun `renderScript sends correct request and returns response`() {
        val script = listOf("action1", "action2")
        val config = RenderParams()
        val expectedResponse = VirtualHomeResponse(0, true, "ok", 0, listOf("result"))
        stubFor(
            post("/")
                .withRequestBody(matchingJsonPath("$[?(@.action == 'render_script')]"))
                .willReturn(okJson(format.encodeToString(expectedResponse))),
        )
        val response = vh.renderScript(script, config)
        assertEquals(expectedResponse, response)
    }

    @Test
    fun `renderScript works with default config`() {
        val script = listOf("foo")
        val expectedResponse = VirtualHomeResponse(1, true, "default", 0, listOf("bar"))
        stubFor(
            post("/")
                .withRequestBody(matchingJsonPath("$[?(@.action == 'render_script')]"))
                .willReturn(okJson(format.encodeToString(expectedResponse))),
        )
        val response = vh.renderScript(script)
        assertEquals(expectedResponse, response)
    }

    @Test
    fun `renderScript handles empty script`() {
        val script = emptyList<String>()
        val expectedResponse = VirtualHomeResponse(2, true, "empty", 0, emptyList())
        stubFor(
            post("/")
                .withRequestBody(matchingJsonPath("$[?(@.action == 'render_script')]"))
                .willReturn(okJson(format.encodeToString(expectedResponse))),
        )
        val response = vh.renderScript(script)
        assertEquals(expectedResponse, response)
    }

    @Test
    fun `renderScript throws or returns error on server error`() {
        stubFor(
            post("/")
                .withRequestBody(matchingJsonPath("$[?(@.action == 'render_script')]"))
                .willReturn(serverError()),
        )
        assertFalse(vh.renderScript(listOf("fail")).success)
    }
}
