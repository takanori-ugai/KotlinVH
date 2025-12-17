package com.fujitsu.labs.virtualhome

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

private val logger = KotlinLogging.logger {}

/**
 * This class contains tests for the AddCharacterConfig class.
 */
class AddCharacterConfigTest {
    /**
     * Configuration of Json converter
     */
    @kotlinx.serialization.ExperimentalSerializationApi
    private val format =
        Json {
            encodeDefaults = true
            explicitNulls = false
        }

    /**
     * Tests the serialization of the AddCharacterConfig class.
     */
    @Test
    @kotlinx.serialization.ExperimentalSerializationApi
    fun test1() {
        val position = Position(1, 2, 3)
        logger.info {
            format.encodeToString(
                AddCharacterConfig(
                    characterResource = "Chars/Male1",
                    mode = AddCharacterMode.FixPosition.value,
                    characterPosition = position,
                ),
            )
        }
        logger.info {
            format.encodeToString(
                AddCharacterConfig(
                    characterResource = "Chars/Male1",
                    mode = AddCharacterMode.FixPosition.value,
                    initialRoom = "kitchen",
                ),
            )
        }

        val config = AddCharacterConfig()
        assertEquals("Chars/Male1", config.characterResource)
        assertEquals("random", config.mode)
        assertNull(config.initialRoom)
    }
}
