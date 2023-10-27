package com.fujitsu.labs.virtualhome

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class representing rendering parameters.
 *
 * @property randomize_execution Boolean flag to randomize execution, default is false.
 * @property random_seed Seed for random number generator, default is -1.
 * @property processing_time_limit Time limit for processing, default is 20.
 * @property skip_execution Boolean flag to skip execution, default is false.
 * @property output_folder The folder to output the results, default is "Output/".
 * @property file_name_prefix The prefix for the output file name, default is "script".
 * @property frame_rate The frame rate for rendering, default is 5.
 * @property image_synthesis The list of image synthesis modes, default is ["normal"].
 * @property find_solution Boolean flag to find a solution, default is false.
 * @property save_pose_data Boolean flag to save pose data, default is false.
 * @property save_scene_status Boolean flag to save scene status, default is false.
 * @property camera_mode The list of camera modes, default is ["AUTO"].
 * @property recording Boolean flag to enable recording, default is false.
 * @property image_width The width of the image, default is 640.
 * @property image_height The height of the image, default is 480.
 * @property time_scale The scale for time, default is 1.0.
 * @property skip_animation Boolean flag to skip animation, default is false.
 * @property vis_check_object Boolean checked by all cameras or not
 * @property vis_check_character Boolean checked by all camera or not
 * @property vis_check_object_all Boolean checked objects of the camera belong to the room
 * @property out_graph Boolean out graph data every frame or not
 * @property per_frame Int out grap data per frame
 * @property specified_cameras The list of camera indexes, must be used when the camera mode is 'SPECIFIED'. 'SPECIFIED' camera mode's functionalities are exactly same as 'AUTO' mode. 2023/04/21
 * @property diagonal_cameras1 The list of each fixed diagonal camera index for each room, must be used when the camera mode is 'DIAGONAL1'. There should be only one camera index for each room. 2023/04/21
 * @property diagonal_cameras2 The list of each fixed diagonal camera index for each room, must be used when the camera mode is 'DIAGONAL2'. There should be only one camera index for each room. 'DIAGONAL1' and 'DIAGONAL2' camera mode's functionalities are exactly same as 'AUTO' mode. 2023/04/21
 */
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
    val skip_animation: Boolean = false,
    val vis_check_object: Boolean = false,
    val vis_check_character: Boolean = false,
    val vis_check_object_all: Boolean = false,
    val out_graph: Boolean = false,
    val per_frame: Int = 5,
    val specified_cameras: List<String> = emptyList(),
    val diagonal_cameras1: List<String> = emptyList(),
    val diagonal_cameras2: List<String> = emptyList()
)
