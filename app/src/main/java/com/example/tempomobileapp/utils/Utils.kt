package com.example.tempomobileapp.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

fun getDarkenColor(color: Color, factor: Float = 0.2f): Color {
    val hsl = FloatArray(3)
    ColorUtils.colorToHSL(color.toArgb(), hsl)

    hsl[2] = if (hsl[2] - factor >= 0f) hsl[2] - factor else 0f

    return Color(ColorUtils.HSLToColor(hsl))
}

fun getLightenColor(color: Color, factor: Float = 0.2f): Color {
    val hsl = FloatArray(3)
    ColorUtils.colorToHSL(color.toArgb(), hsl)

    hsl[2] = if (hsl[2] + factor <= 1f) hsl[2] + factor else 1f

    return Color(ColorUtils.HSLToColor(hsl))
}
