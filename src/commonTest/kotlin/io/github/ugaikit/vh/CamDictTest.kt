package io.github.ugaikit.vh

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class CamDictTest {
    @Test
    fun `serialize CamDict to JSON`() {
        val camDict = CamDict(Position(1, 2), Position(3, 4), 90, "FrontCamera")
        val json = Json.encodeToString(camDict)
        val expectedJson =
            "{\"position\":{\"x\":1,\"y\":2},\"rotation\":{\"x\":3,\"y\":4},\"field_view\":90," +
                "\"camera_name\":\"FrontCamera\"}"
        assertEquals(expectedJson, json)
    }

    @Test
    fun `deserialize JSON to CamDict`() {
        val json =
            "{\"position\":{\"x\":1,\"y\":2},\"rotation\":{\"x\":3,\"y\":4},\"field_view\":90," +
                "\"camera_name\":\"FrontCamera\"}"
        val camDict = Json.decodeFromString<CamDict>(json)
        assertEquals(Position(1, 2), camDict.position)
        assertEquals(Position(3, 4), camDict.rotation)
        assertEquals(90, camDict.fieldView)
        assertEquals("FrontCamera", camDict.cameraName)
    }

    @Test
    fun `test default values of CamDict`() {
        val position = Position(0, 0)
        val rotation = Position(0, 0)
        val camDict = CamDict(position, rotation)

        assertEquals(position, camDict.position)
        assertEquals(rotation, camDict.rotation)
        assertEquals(null, camDict.fieldView)
        assertEquals(null, camDict.cameraName)
    }

    @Test
    fun `serialize and deserialize CamDict with default values`() {
        val position = Position(0, 0)
        val rotation = Position(0, 0)
        val camDict = CamDict(position, rotation)
        val json = Json.encodeToString(camDict)
        val deserializedCamDict = Json.decodeFromString<CamDict>(json)

        assertEquals(camDict, deserializedCamDict)
    }
}
