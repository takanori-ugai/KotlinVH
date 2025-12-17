package com.fujitsu.labs.virtualhome

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ScriptTest {
    // "<char0> [WALK] <cat> (366)"
    @Test
    fun scriptLineTest() {
        val script = Script(listOf("<char0> [GRAB] <cat> (366)"))
        // character walk anywhere (true)
        var line = ScriptLine("char0", "WALK", listOf(Obj("table", 366)))
        assertTrue(script.checkLine(line))

        // GRABABLE thing can be grabbed (true)
        line = ScriptLine("char0", "GRAB", listOf(Obj("cat", 366)))
        assertTrue(script.checkLine(line))

        // table is not grabable (false)
        line = ScriptLine("char0", "GRAB", listOf(Obj("table", 366)))
        assertFalse(script.checkLine(line))

        // CAN_OPEN thing can OPEN (true)
        line = ScriptLine("char0", "OPEN", listOf(Obj("addressbook", 366)))
        assertTrue(script.checkLine(line))

        // PUT addressbook on table (true)
        line = ScriptLine("char0", "PUTBACK", listOf(Obj("addressbook", 366), Obj("table", 365)))
        assertTrue(script.checkLine(line))

        // PUT addressbook in table (false)
        line = ScriptLine("char0", "PUTIN", listOf(Obj("addressbook", 366), Obj("table", 365)))
        assertFalse(script.checkLine(line))

        // PUT table in addressbook(CAN_OPEN) (false)
        line = ScriptLine("char0", "PUTIN", listOf(Obj("table", 365), Obj("addressbook", 366)))
        assertFalse(script.checkLine(line))

        // PUT cat(GRABBABLE) in addressbook(CAN_OPEN) (true)
        line = ScriptLine("char0", "PUTIN", listOf(Obj("cat", 365), Obj("addressbook", 366)))
        assertTrue(script.checkLine(line))

        // POUR juice(DRIKABLE, POURABLE) to glass(CECIPIENT) (true)
        line = ScriptLine("char0", "POUR", listOf(Obj("juice", 365), Obj("glass", 366)))
        assertTrue(script.checkLine(line))

        // READ addressbook(READABLE) (true)
        line = ScriptLine("char0", "READ", listOf(Obj("addressbook", 365)))
        assertTrue(script.checkLine(line))
    }
}
