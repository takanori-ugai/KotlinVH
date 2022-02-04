package com.fujitsu.labs.virtualhome

import kotlinx.serialization.Serializable

@Serializable
data class ImageConfig(
    val mode: String = "normal",
    val image_width: String = "640",
    val image_height: String = "320"
)
