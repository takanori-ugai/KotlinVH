package com.fujitsu.labs.virtualhome

// import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Base64

fun main() {
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
        "<char0> [WALK] <wine> (93)",
        "<char0> [GRAB] <wine> (93)",
        "<char0> [DRINK] <wine> (93)",
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
        "<char0> [WALK] <tablelamp> (76)"
//        "<char0> [PUTON] <clothesshirt> (36)"
//        "<char0> [GRAB] <book> (86)",
//        "<char0> [PLUGIN] <tablelamp> (76)"
    )
    val main = MainTest()
    main.testReset()
    main.testEnvironmentGraph()
    main.testExpandScene()
    main.testExpandScene2()
//    System.exit(0)
    main.testAddCharacter()
    main.testCameraCount()
    main.testVisibleObjects()
    main.testRendering()
//    System.exit(0)

    val script = listOf(
        "<char0> [RUN] <book> (86)",
        "<char0> [FIND] <book> (86)",
        "<char0> [READ] <book> (86)"
//        "<char0> [WALK] <sofa> (139)"
    )
    val scriptObj = Script(script)
    val script2 = listOf(
        "<char0> [RUN] <book> (86)",
        "<char0> [FIND] <book> (86)",
        "<char0> [READ] <book> (86)",
        "<char0> [FALLBACK]"
//        "<char0> [LIFT] <book> (86)",
//        "<char0> [WALK] <sofa> (139)",
//        "<char0> [SIT] <sofa> (139)"
    )
    main.testRendering(script2, 4)
}

/**
 * This class contains various test methods for the VirtualHomeClient.
 */
class MainTest {
    private val sceneNum = 3
    private val client = VirtualHomeClient(host = "localhost")

    /**
     * Tests the reset functionality of the client.
     */
    fun testReset() {
        println("Check : " + client.check().success)
        for (index in 0..8) {
            println("Reset $index " + client.reset(index).success)
        }
        println("Test Reset() Succeeded")
    }

    /**
     * Tests the environment graph functionality of the client.
     */
    fun testEnvironmentGraph() {
        for (it in 0..6) {
            if (!client.reset(it).success) throw VHException("Reset Error")
            val initGraph = client.environmentGraph()
            if (initGraph.nodes.any { it.className == "sofa" }) {
                println("Scene $it succeeded")
            } else {
                println("Sceone $it Failed")
            }
        }
    }

    /**
     * Tests the expand scene functionality of the client.
     */
    fun testExpandScene() {
        for (it in 0..6) {
            if (!client.reset(it).success) throw VHException("Reset Error")
            val initGraph = client.environmentGraph()
            println(client.expandScene(initGraph).success)
            println("Expand Scene $it Succeeded")
        }
    }

    /**
     * Tests the expand scene functionality of the client with a modified graph.
     */
    fun testExpandScene2() {
        for (it in 0..6) {
            if (!client.reset(it).success) throw VHException("Reset Error")
            val graph = client.environmentGraph()
            val sofa = graph.nodes.last { it.className == "sofa" }
            println("Sofa: ${sofa.id}")
            graph.nodes.add(
                Node(
                    className = "cat",
                    category = "Animals",
                    id = 1000,
                    properties = listOf(),
                    states = listOf()
                )
            )
            graph.edges.add(Edge(fromId = 1000, toId = sofa.id!!, relationType = "ON"))
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
    fun testAddCharacter() {
        for (it in 0..6) {
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
    fun testCameraCount() {
        for (it in 0..6) {
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
    fun testVisibleObjects() {
        for (it in 0..6) {
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
    fun testRendering() {
        for (it in 0..6) {
            if (!client.reset(it).success) throw VHException("Reset Error")
            val initGraph = client.environmentGraph()
            if (client.addCharacter().success) {
                val sofa = initGraph.nodes.last { it.className == "book" }
                val script = listOf(
                    "<char0> [WALK] <book> (${sofa.id})",
                    "<char0> [FIND] <book> (${sofa.id})",
                    "<char0> [READ] <book> (${sofa.id})"
                )
                val config = RenderParams(
                    processingTimeLimit = 1,
                    find_solution = false,
                    skip_animation = false,
                    recording = true,
                    save_pose_data = false,
                    skipExecution = false
                )
                if (client.renderScript(script, config).success) {
                    println("Rendering on Scene $it Succeeded")
                } else {
                    println("Rendering on Scene $it Failed")
                }
                val res0 = client.cameraImage(listOf(0))
                val image = Base64.getDecoder().decode(res0.messageList?.get(0))
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
        config: RenderParams = RenderParams(
            processingTimeLimit = 1,
            find_solution = true,
            skip_animation = false,
            recording = true,
            save_pose_data = false,
            skipExecution = false
            out_graph = true
        )
    ) {
        if (!client.reset(scene).success) throw VHException("Reset Error")
        if (client.addCharacter().success) {
            if (client.renderScript(script, config)?.success == true) {
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
    fun checkScripts(script: List<String>): Boolean {
        if (!client.reset(sceneNum).success) throw VHException("Reset Error")
        val initGraph = client.environmentGraph()
        val sofa = initGraph.nodes.last { it.className == "sofa" }
        initGraph.nodes.add(
            Node(
                className = "cat",
                category = "Animals",
                id = 1000,
                properties = listOf(),
                states = listOf()
            )
        )
        initGraph.edges.add(Edge(fromId = 1000, toId = sofa.id!!, relationType = "ON"))
        if (!client.expandScene(initGraph).success) throw VHException("Expand Scene Error")
        if (!client.addCharacter().success) throw VHException("Add Character Error")
        val graph = client.environmentGraph()
        val catId = graph.nodes.last { it.className == "cat" }
        val config = RenderParams(
            processingTimeLimit = 1,
            find_solution = false,
            skip_animation = false,
            recording = true,
            save_pose_data = false,
            skipExecution = true
        )
//        val scriptObj = Script(script)
        if (!client.renderScript(script, config).success) throw VHException("Error in Rendering")
        return true
    }
}
