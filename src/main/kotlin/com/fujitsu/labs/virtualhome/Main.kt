package com.fujitsu.labs.virtualhome

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.lang.System.currentTimeMillis
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Base64

private val logger = KotlinLogging.logger {}

private const val CAT_ID = 1000
private const val CAMERA_ID = 89
private const val PROCESSING_TIME_LIMIT = 60
private const val POS_X = 10
private const val POS_Y = 200
private const val POS_Z = 5
private const val ROT_X = 20
private const val ROT_Y = 21
private const val ROT_Z = 22
private const val RESET_NUM = 4
private const val TEST_PROCESSING_TIME_LIMIT = 1
private const val NODE_ID = 1
private const val BOOK_ID = 86
private const val SOFA_ID_2 = 139
private const val WINE_ID = 93
private const val TABLELAMP_ID = 76
private val CAMERA_IDS = listOf(0, 1, 2, 3)
private const val SAVE_CAMERA_ID = 0
private const val CAT_NODE_ID = 0
private const val MAIN_CAMERA_ID = 0
private const val SOFA_INDEX = 1

fun main() {
    val script0 =
        listOf(
            "<char0> [WALK] <wine> ($WINE_ID)",
            "<char0> [GRAB] <wine> ($WINE_ID)",
            " <char0> [DRINK] <wine> ($WINE_ID)",
            "[DRINK] <wine> ($WINE_ID)",
            "  [DRINK] <wine> ($WINE_ID)",
            "<char0> [WALK] <tablelamp> ($TABLELAMP_ID)",
        )
    val main = Main()
    main.testScripts(script0)
    printNodeInformation(main)

    val sq = VirtualHomeClient(host = "localhost")
    val data = VirtualHomeRequest(currentTimeMillis().toInt(), "idle")
    val format = Json { encodeDefaults = true }
    val res = sq.sendRequest(format.encodeToString(data).toByteArray(Charsets.UTF_8))
    logger.info { res?.success }
    executeResetAndEnvironmentGraphRequests(sq)

    val graph = sq.environmentGraph()
    println(graph.nodes[MAIN_CAMERA_ID])
    println(graph.nodes.size)

    val sofa = graph.nodes.filter { it.className == "sofa" }[SOFA_INDEX]
    println(sofa)

    performCameraActions(sq)
    addCatToScene(sq, graph, sofa)
    renderFinalScript(sq)
}

private fun executeResetAndEnvironmentGraphRequests(sq: VirtualHomeClient) {
    println("Check: " + sq.check().success)
    println(sq.reset(RESET_NUM).success)
    CAMERA_IDS.forEach { id ->
        println(sq.visibleObjects(id).size)
    }
}

private fun printNodeInformation(main: Main) {
    println(main.findNodes("tv"))
    println(main.findNodesByProperty("HAS_PLUG"))
    println(main.findNodesById(NODE_ID))
}

private fun performCameraActions(sq: VirtualHomeClient) {
    println(sq.addCamera(Position(POS_X, POS_Y, POS_Z), Position(ROT_X, ROT_Y, ROT_Z)))
    println(sq.cameraCount())
    println(sq.cameraData(listOf(CAMERA_ID)))
}

private fun addCatToScene(sq: VirtualHomeClient, graph: Graph, sofa: Node) {
    val node = Node(className = "cat", category = "Animals", id = CAT_ID, properties = listOf(), states = listOf())
    graph.nodes.add(node)
    println("ADDRESSBOOK: " + graph.nodes.filter { it.className == "book" })
    graph.edges.add(Edge(fromId = CAT_ID, toId = sofa.id!!, relationType = "ON"))
    println(sq.expandScene(graph))
    val graph2 = sq.environmentGraph()
    println(graph2.nodes.filter { it.id == CAT_ID })
    println("CAT: " + graph2.nodes.filter { it.className == "cat" })
    println(graph2.edges.filter { it.toId == sofa.id })
    println(graph2.nodes.size)
    println(sq.addCharacter())
    println(sq.cameraCount())
}

private fun renderFinalScript(sq: VirtualHomeClient) {
    val config =
        RenderParams(
            processingTimeLimit = PROCESSING_TIME_LIMIT,
            findSolution = false,
            skipAnimation = false,
            recording = true,
            savePoseData = true,
        )
    val script2 =
        listOf(
            "<char0> [RUN] <book> ($BOOK_ID)",
            "<char0> [FIND] <book> ($BOOK_ID)",
            "<char0> [READ] <book> ($BOOK_ID)",
            "<char0> [WALK] <sofa> ($SOFA_ID_2)",
            "<char0> [SIT] <sofa> ($SOFA_ID_2)",
        )
    println(sq.renderScript(script2, config))
    saveCameraImage(sq)
}

private fun saveCameraImage(sq: VirtualHomeClient) {
    val res0 = sq.cameraImage(listOf(SAVE_CAMERA_ID))
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
        val sofa = sofas.last()
        println(sofa)
        initGraph.nodes.add(
            Node(
                className = "cat",
                category = "Animals",
                id = CAT_ID,
                properties = listOf(),
                states = listOf(),
            ),
        )
        initGraph.edges.add(Edge(fromId = CAT_ID, toId = sofa.id!!, relationType = "ON"))
        if (client.expandScene(initGraph)!!.success) {
            println("Sucess : Expend Scene")
            val graph = client.environmentGraph()
            val catId = graph.nodes.filter { it.className == "cat" }[CAT_NODE_ID]
            println("CATID: $catId")
        } else {
            println("Failed : Expend Scene")
        }
        if (!client.addCharacter()!!.success) throw VHException("Add Character Error")
        println("Success : Add Character")
        val config =
            RenderParams(
                processingTimeLimit = TEST_PROCESSING_TIME_LIMIT,
                findSolution = false,
                skipAnimation = false,
                recording = true,
                savePoseData = false,
                skipExecution = false,
            )
//    val config = RenderParams(
//        processingTimeLimit = 60, findSolution = false, skipAnimation = true, recording = false,
//        savePoseData = false, imageSynthesis = listOf()
//    )
//        val scriptObj = Script(script)
        println(client.renderScript(script, config))
        return true
    }

    fun checkScripts(script: List<String>): Boolean {
        if (!client.reset(sceneNum)!!.success) throw VHException("Reset Error")
        val initGraph = client.environmentGraph()
        val sofa = initGraph.nodes.filter { it.className == "sofa" }[SOFA_INDEX]
        initGraph.nodes.add(
            Node(
                className = "cat",
                category = "Animals",
                id = CAT_ID,
                properties = listOf(),
                states = listOf(),
            ),
        )
        initGraph.edges.add(Edge(fromId = CAT_ID, toId = sofa.id!!, relationType = "ON"))
        if (!client.expandScene(initGraph)!!.success) throw VHException("Expand Scene Error")
        if (!client.addCharacter()!!.success) throw VHException("Add Character Error")
        val graph = client.environmentGraph()
        val catId = graph.nodes.filter { it.className == "cat" }[CAT_NODE_ID]
        val config =
            RenderParams(
                processingTimeLimit = TEST_PROCESSING_TIME_LIMIT,
                findSolution = false,
                skipAnimation = false,
                recording = true,
                savePoseData = false,
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
