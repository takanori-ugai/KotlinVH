package com.fujitsu.labs.virtualhome

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import org.junit.jupiter.api.Test

private val logger = KotlinLogging.logger {}

class AddCharacterConfigTest {
    @kotlinx.serialization.ExperimentalSerializationApi
    private val format = Json {
        encodeDefaults = true
        explicitNulls = false
    }

    @Test
    @kotlinx.serialization.ExperimentalSerializationApi
    fun test1() {
        val position = Position(1, 2, 3)
        println(
            format.encodeToString(
                AddCharacterConfig(
                    characterResource = "Chars/Male1",
                    mode = "fix_position",
                    characterPosition = position
                )
            )
        )
        println(
            format.encodeToString(
                AddCharacterConfig(
                    characterResource = "Chars/Male1",
                    mode = "fix_position",
                    initialRoom = "kitchen"
                )
            )
        )
    }
}
