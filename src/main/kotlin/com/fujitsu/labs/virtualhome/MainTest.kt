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
    System.exit(0)
    main.testAddCharacter()
    main.testCameraCount()
    main.testVisibleObjects()
    main.testRendering()
    System.exit(0)

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

class MainTest {
    val sceneNum = 3
    private val client = VirtualHomeClient(host = "localhost")

    fun testReset() {
        println("Check : " + client.check()?.success)
        println("Reset 0 " + client.reset(0)?.success)
        println("Reset 1 " + client.reset(1)?.success)
        println("Reset 2 " + client.reset(2)?.success)
        println("Reset 3 " + client.reset(3)?.success)
        println("Reset 4 " + client.reset(4)?.success)
        println("Reset 5 " + client.reset(5)?.success)
        println("Reset 6 " + client.reset(6)?.success)
        println("Reset 7 " + client.reset(7)?.success)
        println("Reset 8 " + client.reset(8)?.success)
        println("Test Reset() Succeeded")
    }

    fun testEnvironmentGraph() {
        (0..6).forEach {
            if (!client.reset(it)!!.success) throw VHException("Reset Error")
            val initGraph = client.environmentGraph()
            if (initGraph.nodes.filter { it.className == "sofa" }.size > 0) {
                println("Scene $it succeeded")
            } else {
                println("Sceone $it Failed")
            }
        }
    }

    fun testExpandScene() {
        (0..6).forEach {
            if (!client.reset(it)!!.success) throw VHException("Reset Error")
            val initGraph = client.environmentGraph()
            println(client.expandScene(initGraph)?.success)
            println("Expand Scene $it Succeeded")
        }
    }

    fun testExpandScene2() {
        if (!client.reset(4)!!.success) throw VHException("Reset Error")
        val graph = client.environmentGraph()
        println(graph.nodes[0])
        println(graph.nodes.size)
        val sofa = graph.nodes.filter { it.className == "sofa" }[0]
        println(sofa)
        graph.nodes.add(Node(className = "cat", category = "Animals", id = 1000, properties = listOf(), states = listOf()))
//        println("ADDRESSBOOK: " + graph.nodes.filter { it.className == "book" })
        graph.edges.add(Edge(fromId = 1000, toId = sofa.id!!, relationType = "ON"))
        println(client.expandScene(graph))
        val graph2 = client.environmentGraph()
        // expandSceneのあとオブジェクトのIDが変化する
        println(graph2.nodes.filter { it.id == 1000 })
        val cat = graph2.nodes.filter { it.className == "cat" }
        println("CAT: " + cat)
        // edgeは適切に修正される
        println(graph2.edges.filter { it.toId == sofa.id })
        println(graph2.edges.filter { it.fromId == sofa.id })
        println(graph2.edges.filter { it.fromId == cat[0].id })

        println(graph2.nodes.size)
    }

    fun testAddCharacter() {
        (0..6).forEach {
            if (!client.reset(it)!!.success) throw VHException("Reset Error")
            if (client.addCharacter()?.success == true) {
                println("AddCharacter on Scene $it Succeeded")
            } else {
                println("Add Character on Scene $it Failed")
            }
        }
    }

    fun testCameraCount() {
        (0..6).forEach {
            if (!client.reset(it)!!.success) throw VHException("Reset Error")
            val cameraA = client.cameraCount()
            client.addCharacter()
            if (cameraA < client.cameraCount()) {
                println("Camera Count on $it Succeed ($cameraA : ${client.cameraCount()})")
            } else {
                println("Camera Count on $it Failed ($cameraA : ${client.cameraCount()})")
            }
        }
    }

    fun testVisibleObjects() {
        (0..6).forEach {
            if (!client.reset(it)!!.success) throw VHException("Reset Error")
            client.addCharacter()
            val cameraN = client.cameraCount()
            if (client.visibleObjects(0).size > 0) {
                if (client.visibleObjects(cameraN - 1).size > 0) {
                    println("Visible Objects on Scene $it Suceeded")
                } else {
                    println("Visible Objects on Scene $it Failed with the last camera")
                }
            } else {
                println("Visible Objects on Scene $it Failed with the first camera")
            }
        }
    }

    fun testRendering() {
        (0..5).forEach {
            if (!client.reset(it)!!.success) throw VHException("Reset Error")
            val initGraph = client.environmentGraph()
            if (client.addCharacter()!!.success) {
                val sofa = initGraph.nodes.filter { it.className == "book" }.last()
                val script = listOf(
                    "<char0> [WALK] <book> (${sofa.id})",
                    "<char0> [FIND] <book> (${sofa.id})",
                    "<char0> [READ] <book> (${sofa.id})"
                )
                val config = RenderParams(
                    processing_time_limit = 1,
                    find_solution = false,
                    skip_animation = false,
                    recording = true,
                    save_pose_data = false,
                    skip_execution = false
                )
                if (client.renderScript(script, config)?.success == true) {
                    println("Rendering on Scene $it Succeeded")
                } else {
                    println("Rendering on Scene $it Failed")
                }
                val res0 = client.cameraImage(listOf(0))
                val image = Base64.getDecoder().decode(res0?.messageList?.get(0))
                Files.write(Paths.get("bfo.png"), image)
                println("  >> Size of Image is ${image.size} : Camera Image Succeeded")
            } else {
                println("Add Character Fail")
            }
        }
    }

    fun testRendering(
        script: List<String>,
        scene: Int,
        config: RenderParams = RenderParams(
            processing_time_limit = 1,
            find_solution = true,
            skip_animation = false,
            recording = true,
            save_pose_data = false,
            skip_execution = false
        )
    ) {
        if (!client.reset(scene)!!.success) throw VHException("Reset Error")
        if (client.addCharacter()!!.success) {
            if (client.renderScript(script, config)?.success == true) {
                println("Rendering on Scene $scene Succeeded")
            } else {
                println("Rendering on Scene $scene Failed")
            }
        } else {
            println("Add Character Fail")
        }
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
                states = listOf()
            )
        )
        initGraph.edges.add(Edge(fromId = 1000, toId = sofa.id!!, relationType = "ON"))
        if (!client.expandScene(initGraph)!!.success) throw VHException("Expand Scene Error")
        if (!client.addCharacter()!!.success) throw VHException("Add Character Error")
        val graph = client.environmentGraph()
        val catId = graph.nodes.filter { it.className == "cat" }[0]
        val config = RenderParams(
            processing_time_limit = 1,
            find_solution = false,
            skip_animation = false,
            recording = true,
            save_pose_data = false,
            skip_execution = true
        )
//        val scriptObj = Script(script)
        if (!client.renderScript(script, config)!!.success) throw VHException("Error in Rendering")
        return true
    }
}
