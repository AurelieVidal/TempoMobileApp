package com.example.tempomobileapp.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import junit.framework.TestCase.assertEquals
import org.junit.Test

class UtilsTest {

    @Test
    fun testDarkenColor() {
        // Couleur de base
        val color = Color(0xFF76A9EA) // Bleu clair

        // Applique la fonction de réduction de luminosité
        val darkenedColor = darkenColor(color, 0.2f)

        // Vérifie que la couleur est plus sombre
        assert(darkenedColor.toArgb() != color.toArgb()) // La couleur devrait être différente
    }

    @Test
    fun testLightenColor() {
        // Couleur de base
        val color = Color(0xFF76A9EA) // Bleu clair

        // Applique la fonction d'augmentation de luminosité
        val lightenedColor = lightenColor(color, 0.2f)

        // Vérifie que la couleur est plus claire
        assert(lightenedColor.toArgb() != color.toArgb()) // La couleur devrait être différente
    }

    @Test
    fun testDarkenColorEdgeCase() {
        // Cas limite où le facteur atteint le minimum
        val color = Color(0xFF000000) // Noir

        // Applique la fonction
        val darkenedColor = darkenColor(color, 0.2f)

        // La couleur devrait rester noire
        assertEquals(color.toArgb(), darkenedColor.toArgb())
    }

    @Test
    fun testLightenColorEdgeCase() {
        // Cas limite où le facteur atteint le maximum
        val color = Color(0xFFFFFFFF) // Blanc

        // Applique la fonction
        val lightenedColor = lightenColor(color, 0.2f)

        // La couleur devrait rester blanche
        assertEquals(color.toArgb(), lightenedColor.toArgb())
    }
}