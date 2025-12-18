package io.github.ugaikit.vh

import kotlinx.coroutines.runBlocking
import java.nio.file.Files
import java.nio.file.Paths

private const val MAX_LOOP = 6
private const val NEW_CAT_ID = 1000
private const val SCENE_NUM = 4
private const val TEST_SCENE_NUM = 3
private const val RESET_MAX_LOOP = 8
private const val TEST_PROCESSING_TIME_LIMIT = 1
private const val BOOK_ID = 86
private const val SOFA_ID = 139
private const val WINE_ID = 93
private const val TABLELAMP_ID = 76
private const val CAMERA_ID = 0

fun main() {
    val script0 =
        listOf(
            "<char0> [WALK] <wine> ($WINE_ID)",
            "<char0> [GRAB] <wine> ($WINE_ID)",
            "<char0> [DRINK] <wine> ($WINE_ID)",
            "<char0> [WALK] <tablelamp> ($TABLELAMP_ID)",
        )
    val main = MainTest()
    main.testReset()
    main.testEnvironmentGraph()
    main.testExpandScene()
    main.testExpandScene2()
    main.testCharacterCameras()
    main.testAddCharacter()
    main.testCameraCount()
    main.testVisibleObjects()
    main.testRendering()

    val script =
        listOf(
            "<char0> [RUN] <book> ($BOOK_ID)",
            "<char0> [FIND] <book> ($BOOK_ID)",
            "<char0> [READ] <book> ($BOOK_ID)",
            "<char0> [WALK] <sofa> ($SOFA_ID)",
        )
    val scriptObj = Script(script)
    val script2 =
        listOf(
            "<char0> [RUN] <book> ($BOOK_ID)",
            "<char0> [FIND] <book> ($BOOK_ID)",
            "<char0> [READ] <book> ($BOOK_ID)",
            "<char0> [FALLBACK]",
        )
    main.testRendering(script2, SCENE_NUM)
}

/**
 * This class contains various test methods for the VirtualHomeClient.
 */
class MainTest {
    private val sceneNum = TEST_SCENE_NUM
    private val client = VirtualHomeClient(host = "localhost")

    /**
     * Tests the reset functionality of the client.
     */
    fun testReset() =
        runBlocking {
            val check = client.check()
            if (check.success) {
                println("Check : Success")
                for (index in 0..RESET_MAX_LOOP) {
                    println("Reset $index " + client.reset(index).success)
                }
                println("Test Reset() Succeeded")
            } else {
                println("Check fail")
                throw VHException("Check Error: ${check.message}")
            }
        }

    /**
     * Tests the environment graph functionality of the client.
     */
    fun testEnvironmentGraph() =
        runBlocking {
            for (it in 0..MAX_LOOP) {
                val reset = client.reset(it)
                println(reset)
                if (!reset.success) throw VHException("Reset Error")
                val initGraph = client.environmentGraph()
                if (initGraph.nodes.any { it.className == "sofa" }) {
                    println("Scene $it succeeded")
                } else {
                    println("Sceone $it Failed")
                }
            }
        }

    /**
     * Tests the character cameras functionality by resetting and retrieving camera data for each character.
     *
     * Iterates through character indices (0 to 6) and performs a reset followed by a retrieval of camera data.
     * If the reset is not successful, a VHException is thrown. Otherwise, it prints out the camera data.
     *
     * @throws VHException if the reset operation fails.
     */
    fun testCharacterCameras() =
        runBlocking {
            for (it in 0..MAX_LOOP) {
                if (!client.reset(it).success) throw VHException("Reset Error")
                if (client.addCharacterCamera().success) {
                    val cameras = client.characterCameras()
                    println("CharacterCameras $it : $cameras")
                } else {
                    println("AddCharacterCameras $it Failed")
                }
            }
        }

    /**
     * Tests the expand scene functionality of the client.
     */
    fun testExpandScene() =
        runBlocking {
            for (it in 0..MAX_LOOP) {
                if (!client.reset(it).success) throw VHException("Reset Error")
                val initGraph = client.environmentGraph()
                println(client.expandScene(initGraph).success)
                println("Expand Scene $it Succeeded")
            }
        }

    /**
     * Tests the expand scene functionality of the client with a modified graph.
     */
    fun testExpandScene2() =
        runBlocking {
            for (it in 0..MAX_LOOP) {
                if (!client.reset(it).success) throw VHException("Reset Error")
                val graph = client.environmentGraph()
                val sofa = graph.nodes.last { it.className == "sofa" }
                println("Sofa: ${sofa.id}")
                graph.nodes.add(
                    Node(
                        className = "cat",
                        category = "Animals",
                        id = NEW_CAT_ID,
                        properties = listOf(),
                        states = listOf(),
                    ),
                )
                graph.edges.add(Edge(fromId = NEW_CAT_ID, toId = sofa.id!!, relationType = "ON"))
                println(client.expandScene(graph))
                val graph2 = client.environmentGraph()
                // expandSceneのあとオブジェクトのIDが変化する
                val cat = graph2.nodes.filter { it.className == "cat" }
                println("CAT: ${cat.size} (${cat[0].id})")
                // edgeは適切に修正される
                println(graph2.edges.filter { it.toId == sofa.id && it.fromId == cat[0].id })
                println(graph2.nodes.size == graph.nodes.size)
            }
        }

