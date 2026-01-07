package io.github.ugaikit.vh

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class JavaVirtualHomeClientTest {
    @Test
    fun `delegates check and reset`() {
        val mock = mockk<VirtualHomeClient>()
        val sceneIndexSlot = slot<Int>()
        val checkResponse = VirtualHomeResponse(1, true, null, 0, null)
        val resetResponse = VirtualHomeResponse(2, true, null, 0, null)
        coEvery { mock.check() } returns checkResponse
        coEvery { mock.reset(capture(sceneIndexSlot)) } returns resetResponse

        val client = JavaVirtualHomeClient(client = mock)

        assertSame(checkResponse, client.check())
        val resetResult = client.reset(5)

        assertEquals(5, sceneIndexSlot.captured)
        assertSame(resetResponse, resetResult)
        coVerify(exactly = 1) { mock.check() }
        coVerify(exactly = 1) { mock.reset(5) }
    }

    @Test
    fun `propagates exceptions from delegate`() {
        val mock = mockk<VirtualHomeClient>()
        val ex = RuntimeException("boom")
        coEvery { mock.check() } throws ex

        val client = JavaVirtualHomeClient(client = mock)

        val thrown = assertFailsWith<RuntimeException> { client.check() }
        assertSame(ex, thrown)
        coVerify(exactly = 1) { mock.check() }
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
    fun `delegates addCharacter`() {
        val mock = mockk<VirtualHomeClient>()
        val position = Position(1, 2, 3)
        val addCharacterResponse = VirtualHomeResponse(4, true, null, 0, null)

        coEvery { mock.addCharacter("Chars/Test", position, "kitchen") } returns addCharacterResponse

        val client = JavaVirtualHomeClient(client = mock)
        val addCharacterResult = client.addCharacter("Chars/Test", position, "kitchen")

        assertSame(addCharacterResponse, addCharacterResult)
        coVerify(exactly = 1) { mock.addCharacter("Chars/Test", position, "kitchen") }
    }

    @Test
    fun `delegates renderScript`() {
        val mock = mockk<VirtualHomeClient>()
        val script = listOf("do something")
        val renderResponse = VirtualHomeResponse(5, true, null, 0, null)

        val client = JavaVirtualHomeClient(client = mock)
        val renderConfig = client.createRenderParams(1, true, false, true, false, true)
        coEvery { mock.renderScript(script, renderConfig) } returns renderResponse
        val renderResult = client.renderScript(script, renderConfig)

        assertSame(renderResponse, renderResult)
        coVerify(exactly = 1) { mock.renderScript(script, renderConfig) }
    }

    @Test
    fun `delegates addCamera`() {
        val mock = mockk<VirtualHomeClient>()
        val position = Position(0, 1, 2)
        val rotation = Position(3, 4, 5)
        val addCameraResponse = VirtualHomeResponse(6, true, null, 0, null)

        coEvery { mock.addCamera(position, rotation, 90) } returns addCameraResponse

        val client = JavaVirtualHomeClient(client = mock)

        val addCameraResult = client.addCamera(position, rotation, 90)

        assertSame(addCameraResponse, addCameraResult)

        coVerify { mock.addCamera(position, rotation, 90) }
    }

    @Test
    fun `delegates cameraCount`() {
        val mock = mockk<VirtualHomeClient>()
        coEvery { mock.cameraCount() } returns 42

        val client = JavaVirtualHomeClient(client = mock)

        assertEquals(42, client.cameraCount())
        coVerify { mock.cameraCount() }
    }

    @Test
    fun `delegates cameraData`() {
        val mock = mockk<VirtualHomeClient>()
        val response = VirtualHomeResponse(7, true, null, 0, null)
        coEvery { mock.cameraData(listOf(1, 2, 3)) } returns response

        val client = JavaVirtualHomeClient(client = mock)

        assertSame(response, client.cameraData(listOf(1, 2, 3)))
        coVerify { mock.cameraData(listOf(1, 2, 3)) }
    }

    @Test
    fun `delegates visibleObjects`() {
        val mock = mockk<VirtualHomeClient>()
        val visibleObjects = mapOf("obj" to "value")
        coEvery { mock.visibleObjects(7) } returns visibleObjects

        val client = JavaVirtualHomeClient(client = mock)

        assertEquals(visibleObjects, client.visibleObjects(7))
        coVerify { mock.visibleObjects(7) }
    }

    @Test
    fun `delegates cameraImage`() {
        val mock = mockk<VirtualHomeClient>()
        val imageBytes = listOf(byteArrayOf(1, 2, 3))
        coEvery { mock.cameraImage(listOf(8), "depth", 10, 11) } returns imageBytes

        val client = JavaVirtualHomeClient(client = mock)

        assertSame(imageBytes, client.cameraImage(listOf(8), "depth", 10, 11))
        coVerify { mock.cameraImage(listOf(8), "depth", 10, 11) }
    }

    @Test
    fun `cameraImage propagates delegate exceptions`() {
        val mock = mockk<VirtualHomeClient>()
        val ex = IllegalStateException("camera fail")
        coEvery { mock.cameraImage(listOf(1), any(), any(), any()) } throws ex

        val client = JavaVirtualHomeClient(client = mock)

        val thrown =
            assertFailsWith<IllegalStateException> {
                client.cameraImage(listOf(1), "normal", 640, 480)
            }
        assertSame(ex, thrown)
        coVerify { mock.cameraImage(listOf(1), "normal", 640, 480) }
    }
}
