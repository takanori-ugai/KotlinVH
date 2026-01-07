package io.github.ugaikit.vh

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class JavaVirtualHomeClientTest {
    @Test
    fun `delegates check and reset`() {
        val mock = mockk<VirtualHomeClient>()
        var seenSceneIndex: Int? = null
        val checkResponse = VirtualHomeResponse(1, true, null, 0, null)
        val resetResponse = VirtualHomeResponse(2, true, null, 0, null)
        coEvery { mock.check() } returns checkResponse
        coEvery { mock.reset(any()) } answers {
            seenSceneIndex = firstArg()
            resetResponse
        }

        val client = JavaVirtualHomeClient(client = mock)

        assertSame(checkResponse, client.check())
        val resetResult = client.reset(5)

        assertEquals(5, seenSceneIndex)
        assertSame(resetResponse, resetResult)
        coVerify(exactly = 1) { mock.check() }
        coVerify(exactly = 1) { mock.reset(5) }
    }

    @Test
    fun `delegates environmentGraph and expandScene`() {
        val mock = mockk<VirtualHomeClient>()
        val graph = Graph()
        val config = ExpandSceneConfig(randomize = true)
        val expandResponse = VirtualHomeResponse(3, true, null, 0, null)
        coEvery { mock.environmentGraph() } returns graph
        coEvery { mock.expandScene(graph, config) } returns expandResponse

        val client = JavaVirtualHomeClient(client = mock)
        val result = client.expandScene(graph, config)

        assertSame(expandResponse, result)
        coVerify(exactly = 1) { mock.expandScene(graph, config) }
        coVerify(exactly = 0) { mock.environmentGraph() } // wrapper does not call implicitly
    }

    @Test
    fun `delegates addCharacter and renderScript`() {
        val mock = mockk<VirtualHomeClient>()
        val position = Position(1, 2, 3)
        val script = listOf("do something")
        val addCharacterResponse = VirtualHomeResponse(4, true, null, 0, null)
        val renderResponse = VirtualHomeResponse(5, true, null, 0, null)

        coEvery { mock.addCharacter("Chars/Test", position, "kitchen") } returns addCharacterResponse
        // renderScript stub set after renderConfig creation

        val client = JavaVirtualHomeClient(client = mock)
        val renderConfig = client.createRenderParams(1, true, false, true, false, true)
        coEvery { mock.renderScript(script, renderConfig) } returns renderResponse
        val addCharacterResult = client.addCharacter("Chars/Test", position, "kitchen")
        val renderResult = client.renderScript(script, renderConfig)

        assertSame(addCharacterResponse, addCharacterResult)
        assertSame(renderResponse, renderResult)
        coVerify(exactly = 1) { mock.addCharacter("Chars/Test", position, "kitchen") }
        coVerify(exactly = 1) { mock.renderScript(script, renderConfig) }
    }

    @Test
    fun `delegates camera operations`() {
        val mock = mockk<VirtualHomeClient>()
        val position = Position(0, 1, 2)
        val rotation = Position(3, 4, 5)
        val addCameraResponse = VirtualHomeResponse(6, true, null, 0, null)
        val cameraDataResponse = VirtualHomeResponse(7, true, null, 0, null)
        val visibleObjects = mapOf("obj" to "value")
        val imageBytes = listOf(byteArrayOf(1, 2, 3))

        coEvery { mock.addCamera(position, rotation, 90) } returns addCameraResponse
        coEvery { mock.cameraCount() } returns 42
        coEvery { mock.cameraData(listOf(1, 2, 3)) } returns cameraDataResponse
        coEvery { mock.visibleObjects(7) } returns visibleObjects
        coEvery { mock.cameraImage(listOf(8), "depth", 10, 11) } returns imageBytes

        val client = JavaVirtualHomeClient(client = mock)

        val addCameraResult = client.addCamera(position, rotation, 90)
        val count = client.cameraCount()
        val dataResult = client.cameraData(listOf(1, 2, 3))
        val visible = client.visibleObjects(7)
        val image = client.cameraImage(listOf(8), "depth", 10, 11)

        assertSame(addCameraResponse, addCameraResult)
        assertEquals(42, count)
        assertSame(cameraDataResponse, dataResult)
        assertEquals(visibleObjects, visible)
        assertSame(imageBytes, image)

        coVerify { mock.addCamera(position, rotation, 90) }
        coVerify { mock.cameraCount() }
        coVerify { mock.cameraData(listOf(1, 2, 3)) }
        coVerify { mock.visibleObjects(7) }
        coVerify { mock.cameraImage(listOf(8), "depth", 10, 11) }
    }
}
