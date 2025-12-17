package com.fujitsu.labs.virtualhome

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class ImageConfigTest {
    @Test
    fun `serialize ImageConfig to JSON with default values`() {
        val config = ImageConfig()
        val json = Json { encodeDefaults = true }.encodeToString(config)
        assertEquals("""{"mode":"normal","image_width":"640","image_height":"320"}""", json)
    }

    @Test
    fun `deserialize JSON to ImageConfig with custom values`() {
        val json = """{"mode":"panorama","image_width":"1024","image_height":"512"}"""
        val config = Json.decodeFromString<ImageConfig>(json)
        assertEquals("panorama", config.mode)
        assertEquals("1024", config.imageWidth)
        assertEquals("512", config.imageHeight)
    }
}
