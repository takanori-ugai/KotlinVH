package com.fujitsu.labs.virtualhome

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

/**
 * This class contains tests for the VirtualHomeClient's request methods.
 */
@WireMockTest
class RequestTest {
    /**
     * Configuration of Json converter
     */
    private val format =
        Json {
            encodeDefaults = true
            @ExperimentalSerializationApi
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
    fun checkTest(info: WireMockRuntimeInfo) {
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
    fun resetTest(info: WireMockRuntimeInfo) {
        val sceneIndex = 0
        val res = VirtualHomeResponse(1, true, "test", 1, listOf("Test"))
        stubFor(
            post("/")
                .withRequestBody(
                    matchingJsonPath("$[?(@.action == 'reset')]")
                        .and(matchingJsonPath("$[?(@.intParams[0] == $sceneIndex)]")),
                )
                .willReturn(okJson(format.encodeToString(res))),
        )
        assertTrue(vh.reset(sceneIndex).success)
        assertTrue(vh.reset().success)
    }

    /**
     * Tests the cameraCount method of the VirtualHomeClient class.
     */
    @Test
    fun cameraCountTest(info: WireMockRuntimeInfo) {
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
    fun addCharacterCameraTest(info: WireMockRuntimeInfo) {
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
    fun addCameraTest(info: WireMockRuntimeInfo) {
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
    fun getVisibleObjectsTest(info: WireMockRuntimeInfo) {
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
    fun getVisibleObjectsTest2(info: WireMockRuntimeInfo) {
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
    fun visibleObjectTest(info: WireMockRuntimeInfo) {
        val value = 1
        val map = mapOf("0" to "1")
        val res = VirtualHomeResponse(1, true, format.encodeToString(map), value, listOf("Test"))
        stubFor(
            post("/")
                .withRequestBody(
                    matchingJsonPath("$[?(@.action == 'observation')]")
                        .and(matchingJsonPath("$[?(@.intParams[0] == $value)]")),
                )
                .willReturn(okJson(format.encodeToString(res))),
        )
        assertEquals(map, vh.visibleObjects(value))
    }

    @Test
    fun updateCharacterCameraTest(info: WireMockRuntimeInfo) {
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
    fun updateCameraTest(info: WireMockRuntimeInfo) {
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
    fun environmentGraphTest(info: WireMockRuntimeInfo) {
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
    fun cameraImageTest(info: WireMockRuntimeInfo) {
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
    fun cameraDataTest(info: WireMockRuntimeInfo) {
        val value = 1
        val cameraIndexes = listOf(1, 2)
        val res = VirtualHomeResponse(1, true, "Test", value, listOf("Test"))
        stubFor(
            post("/")
                .withRequestBody(
                    matchingJsonPath("$[?(@.action == 'camera_data')]")
                        .and(matchingJsonPath("$[?(@.intParams[0] == $value)]")),
                )
                .willReturn(okJson(format.encodeToString(res))),
        )
        assertTrue(vh.cameraData(cameraIndexes).success)
    }
}
