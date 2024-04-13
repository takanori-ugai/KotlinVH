package com.fujitsu.labs.virtualhome

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CamDictTest {
    @Test
    fun `serialize CamDict to JSON`() {
        val camDict = CamDict(Position(1, 2), Position(3, 4), 90, "FrontCamera")
        val json = Json.encodeToString(camDict)
        assertEquals("""{"position":{"x":1,"y":2},"rotation":{"x":3,"y":4},"field_view":90,"camera_name":"FrontCamera"}""", json)
    }

    @Test
    fun `deserialize JSON to CamDict`() {
        val json = """{"position":{"x":1,"y":2},"rotation":{"x":3,"y":4},"field_view":90,"camera_name":"FrontCamera"}"""
        val camDict = Json.decodeFromString<CamDict>(json)
        assertEquals(Position(1, 2), camDict.position)
        assertEquals(Position(3, 4), camDict.rotation)
        assertEquals(90, camDict.fieldView)
        assertEquals("FrontCamera", camDict.cameraName)
    }
}
