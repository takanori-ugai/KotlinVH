package io.github.ugaikit.vh

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.runBlocking
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlin.random.Random

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
private const val CAM_0 = 0
private const val CAM_1 = 1
private const val CAM_2 = 2
private const val CAM_3 = 3
private val CAMERA_IDS = listOf(CAM_0, CAM_1, CAM_2, CAM_3)
private const val SAVE_CAMERA_ID = 0
private const val CAT_NODE_ID = 0
private const val MAIN_CAMERA_ID = 0
private const val SOFA_INDEX = 1
private const val MAIN_SCENE_NUM = 0

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
    runBlocking {
        val data =
            VirtualHomeRequest(
                Random.nextInt(),
                "idle",
            )
        val res = sq.sendRequest(data)
        logger.info { res.success }
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
}

private suspend fun executeResetAndEnvironmentGraphRequests(sq: VirtualHomeClient) {
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

private suspend fun performCameraActions(sq: VirtualHomeClient) {
    println(sq.addCamera(Position(POS_X, POS_Y, POS_Z), Position(ROT_X, ROT_Y, ROT_Z)))
    println(sq.cameraCount())
    println(sq.cameraData(listOf(CAMERA_ID)))
}

private suspend fun addCatToScene(
    sq: VirtualHomeClient,
    graph: Graph,
    sofa: Node,
) {
    val sofaId = sofa.id ?: throw VHException("Sofa node does not have an id")
    val node = Node(className = "cat", category = "Animals", id = CAT_ID, properties = listOf(), states = listOf())
    graph.nodes.add(node)
    println("ADDRESSBOOK: " + graph.nodes.filter { it.className == "book" })
    graph.edges.add(Edge(fromId = CAT_ID, toId = sofaId, relationType = "ON"))
    println(sq.expandScene(graph))
    val graph2 = sq.environmentGraph()
    println(graph2.nodes.filter { it.id == CAT_ID })
    println("CAT: " + graph2.nodes.filter { it.className == "cat" })
    println(graph2.edges.filter { it.toId == sofaId })
    println(graph2.nodes.size)
    println(sq.addCharacter())
    println(sq.cameraCount())
}

private suspend fun renderFinalScript(sq: VirtualHomeClient) {
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

private suspend fun saveCameraImage(sq: VirtualHomeClient) {
    val res0 = sq.cameraImage(listOf(SAVE_CAMERA_ID))
    val image = res0[0]
    val path = Path("bfo.png")
    SystemFileSystem.sink(path).buffered().use { sink ->
        sink.write(image)
    }
}

class Main {
    val sceneNum = MAIN_SCENE_NUM
    private val client = VirtualHomeClient(host = "localhost")

    fun testScripts(script: List<String>): Boolean =
        runBlocking {
            if (!client.reset(sceneNum).success) throw VHException("Reset Error")
            val initGraph = client.environmentGraph()
            val sofas = initGraph.nodes.filter { it.className == "sofa" }
            println(sofas)
            val sofa = sofas.last()
            val sofaId = sofa.id ?: throw VHException("Sofa node does not have an id")
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
            initGraph.edges.add(Edge(fromId = CAT_ID, toId = sofaId, relationType = "ON"))
            if (client.expandScene(initGraph).success) {
                println("Sucess : Expend Scene")
                val graph = client.environmentGraph()
                val catId = graph.nodes.filter { it.className == "cat" }[CAT_NODE_ID]
                println("CATID: $catId")
            } else {
                println("Failed : Expend Scene")
            }
            if (!client.addCharacter().success) throw VHException("Add Character Error")
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
            return@runBlocking true
        }

    fun checkScripts(script: List<String>): Boolean =
        runBlocking {
            if (!client.reset(sceneNum).success) throw VHException("Reset Error")
            val initGraph = client.environmentGraph()
            val sofa = initGraph.nodes.filter { it.className == "sofa" }[SOFA_INDEX]
            val sofaId = sofa.id ?: throw VHException("Sofa node does not have an id")
            initGraph.nodes.add(
                Node(
                    className = "cat",
                    category = "Animals",
                    id = CAT_ID,
                    properties = listOf(),
                    states = listOf(),
                ),
            )
            initGraph.edges.add(Edge(fromId = CAT_ID, toId = sofaId, relationType = "ON"))
            if (!client.expandScene(initGraph).success) throw VHException("Expand Scene Error")
            if (!client.addCharacter().success) throw VHException("Add Character Error")
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
            if (!client.renderScript(script, config).success) throw VHException("Error in Rendering")
            return@runBlocking true
        }

    fun findNodes(name: String): List<Node> =
        runBlocking {
            if (!client.reset(sceneNum).success) throw VHException("Reset Error")
            if (!client.addCharacter().success) throw VHException("Add Character Error")
            val graph = client.environmentGraph()
            val regex = Regex(name)
            return@runBlocking graph.nodes.filter {
                if (it.className != null) {
                    regex.containsMatchIn(it.className)
                } else {
                    false
                }
            }
        }

    fun findNodesByProperty(property: String): List<Node> =
        runBlocking {
            if (!client.reset(sceneNum).success) throw VHException("Reset Error")
            if (!client.addCharacter().success) throw VHException("Add Character Error")
            val graph = client.environmentGraph()
            return@runBlocking graph.nodes.filter {
                it.properties != null && it.properties.contains(property)
            }
        }

    fun findNodesById(id: Int): List<Node> =
        runBlocking {
            if (!client.reset(sceneNum).success) throw VHException("Reset Error")
            if (!client.addCharacter().success) throw VHException("Add Character Error")
            val graph = client.environmentGraph()
            return@runBlocking graph.nodes.filter {
                it.id != null && it.id == id
            }
        }
}
