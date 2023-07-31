package com.fujitsu.labs.virtualhome

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class representing the configuration for adding a character.
 *
 * @property characterResource The resource used for the character. Default is "Chars/Male1".
 * @property mode The mode used for adding the character. Default is random.
 * @property characterPosition The position where the character will be added. Null by default.
 * @property initialRoom The initial room where the character will be placed. Null by default.
 */
@Serializable
data class AddCharacterConfig(
    /**
     * The resource used for the character.
     */
    @SerialName("character_resource")
    val characterResource: String = "Chars/Male1",

    /**
     * The mode used for adding the character.
     */
    val mode: String = AddCharacterMode.Random.toString(),

    /**
     * The position where the character will be added.
     */
    @SerialName("character_position")
    val characterPosition: Position? = null,

    /**
     * The initial room where the character will be placed.
     */
    @SerialName("initial_room")
    val initialRoom: String? = null
)
