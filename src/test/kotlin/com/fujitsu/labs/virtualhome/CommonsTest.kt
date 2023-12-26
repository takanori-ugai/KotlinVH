package com.fujitsu.labs.virtualhome

import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

private val logger = KotlinLogging.logger {}

/**
 * This class contains tests for the Commons class.
 */
class CommonsTest {
    /**
     * Tests the objectStates method of the Commons class.
     */
    @Test
    fun objectStatesTest() {
        val razor = Commons.objectStates()["razor"]
        logger.info { razor }
        Assertions.assertNotNull(razor, "Razor should not be null")
        Assertions.assertEquals(3, razor?.size)
        Assertions.assertTrue(razor?.contains("grabbed") ?: false, "Razor should contain 'grabbed'")
    }

    /**
     * Tests the propertiesData method of the Commons class.
     */
    @Test
    fun propertiesDataTest() {
        val properties = Commons.propertiesData()
        Assertions.assertEquals(390, properties.size)
        val addressbook = properties["addressbook"]
        Assertions.assertNotNull(addressbook, "Addressbook should not be null")
        Assertions.assertEquals(6, addressbook?.size)
        logger.info { properties["addressbook"] }
    }
}
