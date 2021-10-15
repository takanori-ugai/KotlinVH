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

    /**
     * Not supported action in Unity Simulater
     **  EAT
     **  CUT
     **  PUTIN
     **  WIPE
     **  PUTON
     **  PUTOFF
     **  GREET
     **  DROP
     **  LIE
     **  POUR
     **  TYPE
     *   PUSH
     *   PULL
     **  MOVE
     **  WASH
     *   RINSE
     **  SLEEP
     **  WAKEUP
     *   SCRUB
     **  SQUEEZE
     *   RELEASE
     **  PLUGIN
     **  PLUGOUT
     * Supported action
     **  FIND
     **  WALK
     **  DRINK
     **  CLOSE // ドアをちゃんと閉めない。ドアしか閉められない
     **  READ
     **  SIT
     **  STANDUP
     **  PUTBACK
     **  GRAB
     **  LOOKAT
     *   LOOKAT_SHORT
     *   LOOKAT_MEDIUM = LOOKAT
     *   LOOKAT_LONG
     **  OPEN // ドアしか開けられない
     **  POINTAT = LOOKAT
     *   PUTOBJBACK // 手に持っているものを元あった場所に戻す
     *   RUN
     **  SWITCHOFF
     **  SWITCHON
     **  TOUCH
     **  TURNTO
     **  WATCH
     */
    val script0 = listOf(
//        "<char0> [WALK] <wine> (93)",
//        "<char0> [GRAB] <wine> (93)",
//        "<char0> [DRINK] <wine> (93)",
//        "<char0> [WALK] <washingmachine> (365)",
//        "<char0> [PUTIN] <washingmachine> (365)  <wine> (93)",
//        "<char0> [CLOSE] <door> (128)",
//        "<char0> [LOOKAT_LONG] <door> (128)",
//        "<char0> [OPEN] <door> (128)",
//        "<char0> [WALK] <sofa> (25)",
//        "<char0> [FIND] <book> (86)",
//        "<char0> [PULL] <sofa> (25)",
//        "<char0> [GRAB] <book> (86)",
//        "<char0> [WALK] <tv> (106)",
//        "<char0> [TOUCH] <tv> (106)",
//        "<char0> [TURNTO] <tv> (106)",
//        "<char0> [WIPE] <tv> (106)",
//        "<char0> [FIND] <sofa> (139)",
//        "<char0> [SIT] <sofa> (139)",
//        "<char0> [STANDUP] <sofa> (139)",
        "<char0> [WALK] <tablelamp> (76)",
//        "<char0> [PUTON] <clothesshirt> (36)"
//        "<char0> [GRAB] <book> (86)",
        "<char0> [PLUGIN] <tablelamp> (76)"
    )
    val main = Main()
//    main.checkScripts(script0)
    main.testScripts(script0)
    println(main.findNodes("tv"))
    println(main.findNodesByProperty("HAS_PLUG"))
    println(main.findNodesById(1))
    System.exit(0)

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
    val sofa = graph.nodes.filter { it.class_name == "sofa" }[1]
    println(sofa)
    graph.nodes.add(Node(class_name = "cat", category = "Animals", id = 1000, properties = listOf(), states = listOf()))
    println("ADDRESSBOOK: " + graph.nodes.filter { it.class_name == "book" })
    graph.edges.add(Edge(from_id = 1000, to_id = sofa.id!!, relation_type = "ON"))
    println(sq.expandScene(graph))
    val graph2 = sq.environmentGraph()
    // expandSceneのあとオブジェクトのIDが変化する
    println(graph2.nodes.filter { it.id == 1000 })
    println("CAT: " + graph2.nodes.filter { it.class_name == "cat" })
    // edgeは適切に修正される
    println(graph2.edges.filter { it.to_id == sofa.id })
    println(graph2.nodes.size)
    // キャラクターを追加するのはシーンを作った後、キャラクターを追加してからシーンを作成するとサーバが止まる
    println(sq.addCharacter())
    val config = RenderParams(
        processing_time_limit = 60, find_solution = false, skip_animation = false, recording = true,
        save_pose_data = true
    )
