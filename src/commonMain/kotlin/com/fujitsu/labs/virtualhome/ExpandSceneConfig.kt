package com.fujitsu.labs.virtualhome

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

/**
 * Data class representing the configuration for expanding a scene.
 * @property randomize Flag indicating whether to randomize the expansion.
 * @property randomSeed Seed for the random number generator.
 * @property animateCharacter Flag indicating whether to animate the character during the expansion.
 * @property ignoreObstacles Flag indicating whether to ignore obstacles during the expansion.
 * @property transferTransform Flag indicating whether to transfer transformations during the expansion.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
data class ExpandSceneConfig(
    val randomize: Boolean = false,
    @SerialName("random_seed")
    val randomSeed: Int = -1,
    @SerialName("animate_character")
    val animateCharacter: Boolean = false,
    @SerialName("ignore_obstacles")
    val ignoreObstacles: Boolean = false,
    @SerialName("transfer_transform")
    val transferTransform: Boolean = true,
)
