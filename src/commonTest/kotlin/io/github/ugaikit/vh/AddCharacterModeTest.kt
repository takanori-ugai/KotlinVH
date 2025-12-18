package io.github.ugaikit.vh

import kotlin.test.Test
import kotlin.test.assertEquals

class AddCharacterModeTest {
    @Test
    fun `test FixPosition toString returns correct value`() {
        val mode = AddCharacterMode.FixPosition
        assertEquals("fix_position", mode.toString(), "FixPosition mode should return 'fix_position'")
    }

    @Test
    fun `test Random toString returns correct value`() {
        val mode = AddCharacterMode.Random
        assertEquals("random", mode.toString(), "Random mode should return 'random'")
    }
}