    /**
     * Tests the add character functionality of the client.
     */
    fun testAddCharacter() =
        runBlocking {
            for (it in 0..MAX_LOOP) {
                if (!client.reset(it).success) throw VHException("Reset Error")
                if (client.addCharacter().success) {
                    println("AddCharacter on Scene $it Succeeded")
                } else {
                    println("Add Character on Scene $it Failed")
                }
            }
        }

    /**
     * Tests the camera count functionality of the client.
     */
    fun testCameraCount() =
        runBlocking {
            for (it in 0..MAX_LOOP) {
                if (!client.reset(it).success) throw VHException("Reset Error")
                val cameraA = client.cameraCount()
                client.addCharacter()
                if (cameraA < client.cameraCount()) {
                    println("Camera Count on $it Succeed ($cameraA : ${client.cameraCount()})")
                } else {
                    println("Camera Count on $it Failed ($cameraA : ${client.cameraCount()})")
                }
            }
        }

    /**
     * Tests the visible objects functionality of the client.
     */
    fun testVisibleObjects() =
        runBlocking {
            for (it in 0..MAX_LOOP) {
                if (!client.reset(it).success) throw VHException("Reset Error")
                client.addCharacter()
                val cameraN = client.cameraCount()
                if (client.visibleObjects(0).isNotEmpty()) {
                    if (client.visibleObjects(cameraN - 1).isNotEmpty()) {
                        println("Visible Objects on Scene $it Suceeded")
                    } else {
                        println("Visible Objects on Scene $it Failed with the last camera")
                    }
                } else {
                    println("Visible Objects on Scene $it Failed with the first camera")
                }
            }
        }

    /**
     * Tests the rendering functionality of the client.
     */
    fun testRendering() =
        runBlocking {
            for (it in 0..MAX_LOOP) {
                if (!client.reset(it).success) throw VHException("Reset Error")
                val initGraph = client.environmentGraph()
                if (client.addCharacter().success) {
                    val sofa = initGraph.nodes.last { it.className == "book" }
                    val script =
                        listOf(
                            "<char0> [WALK] <book> (${sofa.id})",
                            "<char0> [FIND] <book> (${sofa.id})",
                            "<char0> [READ] <book> (${sofa.id})",
                        )
                    val config =
                        RenderParams(
                            processingTimeLimit = TEST_PROCESSING_TIME_LIMIT,
                            findSolution = false,
                            skipAnimation = false,
                            recording = true,
                            savePoseData = false,
                            skipExecution = false,
                        )
                    if (client.renderScript(script, config).success) {
                        println("Rendering on Scene $it Succeeded")
                    } else {
                        println("Rendering on Scene $it Failed")
                    }
                    val res0 = client.cameraImage(listOf(CAMERA_ID))
                    val image = res0[0]
                    Files.write(Paths.get("bfo.png"), image)
                    println("  >> Size of Image is ${image.size} : Camera Image Succeeded")
                } else {
                    println("Add Character Fail")
                }
            }
        }

    /**
     * Tests the rendering functionality of the client with a provided script and scene.
     *
     * @param script The script to be rendered.
     * @param scene The scene to be used.
     * @param config The render parameters.
     */
    fun testRendering(
        script: List<String>,
        scene: Int,
        config: RenderParams =
            RenderParams(
                processingTimeLimit = TEST_PROCESSING_TIME_LIMIT,
                findSolution = true,
                skipAnimation = false,
                recording = true,
                savePoseData = false,
                skipExecution = false,
                outGraph = true,
            ),
    ) = runBlocking {
        if (!client.reset(scene).success) throw VHException("Reset Error")
        if (client.addCharacter().success) {
            if (client.renderScript(script, config).success) {
                println("Rendering on Scene $scene Succeeded")
            } else {
                println("Rendering on Scene $scene Failed")
            }
        } else {
            println("Add Character Fail")
        }
    }

    /**
     * Checks the scripts for errors.
     *
     * @param script The script to be checked.
     * @return True if no errors are found, false otherwise.
     */
    fun checkScripts(script: List<String>): Boolean =
        runBlocking {
            if (!client.reset(sceneNum).success) throw VHException("Reset Error")
            val initGraph = client.environmentGraph()
            val sofa = initGraph.nodes.last { it.className == "sofa" }
            initGraph.nodes.add(
                Node(
                    className = "cat",
                    category = "Animals",
                    id = NEW_CAT_ID,
                    properties = listOf(),
                    states = listOf(),
                ),
            )
            initGraph.edges.add(Edge(fromId = NEW_CAT_ID, toId = sofa.id!!, relationType = "ON"))
            if (!client.expandScene(initGraph).success) throw VHException("Expand Scene Error")
            if (!client.addCharacter().success) throw VHException("Add Character Error")
            val graph = client.environmentGraph()
            graph.nodes.last { it.className == "cat" }
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
}
