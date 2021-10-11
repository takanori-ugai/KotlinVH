package com.fujitsu.labs.virtualhome

import kotlinx.serialization.Serializable

@Serializable
data class AddCharacter(
    val character_resource: String = "Chars/Male1",
    val mode: String = "random",
    val character_position: Position? = null,
    val initial_room: String? = null
)