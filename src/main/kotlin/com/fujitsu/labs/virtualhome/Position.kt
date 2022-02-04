package com.fujitsu.labs.virtualhome

import kotlinx.serialization.Serializable

@Serializable
data class Position(
    val x: Int = 0,
    val y: Int = 0,
    val z: Int = 0
)
