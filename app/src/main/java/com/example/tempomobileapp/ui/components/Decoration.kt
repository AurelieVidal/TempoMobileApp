package com.example.tempomobileapp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.tempomobileapp.ui.theme.Main1
import com.example.tempomobileapp.utils.darkenColor
import com.example.tempomobileapp.utils.lightenColor
import com.gandiva.neumorphic.LightSource
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Pressed
import com.gandiva.neumorphic.shape.RoundedCorner
import kotlinx.coroutines.delay
import kotlin.collections.shuffle

@Composable
fun Decoration(modifier: Modifier = Modifier, colors: List<Color>, animate: Boolean = false) {

    val animatedColors = remember { mutableStateListOf<Color>(*colors.toTypedArray()) }
    val animatedSizes = remember { mutableStateListOf<Dp>(50.dp, 40.dp, 50.dp, 40.dp, 50.dp) } // Initial sizes
    val animatedCornerRadius = remember { mutableStateListOf<Dp>(18.dp, 15.dp, 18.dp, 15.dp, 18.dp) }

    if (animate) {
        LaunchedEffect(Unit) {
            while (true) {
                delay(1000)
                animatedColors.shuffle()

                for (i in 0 until animatedSizes.size) {
                    animatedSizes[i] = if (animatedSizes[i] == 50.dp) 40.dp else 50.dp
                    animatedCornerRadius[i] = if (animatedSizes[i] == 50.dp) 18.dp else 15.dp // Update corner radius
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(5) { index ->
                //val size = if (index % 2 == 0) 50.dp else 40.dp
                val size by animateDpAsState(targetValue = animatedSizes[index])
                //val cornerRadius = if (index % 2 == 0) 18.dp else 15.dp // Adjust corner radius
                val cornerRadius by animateDpAsState(targetValue = animatedCornerRadius[index])
                //val color = colors.getOrNull(index) ?: Main1
                val color by animateColorAsState(targetValue = animatedColors[index])
                Box(
                    modifier = Modifier
                        .size(size)
                        .neu(
                            lightShadowColor = lightenColor(color),
                            darkShadowColor = darkenColor(color),
                            shadowElevation = 4.dp,
                            lightSource = LightSource.LEFT_TOP,
                            shape = Pressed(RoundedCorner(cornerRadius)) // Use adjusted corner radius
                        )
                        .background(color, shape = RoundedCornerShape(cornerRadius)) // Use adjusted corner radius
                )
            }
        }
    }
}