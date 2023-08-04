package com.fujitsu.labs.virtualhome

import kotlinx.serialization.Serializable

/**
 * Data class representing a position in a 3D space.
 *
 * @property x The x-coordinate of the position, default is 0.
 * @property y The y-coordinate of the position, default is 0.
 * @property z The z-coordinate of the position, default is 0.
 */
@Serializable
data class Position(
    val x: Int = 0,
    val y: Int = 0,
    val z: Int = 0
)
