package io.github.ugaikit.vh

import io.mockk.Runs
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertTrue

class EntryPointCoverageTest {
    @AfterTest
    fun tearDown() {
        runMainDemoInvoker = ::runMainDemo
        createGetObjectsClient = { VirtualHomeClient(host = "localhost") }
        runCatching { Files.deleteIfExists(Path.of("bfo.png")) }
        runCatching { Files.deleteIfExists(Path.of("scene7.csv")) }
    }

    @Test
    fun `jvm helper delegates to shared demo`() =
        runTest {
            runMainDemoInvoker = { _ -> }

            runJvmMain {}
        }

    @Test
    fun `jvm main delegates to shared demo`() {
        runMainDemoInvoker = { _ -> }

        val mainMethod = Class.forName("io.github.ugaikit.vh.MainKt").getDeclaredMethod("main", Array<String>::class.java)
        mainMethod.invoke(null, arrayOf<String>())
    }

    @Test
    fun `get objects export helper writes the current scene`() =
        runTest {
            val client = mockk<VirtualHomeClient>()
            io.mockk.coEvery { client.reset(any()) } returns VirtualHomeResponse(6, true, null, 0, null)
            io.mockk.coEvery { client.environmentGraph() } returns Graph(nodes = mutableListOf(Node(12, className = "chair")))

            val tempFile =
                kotlin.io.path
                    .createTempFile(prefix = "scene", suffix = ".csv")
                    .toString()
            exportScene(client, tempFile)

            val content =
                Files.readString(
                    java.nio.file.Path
                        .of(tempFile),
                )
            assertTrue(content.contains("nodes/id,nodes/class_name"))
            assertTrue(content.contains("12,chair"))
        }

    @Test
    fun `get objects main delegates to export flow`() {
        val client = mockk<VirtualHomeClient>()
        io.mockk.coEvery { client.reset(any()) } returns VirtualHomeResponse(6, true, null, 0, null)
        io.mockk.coEvery { client.environmentGraph() } returns Graph(nodes = mutableListOf(Node(12, className = "chair")))
        io.mockk.every { client.close() } just Runs
        createGetObjectsClient = { client }

        val mainMethod = Class.forName("io.github.ugaikit.vh.GetObjectsKt").getDeclaredMethod("main")
        mainMethod.invoke(null)
    }
}
