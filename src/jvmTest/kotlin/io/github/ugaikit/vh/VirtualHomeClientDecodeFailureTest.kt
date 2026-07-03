package io.github.ugaikit.vh

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.mockk.coEvery
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class VirtualHomeClientDecodeFailureTest {
    @Test
    fun `cameraImage throws on malformed base64 payload`() =
        runTest {
            val client = spyk(VirtualHomeClient())
            coEvery { client.sendRequest(any()) } returns
                VirtualHomeResponse(
                    id = 1,
                    success = true,
                    message = null,
                    value = 0,
                    messageList = listOf("not-base64"),
                )

            assertFailsWith<IllegalArgumentException> {
                client.cameraImage(listOf(0))
            }
        }

    @Test
    fun `environmentGraph throws on malformed json`() =
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

            assertFailsWith<SerializationException> {
                client.environmentGraph()
            }
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
    fun `visibleObjects throws on malformed json`() =
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

            assertFailsWith<SerializationException> {
                client.visibleObjects(0)
            }
        }

    @Test
    fun `characterCameras throws on malformed json`() =
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

            assertFailsWith<SerializationException> {
                client.characterCameras()
            }
        }

    @Test
    fun `sendRequest returns failure response when transport throws`() =
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
            assertTrue(response.message.orEmpty().contains("Fail to prepare request body"))
        }
}
