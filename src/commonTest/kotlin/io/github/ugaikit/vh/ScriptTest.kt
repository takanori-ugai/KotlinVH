package io.github.ugaikit.vh

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertFailsWith
import kotlin.test.assertSame
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

    @Test
    fun `parses lines with trailing text because parser uses partial matches`() {
        val script = Script(listOf("<char0> [WALK] <cat> (366) trailing"))

        assertTrue(script.lines.size == 1)
        assertTrue(script.lines[0].character == "char0")
        assertTrue(script.lines[0].action == "WALK")
    }

    @Test
    fun `reuses object instances for repeated object references`() {
        val script = Script(listOf("<char0> [PUTBACK] <cat> (366) <cat> (366)"))

        assertTrue(script.objectPool.size == 1)
        assertSame(script.lines[0].objects[0], script.lines[0].objects[1])
    }

    @Test
    fun `findObj distinguishes object name and id`() {
        val script = Script(listOf("<char0> [WALK] <cat> (1)"))
        val sameNameDifferentId = script.findObj("cat", 2)
        val differentNameSameId = script.findObj("dog", 1)

        assertTrue(script.objectPool.size == 3)
        assertTrue(sameNameDifferentId.name == "cat")
        assertTrue(differentNameSameId.name == "dog")
    }

    @Test
    fun `rejects malformed script lines`() {
        assertFailsWith<IllegalArgumentException> {
            Script(listOf("not a valid script line"))
        }
    }

    @Test
    fun `rejects unknown actions`() {
        assertFailsWith<IllegalArgumentException> {
            Script(listOf("<char0> [UNKNOWN] <cat> (366)"))
        }
    }

    @Test
    fun `checkLine returns false for unknown object properties`() {
        val script = Script(listOf("<char0> [WALK]"))

        assertFalse(
            script.checkLine(
                ScriptLine(
                    "char0",
                    "WALK",
                    listOf(Obj("ghost", 1)),
                ),
            ),
        )
    }

    @Test
    fun `rejects unknown object properties during parsing`() {
        assertFailsWith<IllegalArgumentException> {
            Script(listOf("<char0> [WALK] <ghost> (1)"))
        }
    }

    @Test
    fun `checkLine throws when a line has more objects than the action arity`() {
        val script = Script(listOf("<char0> [WALK]"))

        assertFailsWith<IndexOutOfBoundsException> {
            script.checkLine(
                ScriptLine(
                    "char0",
                    "STANDUP",
                    listOf(Obj("table", 1), Obj("cat", 2)),
                ),
            )
        }
    }
}
