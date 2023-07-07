package com.fujitsu.labs.virtualhome

import java.io.File

fun main() {
    val sceneNum = 3
    val client = VirtualHomeClient(host = "localhost")
    val sceneIndex = 6
    client.reset(sceneIndex)
    val graph = client.environmentGraph()
    File("scene${sceneIndex + 1}.csv").bufferedWriter(Charsets.UTF_8).use { out ->
        out.write("nodes/id,nodes/class_name\n")
        println("nodes/id,nodes/class_name")
        graph.nodes.forEach {
            out.write("${it.className},${it.id}\n")
        }
    }
}
class GetObjects
