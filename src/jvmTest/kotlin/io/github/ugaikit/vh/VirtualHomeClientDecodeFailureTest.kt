package io.github.ugaikit.vh

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.mockk.coEvery
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import kotlin.io.encoding.Base64
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class VirtualHomeClientDecodeFailureTest {
    @Test
    fun `cameraImage preserves image positions for malformed base64 payloads`() =
        runTest {
            val client = spyk(VirtualHomeClient())
            coEvery { client.sendRequest(any()) } returns
                VirtualHomeResponse(
                    id = 1,
                    success = true,
                    message = null,
                    value = 0,
                    messageList = listOf("not-base64", Base64.Default.encode("ok".encodeToByteArray())),
                )

            val images = client.cameraImage(listOf(0, 1))
            assertEquals(2, images.size)
            assertTrue(images[0].isEmpty())
            assertTrue(images[1].contentEquals("ok".encodeToByteArray()))
        }

    @Test
    fun `environmentGraph falls back to empty graph on malformed json`() =
        runTest {
            val client = spyk(VirtualHomeClient())
            coEvery { client.sendRequest(any()) } returns
                VirtualHomeResponse(
                    id = 1,
                    success = true,
                    message = "not-json",
                    value = 0,
                    messageList = null,
                )

            assertEquals(Graph(), client.environmentGraph())
        }

    @Test
    fun `environmentGraph returns an empty graph when the response has no message`() =
        runTest {
            val client = spyk(VirtualHomeClient())
            coEvery { client.sendRequest(any()) } returns
                VirtualHomeResponse(
                    id = 1,
                    success = true,
                    message = null,
                    value = 0,
                    messageList = null,
                )

            assertTrue(client.environmentGraph().nodes.isEmpty())
        }

    @Test
    fun `getVisibleObjects falls back to empty list on malformed json`() =
        runTest {
            val client = spyk(VirtualHomeClient())
            coEvery { client.sendRequest(any()) } returns
                VirtualHomeResponse(
                    id = 1,
                    success = true,
                    message = "not-json",
                    value = 0,
                    messageList = null,
                )

            assertEquals(emptyList<Int>(), client.getVisibleObjects(0))
        }

    @Test
    fun `visibleObjects falls back to empty map on malformed json`() =
        runTest {
            val client = spyk(VirtualHomeClient())
            coEvery { client.sendRequest(any()) } returns
                VirtualHomeResponse(
                    id = 1,
                    success = true,
                    message = "not-json",
                    value = 0,
                    messageList = null,
                )

            assertEquals(emptyMap<String, String>(), client.visibleObjects(0))
        }

    @Test
    fun `characterCameras falls back to empty list on malformed json`() =
        runTest {
            val client = spyk(VirtualHomeClient())
            coEvery { client.sendRequest(any()) } returns
                VirtualHomeResponse(
                    id = 1,
                    success = true,
                    message = "not-json",
                    value = 0,
                    messageList = null,
                )

            assertEquals(emptyList<String>(), client.characterCameras())
        }

    @Test
    fun `sendRequest returns a generic failure response when transport throws`() =
        runTest {
            val client =
                VirtualHomeClient(
                    httpClient =
                        HttpClient(
                            MockEngine {
                                error("boom")
                            },
                        ),
                )

            val response = client.check()

            assertTrue(!response.success)
            assertEquals("Request failed", response.message)
        }
}
