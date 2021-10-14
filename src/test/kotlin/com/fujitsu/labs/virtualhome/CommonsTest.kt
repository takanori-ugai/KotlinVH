package com.fujitsu.labs.virtualhome

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CommonsTest {
    @Test
    fun objectStatesTest() {
        val razor2 = Commons.getJsonFromResource("/object_states.json")
        val razor = Commons.objectStates["razor"]
        Assertions.assertEquals(3, razor?.size)

    }
}
