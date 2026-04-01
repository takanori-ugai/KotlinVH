package io.github.ugaikit.vh

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MissingApiTest {
    @Test
    fun testActivatePhysics() =
        runTest {
            val client = spyk(VirtualHomeClient())
            val expectedResponse = VirtualHomeResponse(0, true, null, 0, null)
            coEvery { client.sendRequest(any()) } returns expectedResponse

            client.activatePhysics(true)

            coVerify {
                client.sendRequest(
                    withArg {
                        assertEquals("activate_physics", it.action)
                        assertEquals(listOf("true"), it.stringParams)
                    },
                )
            }
        }

    @Test
    fun testCheckScript() =
        runTest {
            val client = spyk(VirtualHomeClient())
            val script = listOf("walk to <sofa> (1)")
            val expectedResponse = VirtualHomeResponse(0, true, null, 0, null)
            coEvery { client.sendRequest(any()) } returns expectedResponse

            client.checkScript(script)

            coVerify {
                client.sendRequest(
                    withArg {
                        assertEquals("check_script", it.action)
                        assertEquals(script, it.stringParams)
                    },
                )
            }
        }

    @Test
    fun testGetObjects() =
        runTest {
            val client = spyk(VirtualHomeClient())
            val expectedResponse =
                VirtualHomeResponse(
                    0,
                    true,
                    """[{"id":1,"class_name":"sofa"}]""",
                    0,
                    null,
                )
            coEvery { client.sendRequest(any()) } returns expectedResponse

            val objects = client.getObjects()

            coVerify {
                client.sendRequest(
                    withArg {
                        assertEquals("get_objects", it.action)
                    },
                )
            }
            assertEquals(1, objects.size)
            assertEquals(1, objects[0].id)
            assertEquals("sofa", objects[0].className)
        }
}
