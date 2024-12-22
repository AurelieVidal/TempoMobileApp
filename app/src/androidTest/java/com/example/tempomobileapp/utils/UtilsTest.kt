package com.example.tempomobileapp.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import junit.framework.TestCase.assertEquals
import org.junit.Test

class UtilsTest {

    @Test
    fun testDarkenColor() {
        val color = Color(0xFF76A9EA)
        val darkenedColor = getDarkenColor(color, 0.2f)
        assert(darkenedColor.toArgb() != color.toArgb())
    }

    @Test
    fun testLightenColor() {
        val color = Color(0xFF76A9EA)
        val lightenedColor = getLightenColor(color, 0.2f)
        assert(lightenedColor.toArgb() != color.toArgb())
    }

    @Test
    fun testDarkenColorEdgeCase() {
        val color = Color(0xFF000000) // Noir
        val darkenedColor = getDarkenColor(color, 0.2f)
        assertEquals(color.toArgb(), darkenedColor.toArgb())
    }

    @Test
    fun testLightenColorEdgeCase() {
        val color = Color(0xFFFFFFFF)
        val lightenedColor = getLightenColor(color, 0.2f)
        assertEquals(color.toArgb(), lightenedColor.toArgb())
    }
}