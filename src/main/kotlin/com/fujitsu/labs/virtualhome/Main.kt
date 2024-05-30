package com.fujitsu.labs.virtualhome

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.lang.System.currentTimeMillis
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Base64

private val logger = KotlinLogging.logger {}

fun main() {
    val format =
        Json {
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
    val script0 =
        listOf(
            "<char0> [WALK] <wine> (93)",
            "<char0> [GRAB] <wine> (93)",
            " <char0> [DRINK] <wine> (93)",
            "[DRINK] <wine> (93)",
            "  [DRINK] <wine> (93)",
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
//        "<char0> [WALK] <sofa> (139)",
//        "<char0> [SIT] <sofa> (139)",
//        "<char0> [STANDUP] <sofa> (139)",
            "<char0> [WALK] <tablelamp> (76)",
//        "<char0> [PUTON] <clothesshirt> (36)"
//        "<char0> [GRAB] <book> (86)",
//        "<char0> [PLUGIN] <tablelamp> (76)"
        )
    val main = Main()
//    main.checkScripts(script0)
    main.testScripts(script0)
    printNodeInformation(main)
//    System.exit(0)

    val data = VirtualHomeRequest(currentTimeMillis().toInt(), "idle")
    val sq = VirtualHomeClient(host = "localhost")
    val res = sq.sendRequest(format.encodeToString(data).toByteArray(Charsets.UTF_8))
    logger.info { res?.success }
    executeResetAndEnvironmentGraphRequests(sq)
    val graph = sq.environmentGraph()
    println(graph.nodes[0])
    println(graph.nodes.size)
    val sofa = graph.nodes.filter { it.className == "sofa" }[1]
    println(sofa)
    println(sq.addCamera(Position(10, 200, 5), Position(20, 21, 22)))
    println(sq.cameraCount())
    println(sq.cameraData(listOf(89)))
//    val image = Base64.getDecoder().decode(sq.cameraImage(listOf(89))?.messageList?.get(0))
//    Files.write(Paths.get("cfo.png"), image)
//    System.exit(0)
    graph.nodes.add(Node(className = "cat", category = "Animals", id = 1000, properties = listOf(), states = listOf()))
    println("ADDRESSBOOK: " + graph.nodes.filter { it.className == "book" })
    graph.edges.add(Edge(fromId = 1000, toId = sofa.id!!, relationType = "ON"))
    println(sq.expandScene(graph))
    val graph2 = sq.environmentGraph()
    // expandSceneのあとオブジェクトのIDが変化する
    println(graph2.nodes.filter { it.id == 1000 })
    println("CAT: " + graph2.nodes.filter { it.className == "cat" })
    // edgeは適切に修正される
    println(graph2.edges.filter { it.toId == sofa.id })
    println(graph2.nodes.size)
    // キャラクターを追加するのはシーンを作った後、キャラクターを追加してからシーンを作成するとサーバが止まる
    println(sq.addCharacter())
    println(sq.cameraCount())
    val config =
        RenderParams(
            processingTimeLimit = 60,
            find_solution = false,
            skip_animation = false,
            recording = true,
            save_pose_data = true,
        )
//    val config = RenderParams(
//        processing_time_limit = 60, find_solution = true, skip_animation = true, recording = true,
//        save_pose_data = false, image_synthesis = listOf()
//    )
    // val script = listOf("<char0> [WALK] <cat> (366)")
    val script =
        listOf(
            "<char0> [RUN] <book> (86)",
            "<char0> [FIND] <book> (86)",
            "<char0> [READ] <book> (86)",
//        "<char0> [WALK] <sofa> (139)"
        )
    val scriptObj = Script(script)
//    val scriptObj2 = Script(listOf("<char0> [WALK] <cat> (366) <dog> (377)"))
//    val scriptObj3 = Script(listOf("[WALK] <cat> (366) <dog> (377)"))
    val script2 =
        listOf(
            "<char0> [RUN] <book> (86)",
            "<char0> [FIND] <book> (86)",
            "<char0> [READ] <book> (86)",
//        "<char0> [LIFT] <book> (86)",
            "<char0> [WALK] <sofa> (139)",
            "<char0> [SIT] <sofa> (139)",
        )
    println(sq.renderScript(script2, config))
    saveCameraImage(sq)
}

private fun executeResetAndEnvironmentGraphRequests(sq: VirtualHomeClient) {
    println("Check: " + sq.check().success)
    println(sq.reset(4).success)
    println(sq.visibleObjects(0))
    println(sq.visibleObjects(1).size)
    println(sq.visibleObjects(2).size)
    println(sq.visibleObjects(3).size)
}

private fun printNodeInformation(main: Main) {
    println(main.findNodes("tv"))
    println(main.findNodesByProperty("HAS_PLUG"))
    println(main.findNodesById(1))
}

private fun saveCameraImage(sq: VirtualHomeClient) {
    val res0 = sq.cameraImage(listOf(0))
    val image = Base64.getDecoder().decode(res0[0])
    Files.write(Paths.get("bfo.png"), image)
}

class Main {
    val sceneNum = 4
    private val client = VirtualHomeClient(host = "localhost")

    fun testScripts(script: List<String>): Boolean {
        if (!client.reset(sceneNum)!!.success) throw VHException("Reset Error")
        val initGraph = client.environmentGraph()
        val sofas = initGraph.nodes.filter { it.className == "sofa" }
        println(sofas)
        val sofa = sofas[sofas.size - 1]
        println(sofa)
        initGraph.nodes.add(
            Node(
                className = "cat",
                category = "Animals",
                id = 1000,
                properties = listOf(),
                states = listOf(),
            ),
        )
        initGraph.edges.add(Edge(fromId = 1000, toId = sofa.id!!, relationType = "ON"))
        if (client.expandScene(initGraph)!!.success) {
            println("Sucess : Expend Scene")
            val graph = client.environmentGraph()
            val catId = graph.nodes.filter { it.className == "cat" }[0]
            println("CATID: $catId")
        } else {
            println("Failed : Expend Scene")
        }
        if (!client.addCharacter()!!.success) throw VHException("Add Character Error")
        println("Success : Add Character")
        val config =
            RenderParams(
                processingTimeLimit = 1,
                find_solution = false,
                skip_animation = false,
                recording = true,
                save_pose_data = false,
                skipExecution = false,
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
        if (!client.reset(sceneNum)!!.success) throw VHException("Reset Error")
        val initGraph = client.environmentGraph()
        val sofa = initGraph.nodes.filter { it.className == "sofa" }[1]
        initGraph.nodes.add(
            Node(
                className = "cat",
                category = "Animals",
                id = 1000,
                properties = listOf(),
                states = listOf(),
            ),
        )
        initGraph.edges.add(Edge(fromId = 1000, toId = sofa.id!!, relationType = "ON"))
        if (!client.expandScene(initGraph)!!.success) throw VHException("Expand Scene Error")
        if (!client.addCharacter()!!.success) throw VHException("Add Character Error")
        val graph = client.environmentGraph()
        val catId = graph.nodes.filter { it.className == "cat" }[0]
        val config =
            RenderParams(
                processingTimeLimit = 1,
                find_solution = false,
                skip_animation = false,
                recording = true,
                save_pose_data = false,
                skipExecution = true,
            )
//        val scriptObj = Script(script)
        if (!client.renderScript(script, config)!!.success) throw VHException("Error in Rendering")
        return true
    }

    fun findNodes(name: String): List<Node> {
        if (!client.reset(sceneNum)!!.success) throw VHException("Reset Error")
        if (!client.addCharacter()!!.success) throw VHException("Add Character Error")
        val graph = client.environmentGraph()
        val regex = Regex(name)
        return graph.nodes.filter {
            if (it.className != null) {
                regex.containsMatchIn(it.className)
            } else {
                false
            }
        }
    }

    fun findNodesByProperty(property: String): List<Node> {
        if (!client.reset(sceneNum)!!.success) throw VHException("Reset Error")
        if (!client.addCharacter()!!.success) throw VHException("Add Character Error")
        val graph = client.environmentGraph()
        return graph.nodes.filter {
            it.properties != null && it.properties.contains(property)
        }
    }

    fun findNodesById(id: Int): List<Node> {
        if (!client.reset(sceneNum)!!.success) throw VHException("Reset Error")
        if (!client.addCharacter()!!.success) throw VHException("Add Character Error")
        val graph = client.environmentGraph()
        return graph.nodes.filter {
            it.id != null && it.id == id
        }
    }
}
