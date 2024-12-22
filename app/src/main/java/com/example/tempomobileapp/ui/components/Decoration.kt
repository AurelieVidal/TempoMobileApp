package com.example.tempomobileapp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.tempomobileapp.utils.getDarkenColor
import com.example.tempomobileapp.utils.getLightenColor
import com.gandiva.neumorphic.LightSource
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Pressed
import com.gandiva.neumorphic.shape.RoundedCorner
import kotlinx.coroutines.delay

@Composable
fun decoration(
    modifier: Modifier = Modifier,
    colors: List<Color>,
    animate: Boolean = false,
    startingAnimation: Boolean = false
) {
    val scaleStates = remember { List(5) { Animatable(if (startingAnimation) 0f else 1f) } }
    val animatedColors = remember { mutableStateListOf<Color>().also { it.addAll(colors) } }
    val animatedSizes = remember { mutableStateListOf<Dp>(50.dp, 40.dp, 50.dp, 40.dp, 50.dp) }
    val animatedCornerRadius =
        remember { mutableStateListOf<Dp>(18.dp, 15.dp, 18.dp, 15.dp, 18.dp) }

    if (startingAnimation) {
        LaunchedEffect(Unit) {
            scaleStates.forEachIndexed { index, scale ->
                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 200,
                        delayMillis = index * 75
                    )
                )
            }
        }
    }

    if (animate) {
        animateProperties(
            animatedColors = animatedColors,
            animatedSizes = animatedSizes,
            animatedCornerRadius = animatedCornerRadius
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        animatedRow(
            animatedColors = animatedColors,
            animatedSizes = animatedSizes,
            animatedCornerRadius = animatedCornerRadius,
            scaleStates = scaleStates
        )
    }
}

@Composable
fun animateProperties(
    animatedColors: MutableList<Color>,
    animatedSizes: MutableList<Dp>,
    animatedCornerRadius: MutableList<Dp>
) {
    LaunchedEffect(Unit) {
        delay(1000)
        while (true) {
            delay(1000)
            animatedColors.shuffle()
            for (i in animatedSizes.indices) {
                animatedSizes[i] = if (animatedSizes[i] == 50.dp) 40.dp else 50.dp
                animatedCornerRadius[i] = if (animatedSizes[i] == 50.dp) 18.dp else 15.dp
            }
        }
    }
}

@Composable
fun animatedRow(
    animatedColors: List<Color>,
    animatedSizes: List<Dp>,
    animatedCornerRadius: List<Dp>,
    scaleStates: List<Animatable<Float, AnimationVector1D>>
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(5) { index ->
            animatedBox(
                color = animatedColors[index],
                size = animatedSizes[index],
                cornerRadius = animatedCornerRadius[index],
                scale = scaleStates[index].value
            )
        }
    }
}

@Composable
fun animatedBox(color: Color, size: Dp, cornerRadius: Dp, scale: Float) {
    val animatedSize by animateDpAsState(targetValue = size)
    val animatedCornerRadius by animateDpAsState(targetValue = cornerRadius)
    val animatedColor by animateColorAsState(targetValue = color)

    Box(
        modifier = Modifier
            .size(animatedSize)
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .neu(
                lightShadowColor = getLightenColor(animatedColor),
                darkShadowColor = getDarkenColor(animatedColor),
                shadowElevation = 4.dp,
                lightSource = LightSource.LEFT_TOP,
                shape = Pressed(RoundedCorner(animatedCornerRadius))
            )
            .background(animatedColor, shape = RoundedCornerShape(animatedCornerRadius))
    )
}
