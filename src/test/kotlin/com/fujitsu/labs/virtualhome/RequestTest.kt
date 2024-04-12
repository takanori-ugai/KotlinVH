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
}