//    val config = RenderParams(
//        processing_time_limit = 60, find_solution = true, skip_animation = true, recording = true,
//        save_pose_data = false, image_synthesis = listOf()
//    )
    // val script = listOf("<char0> [WALK] <cat> (366)")
    val script = listOf("<char0> [RUN] <book> (86)", "<char0> [FIND] <book> (86)", "<char0> [READ] <book> (86)", "<char0> [WALK] <sofa> (139)")
    val scriptObj = Script(script)
//    val scriptObj2 = Script(listOf("<char0> [WALK] <cat> (366) <dog> (377)"))
//    val scriptObj3 = Script(listOf("[WALK] <cat> (366) <dog> (377)"))
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


class Main {
    val client = VirtualHomeClient(host = "localhost")

    fun testScripts(script: List<String>): Boolean {
        if(! client.reset(4)!!.success) throw Exception("Reset Error")
        val initGraph = client.environmentGraph()
        val sofa = initGraph.nodes.filter { it.class_name == "sofa" }[1]
//        initGraph.nodes.add(Node(class_name = "cat", category = "Animals", id = 1000, properties = listOf(), states = listOf()))
//        initGraph.edges.add(Edge(from_id = 1000, to_id = sofa.id!!, relation_type = "ON"))
        if(! client.expandScene(initGraph)!!.success) throw Exception("Expand Scene Error")
        if(! client.addCharacter()!!.success) throw Exception("Add Character Error")
        val graph = client.environmentGraph()
//        val catId = graph.nodes.filter { it.class_name == "cat"}[0]
//        println("CATID: $catId")
        val config = RenderParams(
            processing_time_limit = 1, find_solution = false, skip_animation = false, recording = true,
            save_pose_data = false, skip_execution = false
        )
//    val config = RenderParams(
//        processing_time_limit = 60, find_solution = false, skip_animation = true, recording = false,
//        save_pose_data = false, image_synthesis = listOf()
//    )
//        val scriptObj = Script(script)
        println(client.renderScript(script, config))
        return true
    }

    fun checkScripts(script: List<String>): Boolean {
        if(! client.reset(4)!!.success) throw Exception("Reset Error")
        val initGraph = client.environmentGraph()
        val sofa = initGraph.nodes.filter { it.class_name == "sofa" }[1]
        initGraph.nodes.add(Node(class_name = "cat", category = "Animals", id = 1000, properties = listOf(), states = listOf()))
        initGraph.edges.add(Edge(from_id = 1000, to_id = sofa.id!!, relation_type = "ON"))
        if(! client.expandScene(initGraph)!!.success) throw Exception("Expand Scene Error")
        if(! client.addCharacter()!!.success) throw Exception("Add Character Error")
        val graph = client.environmentGraph()
        val catId = graph.nodes.filter { it.class_name == "cat"}[0]
        val config = RenderParams(
            processing_time_limit = 1, find_solution = false, skip_animation = false, recording = true,
            save_pose_data = false, skip_execution = true
        )
//        val scriptObj = Script(script)
        if(! client.renderScript(script, config)!!.success) throw Exception("Error in Rendering")
        return true
    }


    fun findNodes(name: String) : List<Node> {
        if(! client.reset(4)!!.success) throw Exception("Reset Error")
        if(! client.addCharacter()!!.success) throw Exception("Add Character Error")
        val graph = client.environmentGraph()
        val regex = Regex(name)
        return graph.nodes.filter {
            if(it.class_name != null) {
                regex.containsMatchIn(it.class_name)
            } else {
                false
            }
        }
    }

    fun findNodesByProperty(property: String) : List<Node> {
        if(! client.reset(4)!!.success) throw Exception("Reset Error")
        if(! client.addCharacter()!!.success) throw Exception("Add Character Error")
        val graph = client.environmentGraph()
        return graph.nodes.filter {
            it.properties != null && it.properties.contains(property)
        }
    }

    fun findNodesById(id: Int) : List<Node> {
        if(! client.reset(4)!!.success) throw Exception("Reset Error")
        if(! client.addCharacter()!!.success) throw Exception("Add Character Error")
        val graph = client.environmentGraph()
        return graph.nodes.filter {
            it.id != null && it.id == id
        }
    }

}