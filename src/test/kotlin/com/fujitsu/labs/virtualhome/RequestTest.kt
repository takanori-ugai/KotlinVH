package com.fujitsu.labs.virtualhome

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@WireMockTest
class RequestTest {

    private val format = Json {
        encodeDefaults = true
        @ExperimentalSerializationApi
        explicitNulls = false
    }

    @Test
    fun checkTest(info: WireMockRuntimeInfo) {
        val res = VirtualHomeResponse(1, true, "test", 1, listOf("Test"))
        stubFor(
            post("/")
                .withRequestBody(matchingJsonPath("$[?(@.action == 'idle')]"))
                .willReturn(okJson(format.encodeToString(res)))
        )
        val port = info.httpPort
        val vh = VirtualHomeClient(port = port)
        assertTrue(vh.check()?.success!!)
    }

    @Test
    fun resetTest(info: WireMockRuntimeInfo) {
        val sceneIndex = 0
        val res = VirtualHomeResponse(1, true, "test", 1, listOf("Test"))
        stubFor(
            post("/")
                .withRequestBody(
                    matchingJsonPath("$[?(@.action == 'reset')]")
                        .and(matchingJsonPath("$[?(@.intParams[0] == $sceneIndex)]"))
                )
                .willReturn(okJson(format.encodeToString(res)))
        )
        val port = info.httpPort
        val vh = VirtualHomeClient(port = port)
        assertTrue(vh.reset(sceneIndex)?.success!!)
    }
}
