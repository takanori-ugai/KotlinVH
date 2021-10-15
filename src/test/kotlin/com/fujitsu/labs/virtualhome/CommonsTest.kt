package com.fujitsu.labs.virtualhome

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CommonsTest {
    @Test
    fun objectStatesTest() {
        val razor = Commons.objectStates()["razor"]
        println(razor)
        Assertions.assertEquals(3, razor?.size)
        Assertions.assertTrue(razor?.contains("grabbed")!!)
    }

    @Test
    fun propertiesDataTest() {
        val properties = Commons.propertiesData()
        Assertions.assertEquals(390, properties.size)
        println(properties["addressbook"])
    }
}
