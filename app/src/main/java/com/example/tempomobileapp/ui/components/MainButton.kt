package com.example.tempomobileapp.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tempomobileapp.ui.theme.background
import com.example.tempomobileapp.utils.getDarkenColor
import com.example.tempomobileapp.utils.getLightenColor
import com.gandiva.neumorphic.LightSource
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Pressed
import com.gandiva.neumorphic.shape.RoundedCorner
import com.spr.jetpack_loading.components.indicators.PulsatingDot

@Composable
fun mainButton(
    mainButtonData: MainButtonData,
    isLoading: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val buttonModifier = getButtonModifier(isPressed, mainButtonData.color, mainButtonData.isSmall)

    Box(
        modifier = mainButtonData.modifier
            .fillMaxWidth()
            .height(if (mainButtonData.isSmall) 30.dp else 60.dp)
            .then(buttonModifier)
            .clip(RoundedCornerShape(if (mainButtonData.isSmall) 24.dp else 18.dp))
            .background(mainButtonData.color)
            .clickable(
                onClick = mainButtonData.onClick,
                indication = null,
                interactionSource = interactionSource
            )
            .padding(horizontal = if (mainButtonData.isSmall) 0.dp else 8.dp),
        contentAlignment = Alignment.Center
    ) {
        buttonText(mainButtonData.text, isPressed, mainButtonData.isSmall, isLoading)
    }
}

@Composable
private fun getButtonModifier(isPressed: Boolean, color: Color, isSmall: Boolean): Modifier {
    return if (!isPressed) {
        Modifier.neu(
            lightShadowColor = getLightenColor(color),
            darkShadowColor = getDarkenColor(color),
            shadowElevation = 4.dp,
            lightSource = LightSource.LEFT_TOP,
            shape = Pressed(RoundedCorner(if (isSmall) 24.dp else 18.dp))
        )
    } else {
        Modifier
    }
}

@Composable
private fun buttonText(
    text: String,
    isPressed: Boolean,
    isSmall: Boolean,
    isLoading: Boolean = false
) {
    val fontSize = if (isSmall) 12f else 18f
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(durationMillis = 100)
    )
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            PulsatingDot(
                color = background,
                ballDiameter = 30f,
                animationDuration = 1000,
                horizontalSpace = 40f
            )
        }
    } else {
        Text(
            text = text,
            color = Color.White,
            fontSize = fontSize.sp,
            modifier = Modifier
                .scale(scale)
                .graphicsLayer { clip = true }
                .wrapContentSize(Alignment.Center)
        )
    }
}

/**
 * Information to create a main button.
 */
data class MainButtonData(
    val onClick: () -> Unit,
    val text: String,
    val modifier: Modifier = Modifier,
    val color: Color,
    val isSmall: Boolean = false
)
