package com.fujitsu.labs.virtualhome

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.lang.System.currentTimeMillis
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
// import javax.imageio.ImageIO
// import org.jetbrains.kotlinx.jupyter.api.annotations.JupyterLibrary
// import org.jetbrains.kotlinx.jupyter.api.*
// import org.jetbrains.kotlinx.jupyter.api.libraries.*

fun main(args: Array<String>) {
    val format = Json {
        encodeDefaults = true
//        explicitNulls = false
    }
    val data = VirtualHomeRequest(currentTimeMillis().toInt(), "idle")
    val sq = VirtualHomeClient(host = "localhost")
    val res = sq.sendRequest(format.encodeToString(data).toByteArray(Charsets.UTF_8))
    println(res?.success)
    println("Check: " + sq.check()?.success)
    println(sq.reset(4)?.success)
    println(sq.visibleObjects(0))
    println(sq.visibleObjects(1).size)
    println(sq.visibleObjects(2).size)
    println(sq.visibleObjects(3).size)
    val graph = sq.environmentGraph()
    println(graph.nodes[0])
    println(graph.nodes.size)
    val sofa = graph.nodes.filter { it.class_name == "sofa" } [1]
    println(sofa)
    graph.nodes.add(Node(class_name = "cat", category = "Animals", id = 1000, properties = listOf(), states = listOf()))
    println(graph.nodes.filter { it.class_name == "cat" })
    graph.edges.add(Edge(from_id = 1000, to_id = sofa.id!!, relation_type = "ON"))
    println(sq.expandScene(graph))
    val graph2 = sq.environmentGraph()
    // expandSceneのあとオブジェクトのIDが変化する
    println(graph2.nodes.filter { it.id == 1000 })
    println(graph2.nodes.filter { it.class_name == "cat" })
    // edgeは適切に修正される
    println(graph2.edges.filter { it.to_id == sofa.id })
    println(graph2.nodes.size)
    // キャラクターを追加するのはシーンを作った後、キャラクターを追加してからシーンを作成するとサーバが止まる
    println(sq.addCharacter())
    val config = RenderParams(
        processing_time_limit = 60, find_solution = false, skip_animation = false, recording = true,
        save_pose_data = true
    )
    val script = listOf("<char0> [WALK] <cat> (366)")
    val scriptObj = Script(script)
    val scriptObj2 = Script(listOf("<char0> [WALK] <cat> (366) <dog> (377)"))
    val scriptObj3 = Script(listOf("[WALK] <cat> (366) <dog> (377)"))
    println(sq.renderScript(script, config))
    val res0 = sq.cameraImage(listOf(0))
//    val image = Base64.getDecoder().decode(res0?.message_list?.get(0)?.toByteArray(Charsets.UTF_8))
    val image = Base64.getDecoder().decode(res0?.message_list?.get(0))
    Files.write(Paths.get("bfo.png"), image)
    /*
    val imag = ImageIO.read(ByteArrayInputStream(image))
    ImageIO.write(imag, "png", File(".", "snap.png"))
    */
}

class Main
