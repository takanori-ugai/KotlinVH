package com.fujitsu.labs.virtualhome

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RenderParams(
    @SerialName("randomize_execution")
    val randomizeExecution: Boolean = false,
    @SerialName("random_seed")
    val randomSeed: Int = -1,
    @SerialName("processing_time_limit")
    val processingTimeLimit: Int = 20,
    @SerialName("skip_execution")
    val skipExecution: Boolean = false,
    @SerialName("output_folder")
    val outputFolder: String = "Output/",
    @SerialName("file_name_prefix")
    val fileNamePrefix: String = "script",
    val frame_rate: Int = 5,
    val image_synthesis: List<String> = listOf("normal"),
    val find_solution: Boolean = false,
    val save_pose_data: Boolean = false,
    val save_scene_status: Boolean = false,
    val camera_mode: List<String> = listOf("AUTO"),
    val recording: Boolean = false,
    val image_width: Int = 640,
    val image_height: Int = 480,
    val time_scale: Double = 1.0,
    val skip_animation: Boolean = false
)
