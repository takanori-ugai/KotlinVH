package io.github.ugaikit.vh

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class representing an image configuration.
 *
 * @property mode The mode of the image, default is "normal".
 * @property imageWidth The width of the image, default is "640".
 * @property imageHeight The height of the image, default is "320".
 */
@Serializable
data class ImageConfig(
    val mode: String = "normal",
    @SerialName("image_width")
    val imageWidth: String = "640",
    @SerialName("image_height")
    val imageHeight: String = "320",
)
