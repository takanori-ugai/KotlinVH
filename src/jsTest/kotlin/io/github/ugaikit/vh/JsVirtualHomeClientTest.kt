package io.github.ugaikit.vh

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class JsVirtualHomeClientTest {
    @Test
    fun `rejects invalid connection parameters`() {
        assertFailsWith<IllegalArgumentException> {
            JsVirtualHomeClient(host = "")
        }
        assertFailsWith<IllegalArgumentException> {
            JsVirtualHomeClient(host = "bad host")
        }
        assertFailsWith<IllegalArgumentException> {
            JsVirtualHomeClient(host = "localhost:9000")
        }
        assertFailsWith<IllegalArgumentException> {
            JsVirtualHomeClient(host = "localhost", port = 0)
        }
        assertFailsWith<IllegalArgumentException> {
            JsVirtualHomeClient(host = "localhost", timeout = 0)
        }
    }

    @Test
    fun `delegates requests through the JS client`() =
        runTest {
            val responses =
                listOf(
                    """{"id":1,"success":true,"message":null,"value":7,"message_list":null}""",
                    """{"id":2,"success":true,"message":null,"value":0,"message_list":null}""",
                    """{"id":3,"success":true,"message":null,"value":0,"message_list":null}""",
                    """{"id":4,"success":true,"message":null,"value":0,"message_list":null}""",
                    """{"id":5,"success":true,"message":null,"value":0,"message_list":null}""",
                    """{"id":6,"success":true,"message":"[{\"id\":1,\"class_name\":\"chair\"}]","value":0,"message_list":null}""",
                    """{"id":7,"success":true,"message":"{\"nodes\":[{\"id\":2,\"class_name\":\"table\"}],\"edges\":[]}","value":0,"message_list":null}""",
                )
            var requestIndex = 0
            val engine =
                MockEngine { _ ->
                    val body = responses.getOrElse(requestIndex) { responses.last() }
                    requestIndex += 1
                    respond(
                        content = body,
                        status = HttpStatusCode.OK,
                        headers = headersOf("Content-Type", ContentType.Application.Json.toString()),
                    )
                }
            val delegate =
                VirtualHomeClient(
                    httpClient =
                        HttpClient(engine) {
                            install(ContentNegotiation) {
                                json(
                                    Json {
                                        encodeDefaults = true
                                        explicitNulls = false
                                    },
                                )
                            }
                        },
                )
            val client = JsVirtualHomeClient(delegate as Any)

            assertEquals(7, client.cameraCount())
            assertEquals(true, client.activatePhysics(true).success)
            assertEquals(true, client.check().success)
            assertEquals(true, client.checkScript(listOf("walk")).success)
            assertEquals(0, client.reset(3).value)
            assertEquals(1, client.getObjects().size)
            assertEquals(Graph(mutableListOf(Node(2, className = "table"))), client.environmentGraph())

            client.close()
        }

    @Test
    fun `use closes the client and blocks further calls`() =
        runTest {
            val engine =
                MockEngine { _ ->
                    respond(
                        content = """{"id":1,"success":true,"message":null,"value":7,"message_list":null}""",
                        status = HttpStatusCode.OK,
                        headers = headersOf("Content-Type", ContentType.Application.Json.toString()),
                    )
                }
            val delegate =
                VirtualHomeClient(
                    httpClient =
                        HttpClient(engine) {
                            install(ContentNegotiation) {
                                json(
                                    Json {
                                        encodeDefaults = true
                                        explicitNulls = false
                                    },
                                )
                            }
                        },
                )
            val client = JsVirtualHomeClient(delegate as Any)

            client.use { used ->
                assertEquals(7, used.cameraCount())
            }

            assertFailsWith<IllegalStateException> {
                runTest {
                    client.cameraCount()
                }
            }
        }
}
