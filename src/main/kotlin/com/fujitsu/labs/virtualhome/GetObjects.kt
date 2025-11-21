package com.fujitsu.labs.virtualhome

import java.io.File

/**
 * The main function that initializes the client, resets the scene, retrieves the environment graph,
 * and writes the nodes to a CSV file.
 */
private const val SCENE_INDEX = 6

fun main() {
    val client = VirtualHomeClient(host = "localhost")
    client.reset(SCENE_INDEX)
    val graph = client.environmentGraph()
    writeNodesToCSV(graph.nodes, "scene${SCENE_INDEX + 1}.csv")
}

/**
 * Writes the list of nodes to a CSV file.
 *
 * @param nodes The list of nodes to be written to the CSV file.
 * @param fileName The name of the CSV file.
 */
private fun writeNodesToCSV(
    nodes: List<Node>,
    fileName: String,
) {
    // Escapes a value for safe CSV output according to RFC 4180
    fun escapeCsv(value: String?): String =
        if (value == null) {
            ""
        } else if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
            "\"" + value.replace("\"", "\"\"") + "\""
        } else {
            value
        }

    File(fileName).bufferedWriter(Charsets.UTF_8).use { out ->
        val header = "nodes/id,nodes/class_name"
        out.write("$header\n")
        println(header)
        nodes.forEach { node ->
            val idField = escapeCsv(node.id.toString())
            val classNameField = escapeCsv(node.className)
            out.write("$idField,$classNameField\n")
        }
    }
}

/**
 * A class named GetObjects. Currently, it doesn't contain any properties or methods.
 */
class GetObjects
