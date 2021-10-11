package com.fujitsu.labs.virtualhome
import com.sun.jdi.StringReference
import kotlinx.serialization.*

@Serializable
data class ImageConfig (
    val mode: String = "normal",
    val image_width: String = "640",
    val image_height: String = "320"
        )