package io.github.ugaikit.vh

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class GetObjectsTest {
    // Helper to invoke the private writeNodesToCSV function via reflection
    private fun callWriteNodesToCSV(
        nodes: List<Node>,
        file: File,
    ) {
        val method =
            Class
                .forName("io.github.ugaikit.vh.GetObjectsKt")
                .getDeclaredMethod("writeNodesToCSV", List::class.java, String::class.java)
        method.isAccessible = true
        method.invoke(null, nodes, file.absolutePath)
    }

    @Test
    fun `writes correct CSV for non-empty node list`(
        @TempDir tempDir: File,
    ) {
        val nodes = listOf(Node(1, className = "Chair"), Node(2, className = "Table"))
        val csvFile = File(tempDir, "test.csv")
        callWriteNodesToCSV(nodes, csvFile)

        val lines = csvFile.readLines()
        assertEquals("nodes/id,nodes/class_name", lines[0])
        assertEquals("1,Chair", lines[1])
        assertEquals("2,Table", lines[2])
    }

    @Test
    fun `writes only header for empty node list`(
        @TempDir tempDir: File,
    ) {
        val nodes = emptyList<Node>()
        val csvFile = File(tempDir, "empty.csv")
        callWriteNodesToCSV(nodes, csvFile)

        val lines = csvFile.readLines()
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
        val csvFile = File(tempDir, "special.csv")
        callWriteNodesToCSV(nodes, csvFile)

        val lines = csvFile.readLines()
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
        val csvFile = File(tempDir, "null.csv")
        callWriteNodesToCSV(nodes, csvFile)

        val lines = csvFile.readLines()
        assertEquals("nodes/id,nodes/class_name", lines[0])
        assertEquals("99,", lines[1])
    }
}
