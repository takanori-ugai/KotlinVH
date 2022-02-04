package com.fujitsu.labs.virtualhome

import mu.KotlinLogging
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
private val logger = KotlinLogging.logger {}

class CommonsTest {
    @Test
    fun objectStatesTest() {
        val razor = Commons.objectStates()["razor"]
        logger.info { razor }
        Assertions.assertEquals(3, razor?.size)
        Assertions.assertTrue(razor?.contains("grabbed")!!)
    }

    @Test
    fun propertiesDataTest() {
        val properties = Commons.propertiesData()
        Assertions.assertEquals(390, properties.size)
        Assertions.assertEquals(6, properties["addressbook"]!!.size)
        logger.info { properties["addressbook"] }
    }
}
