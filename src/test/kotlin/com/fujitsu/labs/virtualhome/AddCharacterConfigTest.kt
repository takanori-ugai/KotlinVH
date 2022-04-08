package com.fujitsu.labs.virtualhome

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

private val logger = KotlinLogging.logger {}

class AddCharacterConfigTest {
    private val format = Json {
        encodeDefaults = true
        explicitNulls = false
    }

    @Test
    fun Test1() {
        val position = Position(1, 2, 3)
        println(
            format.encodeToString(
                AddCharacterConfig(
                    character_resource = "Chars/Male1",
                    mode = "fix_position",
                    character_position = position
                )
            )
        )
        println(
            format.encodeToString(
                AddCharacterConfig(
                    character_resource = "Chars/Male1",
                    mode = "fix_position",
                    initial_room = "kitchen"
                )
            )
        )

    }

}
