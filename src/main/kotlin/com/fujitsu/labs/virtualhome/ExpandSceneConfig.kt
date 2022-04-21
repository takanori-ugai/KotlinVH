package com.fujitsu.labs.virtualhome

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
    val transferTransform: Boolean = true
)
