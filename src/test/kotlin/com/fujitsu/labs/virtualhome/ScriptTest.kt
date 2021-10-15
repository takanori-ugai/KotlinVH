package com.fujitsu.labs.virtualhome

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ScriptTest {


    //"<char0> [WALK] <cat> (366)"
    @Test
    fun scriptLineTest() {
        val script = Script(listOf("<char0> [GRAB] <cat> (366)"))
        // character walk anywhere
        var line = ScriptLine("char0", "WALK", listOf(Obj("table", 366)))
        Assertions.assertTrue(script.checkLine(line))

        // GRABABLE thing can be grabbed
        line = ScriptLine("char0", "GRAB", listOf(Obj("cat", 366)))
        Assertions.assertTrue(script.checkLine(line))

        line = ScriptLine("char0", "GRAB", listOf(Obj("table", 366)))
        Assertions.assertFalse(script.checkLine(line))

        line = ScriptLine("char0", "OPEN", listOf(Obj("addressbook", 366)))
        Assertions.assertTrue(script.checkLine(line))

        line = ScriptLine("char0", "PUTBACK", listOf(Obj("addressbook", 366),Obj("table", 365)))
        Assertions.assertTrue(script.checkLine(line))

        line = ScriptLine("char0", "PUTIN", listOf(Obj("addressbook", 366),Obj("table", 365)))
        Assertions.assertFalse(script.checkLine(line))

        line = ScriptLine("char0", "PUTIN", listOf(Obj("table", 365),Obj("addressbook", 366)))
        Assertions.assertFalse(script.checkLine(line))

        line = ScriptLine("char0", "PUTIN", listOf(Obj("cat", 365),Obj("addressbook", 366)))
        Assertions.assertTrue(script.checkLine(line))

        line = ScriptLine("char0", "POUR", listOf(Obj("juice", 365),Obj("glass", 366)))
        Assertions.assertTrue(script.checkLine(line))

    }

}