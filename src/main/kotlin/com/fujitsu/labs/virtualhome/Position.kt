package com.fujitsu.labs.virtualhome

import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class Position(
    val x: Int = 0,
    val y: Int = 0,
    val z: Int = 0
)
