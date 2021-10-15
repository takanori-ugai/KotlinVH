package com.fujitsu.labs.virtualhome

import kotlinx.serialization.Serializable

@Serializable
data class RenderParams(
    val randomize_execution: Boolean = false,
    val random_seed: Int = -1,
    val processing_time_limit: Int = 20,
    val skip_execution: Boolean = false,
    val output_folder: String = "Output/",
    val file_name_prefix: String = "script",
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
