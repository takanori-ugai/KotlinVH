package io.github.ugaikit.vh

import kotlinx.coroutines.runBlocking
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class GetObjectsTest {
    // Helper to invoke the private writeNodesToCSV function via reflection
    private fun callWriteNodesToCSV(
        nodes: List<Node>,
        path: Path,
    ) {
        val method =
            Class
                .forName("io.github.ugaikit.vh.GetObjectsKt")
                .getDeclaredMethod("writeNodesToCSV", List::class.java, String::class.java)
        method.isAccessible = true
        method.invoke(null, nodes, path.toString())
    }

    // Helper to normalize lines similarly to File.readLines()
    private fun readLines(path: Path): List<String> {
        val content = SystemFileSystem.source(path).buffered().use { it.readString() }
        // lines() in Kotlin/Java usually handles \n, \r\n, \r.
        // File.readLines() returns a list of strings not containing line separators.
        return content
            .lines()
            .let { if (it.isNotEmpty() && it.last() == "") it.dropLast(1) else it }
    }

    @Test
    fun `writes correct CSV for non-empty node list`(
        @TempDir tempDir: File,
    ) {
        val nodes = listOf(Node(1, className = "Chair"), Node(2, className = "Table"))
        val csvPath = Path(tempDir.absolutePath, "test.csv")
        callWriteNodesToCSV(nodes, csvPath)

        val lines = readLines(csvPath)
        assertEquals("nodes/id,nodes/class_name", lines[0])
        assertEquals("1,Chair", lines[1])
        assertEquals("2,Table", lines[2])
    }

    @Test
    fun `writes only header for empty node list`(
        @TempDir tempDir: File,
    ) {
        val nodes = emptyList<Node>()
        val csvPath = Path(tempDir.absolutePath, "empty.csv")
        callWriteNodesToCSV(nodes, csvPath)

        val lines = readLines(csvPath)
        assertEquals(1, lines.size)
        assertEquals("nodes/id,nodes/class_name", lines[0])
    }

    @Test
    fun `escapes special characters in fields`(
        @TempDir tempDir: File,
    ) {
        val nodes =
            listOf(
                Node(42, className = "C,hair"),
                Node(43, className = "Tab\"le"),
                Node(44, className = "Line\nBreak"),
                Node(45, className = "Carriage\rReturn"),
            )
        val csvPath = Path(tempDir.absolutePath, "special.csv")
        callWriteNodesToCSV(nodes, csvPath)

        val lines = readLines(csvPath)
        assertEquals("nodes/id,nodes/class_name", lines[0])
        assertEquals("42,\"C,hair\"", lines[1])
        assertEquals("43,\"Tab\"\"le\"", lines[2])
        // The line break will split into two lines in the file
        assertTrue(lines[3].startsWith("44,\"Line"))
        assertTrue(lines.any { it.contains("Carriage") })
    }

    @Test
    fun `handles null className as empty string`(
        @TempDir tempDir: File,
    ) {
        val nodes = listOf(Node(99, null))
        val csvPath = Path(tempDir.absolutePath, "null.csv")
        callWriteNodesToCSV(nodes, csvPath)

        val lines = readLines(csvPath)
        assertEquals("nodes/id,nodes/class_name", lines[0])
        assertEquals("99,", lines[1])
    }

    private class FakeVirtualHomeClient(
        private val response: VirtualHomeResponse,
    ) : VirtualHomeClient() {
        override suspend fun sendRequest(data: VirtualHomeRequest): VirtualHomeResponse = response
    }

    @Test
    fun `getObjects returns empty list when message is null`() =
        runBlocking {
            val client =
                FakeVirtualHomeClient(
                    VirtualHomeResponse(id = 1, success = true, message = null, value = 0, messageList = null),
                )

            val result = client.getObjects()

            assertTrue(result.isEmpty())
        }

    @Test
    fun `getObjects returns empty list when message is empty string`() =
        runBlocking {
            val client =
                FakeVirtualHomeClient(
                    VirtualHomeResponse(id = 1, success = true, message = "", value = 0, messageList = null),
                )

            val result = client.getObjects()

            assertTrue(result.isEmpty())
        }

    @Test
    fun `getObjects returns empty list when message contains malformed json`() =
        runBlocking {
            val client =
                FakeVirtualHomeClient(
                    VirtualHomeResponse(id = 1, success = true, message = "not json", value = 0, messageList = null),
                )

            val result = client.getObjects()

            assertTrue(result.isEmpty())
        }
}
