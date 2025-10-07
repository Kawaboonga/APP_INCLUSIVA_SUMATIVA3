package com.example.ref01.util


import org.junit.Assert.assertEquals
import org.junit.Test


class TextUtilsTest {
    @Test
    fun `trim y lowercase`() {
        val input = " Hola Mundo "
        val actual = input.trim().lowercase()
        assertEquals("hola mundo", actual)
    }
}