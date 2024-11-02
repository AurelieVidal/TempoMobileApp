package com.example.tempomobileapp.ui.shapes


import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class InputBorderShape(
    private val cornerRadius: CornerRadius = CornerRadius(10.dp.value),
    private val bottomBorderWidth: Float = 8.dp.value
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            addRoundRect(
                RoundRect(
                    rect = Rect(0f, 0f, size.width, size.height - bottomBorderWidth), // Adjust height
                    radiusX = cornerRadius.x,
                    radiusY = cornerRadius.y
                )
            )
            // Add bottom border as a filled rectangle
            //addRect(Rect(0f, size.height - bottomBorderWidth, size.width, size.height))
        }
        return Outline.Generic(path)
    }
/*
    override fun draw(drawScope: DrawScope, layoutDirection: LayoutDirection) {
        // No need to draw here, as the Outline is used for drawing
    }*/
}