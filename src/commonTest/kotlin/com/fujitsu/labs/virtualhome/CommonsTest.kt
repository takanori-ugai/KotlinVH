package com.fujitsu.labs.virtualhome

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

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
        assertNotNull(razor, "Razor should not be null")
        assertEquals(3, razor.size)
        assertTrue(razor.contains("grabbed"), "Razor should contain 'grabbed'")
    }

    /**
     * Tests the propertiesData method of the Commons class.
     */
    @Test
    fun propertiesDataTest() {
        val properties = Commons.propertiesData()
        assertEquals(390, properties.size)
        val addressbook = properties["addressbook"]
        assertNotNull(addressbook, "Addressbook should not be null")
        assertEquals(6, addressbook.size)
        logger.info { properties["addressbook"] }
    }
}
