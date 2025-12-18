package io.github.ugaikit.vh

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class RenderParamsTest {
    @Test
    fun `serialize RenderParams with default values`() {
        val params = RenderParams()
        val json = Json { encodeDefaults = true }.encodeToString(params)
        // JS uses 1 for 1.0 double serialization sometimes
        val expected =
            """
                {"randomize_execution":false,
                "random_seed":-1,
                "processing_time_limit":20,
                "skip_execution":false,
                "output_folder":"Output/",
                "file_name_prefix":"script",
                "frame_rate":5,
                "image_synthesis":["normal"],
                "find_solution":false,
                "save_pose_data":false,
                "save_scene_status":false,
                "camera_mode":["AUTO"],
                "recording":false,
                "image_width":640,
                "image_height":480,
                "time_scale":1.0,
                "skip_animation":false,
                "vis_check_object":false,
                "vis_check_character":false,
                "vis_check_object_all":false,
                "out_graph":false,
                "per_frame":5,
                "specified_cameras":[],
                "diagonal_cameras1":[],
                "diagonal_cameras2":[]}
                """.replace(" ", "").replace("\n", "")

        // Handling both 1.0 and 1
        if (json.contains("\"time_scale\":1,")) {
            assertEquals(
                expected.replace("\"time_scale\":1.0", "\"time_scale\":1.0"),
                json.replace("\"time_scale\":1,", "\"time_scale\":1.0,"),
            )
        } else {
            assertEquals(expected, json)
        }
    }

    @Test
    fun `deserialize JSON to RenderParams with custom values`() {
        val json =
            """
            {"randomize_execution":true,
            "random_seed":42,
            "processing_time_limit":30,
            "skip_execution":true,
            "output_folder":"CustomOutput/",
            "file_name_prefix":"custom_",
            "frame_rate":24,
            "image_synthesis":["depth","normal"],
            "find_solution":true,
            "save_pose_data":true,
            "save_scene_status":true,
            "camera_mode":["MANUAL"],
            "recording":true,
            "image_width":1920,
            "image_height":1080,
            "time_scale":0.5,
            "skip_animation":true,
            "vis_check_object":true,
            "vis_check_character":true,
            "vis_check_object_all":true,
            "out_graph":true,
            "per_frame":10,
            "specified_cameras":["Cam1","Cam2"],
            "diagonal_cameras1":["Diag1"],
            "diagonal_cameras2":["Diag2"]}
            """.replace(" ", "").replace("\n", "")
        val params = Json.decodeFromString<RenderParams>(json)
        assertEquals(true, params.randomizeExecution)
        assertEquals(42, params.randomSeed)
        assertEquals(30, params.processingTimeLimit)
        assertEquals(true, params.skipExecution)
        assertEquals("CustomOutput/", params.outputFolder)
        assertEquals("custom_", params.fileNamePrefix)
        assertEquals(24, params.frameRate)
        assertEquals(listOf("depth", "normal"), params.imageSynthesis)
        assertEquals(true, params.findSolution)
        assertEquals(true, params.savePoseData)
        assertEquals(true, params.saveSceneStatus)
        assertEquals(listOf("MANUAL"), params.cameraMode)
        assertEquals(true, params.recording)
        assertEquals(1920, params.imageWidth)
        assertEquals(1080, params.imageHeight)
        assertEquals(0.5, params.timeScale)
        assertEquals(true, params.skipAnimation)
        assertEquals(true, params.visCheckObject)
        assertEquals(true, params.visCheckCharacter)
        assertEquals(true, params.visCheckObjectAll)
        assertEquals(true, params.outGraph)
        assertEquals(10, params.perFrame)
        assertEquals(listOf("Cam1", "Cam2"), params.specifiedCameras)
        assertEquals(listOf("Diag1"), params.diagonalCameras1)
        assertEquals(listOf("Diag2"), params.diagonalCameras2)
    }
}
