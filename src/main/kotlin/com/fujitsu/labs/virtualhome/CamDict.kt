package com.fujitsu.labs.virtualhome

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a dictionary entry for a camera.
 *
 * @param position The position of the camera.
 * @param rotation The rotation of the camera.
 * @property fieldView The field of view of the camera.
 * @property cameraName The name of the camera. It's optional and default is null.
 */
@Serializable
data class CamDict(
    val position: Position,
    val rotation: Position,
    /**
     * The field of view of the camera.
     */
    @SerialName("field_view")
    val fieldView: Int,
    /**
     * The name of the camera.
     */
    @SerialName("camera_name")
    val cameraName: String? = null,
)
