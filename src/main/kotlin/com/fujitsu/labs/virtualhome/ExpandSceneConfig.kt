package com.fujitsu.labs.virtualhome

import kotlinx.serialization.Serializable

@Serializable
data class ExpandSceneConfig(
    val randomize: Boolean = false,
    val random_seed: Int = -1,
    val animate_character: Boolean = false,
    val ignore_obstacles: Boolean = false,
    val transfer_transform: Boolean = true
)
