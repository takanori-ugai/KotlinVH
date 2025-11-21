package com.fujitsu.labs.virtualhome

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ExpandSceneConfigTest {
    val format =
        Json {
            encodeDefaults = true
//        explicitNulls = false
        }

    @Test
    fun `serialize ExpandSceneConfig to JSON with default values`() {
        val config = ExpandSceneConfig()
        val json = format.encodeToString(config)
        val expectedJson =
            "{\"randomize\":false,\"random_seed\":-1,\"animate_character\":false," +
                "\"ignore_obstacles\":false,\"transfer_transform\":true}"
        assertEquals(
            expectedJson,
            json,
        )
    }

    @Test
    fun `deserialize JSON to ExpandSceneConfig with custom values`() {
        val json =
            "{\"randomize\":true,\"random_seed\":42,\"animate_character\":true," +
                "\"ignore_obstacles\":true,\"transfer_transform\":false}"
        val config = format.decodeFromString<ExpandSceneConfig>(json)
        assertEquals(true, config.randomize)
        assertEquals(42, config.randomSeed)
        assertEquals(true, config.animateCharacter)
        assertEquals(true, config.ignoreObstacles)
        assertEquals(false, config.transferTransform)
    }
}
