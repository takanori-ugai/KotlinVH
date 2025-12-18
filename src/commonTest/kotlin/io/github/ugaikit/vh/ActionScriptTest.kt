package io.github.ugaikit.vh

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ActionScriptTest {
    @Test
    fun `parses action script with character action and objects`() {
        val script = listOf("<Alice> [walk] <door> (1) <key> (2)")
        val result = parseActionScript(script)
        assertEquals(1, result.size)
        val action = result[0]
        assertEquals("Alice", action.character)
        assertEquals("walk", action.action)
        assertEquals(listOf("door" to 1, "key" to 2), action.objects)
    }

    @Test
    fun `parses action script without character`() {
        val script = listOf("[open] <window> (3)")
        val result = parseActionScript(script)
        assertEquals(1, result.size)
        val action = result[0]
        assertNull(action.character)
        assertEquals("open", action.action)
        assertEquals(listOf("window" to 3), action.objects)
    }

    @Test
    fun `parses action script with no objects`() {
        val script = listOf("<Bob> [think]")
        val result = parseActionScript(script)
        assertEquals(1, result.size)
        val action = result[0]
        assertEquals("Bob", action.character)
        assertEquals("think", action.action)
        assertTrue(action.objects.isEmpty())
    }

    @Test
    fun `returns empty list for invalid lines`() {
        val script = listOf("invalid line", "[]", "")
        val result = parseActionScript(script)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `handles multiple valid lines`() {
        val script =
            listOf(
                "<Eve> [sit] <chair> (1)",
                "[jump] <mat> (2)",
                "<Tom> [run]",
            )
        val result = parseActionScript(script)
        assertEquals(3, result.size)
        assertEquals("Eve", result[0].character)
        assertEquals("sit", result[0].action)
        assertEquals(listOf("chair" to 1), result[0].objects)
        assertNull(result[1].character)
        assertEquals("jump", result[1].action)
        assertEquals(listOf("mat" to 2), result[1].objects)
        assertEquals("Tom", result[2].character)
        assertEquals("run", result[2].action)
        assertTrue(result[2].objects.isEmpty())
    }

    @Test
    fun `handles empty script list`() {
        val result = parseActionScript(emptyList())
        assertTrue(result.isEmpty())
    }
}
