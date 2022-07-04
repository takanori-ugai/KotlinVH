package com.fujitsu.labs.virtualhome

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageConfig(
    val mode: String = "normal",
    @SerialName("image_width")
    val imageWidth: String = "640",
    @SerialName("image_height")
    val imageHeight: String = "320"
)
