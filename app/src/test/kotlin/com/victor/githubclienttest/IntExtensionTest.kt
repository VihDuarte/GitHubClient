package com.victor.githubclienttest

import com.victor.githubclient.extensions.formatCount
import org.junit.Assert.assertEquals
import org.junit.Test

class IntExtensionTest {

    @Test
    fun whenPass0_then_returnCurrentValue() {
        val value = 0
        assertEquals("0", value.formatCount())
    }

    @Test
    fun whenPassMinorThen100_then_returnCurrentValue() {
        val value = 99
        assertEquals("99", value.formatCount())
    }

    @Test
    fun whenPassMinorThen1000_then_returnCurrentValue() {
        val value = 199
        assertEquals("199", value.formatCount())
    }

    @Test
    fun whenPassMajorThen1000_then_returnCurrentValueFormatted() {
        val value = 1001
        assertEquals("1K", value.formatCount())
    }

    @Test
    fun whenPassMajorThen2000_then_returnCurrentValueFormatted() {
        val value = 2050
        assertEquals("2K", value.formatCount())
    }

    @Test
    fun whenPass10000_then_returnCurrentValueFormatted() {
        val value = 100000
        assertEquals("100K", value.formatCount())
    }
    @Test
    fun whenPassMajorThen1000000_then_returnCurrentValueFormatted() {
        val value = 1000050
        assertEquals("1M", value.formatCount())
    }

    @Test
    fun whenPassMajorThen2000000_then_returnCurrentValueFormatted() {
        val value = 2500000
        assertEquals("2M", value.formatCount())
    }
}