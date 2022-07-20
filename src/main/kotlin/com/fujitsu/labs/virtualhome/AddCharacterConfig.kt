package com.fujitsu.labs.virtualhome

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddCharacterConfig(
    @SerialName("character_resource")
    val characterResource: String = "Chars/Male1",
    val mode: String = AddCharacterMode.Random.toString(),
    @SerialName("character_position")
    val characterPosition: Position? = null,
    @SerialName("initial_room")
    val initialRoom: String? = null
)
