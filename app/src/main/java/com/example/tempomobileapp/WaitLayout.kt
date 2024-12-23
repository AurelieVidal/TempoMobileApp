package com.example.tempomobileapp

import android.graphics.drawable.AnimatedVectorDrawable
import android.util.Log
import android.widget.ImageView
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.tempomobileapp.ui.components.decoration
import com.example.tempomobileapp.ui.theme.Main1
import com.example.tempomobileapp.ui.theme.Main2
import com.example.tempomobileapp.ui.theme.Main3
import com.example.tempomobileapp.ui.theme.Main4
import com.example.tempomobileapp.ui.theme.Main5
import com.example.tempomobileapp.ui.theme.background
import kotlinx.coroutines.delay

@Composable
@Preview
fun waitLayout() {
    Log.d("WaitLayout", "WaitLayout is being rendered")

    var isScalingDone by remember { mutableStateOf(false) }
    var rotationState by remember { mutableStateOf(0f) }
    var isAnimationComplete by remember { mutableStateOf(false) }
    var isRotationComplete by remember { mutableStateOf(false) }

    val largeLogoHeight = 150.dp
    val smallLogoHeight = 111.dp

    val logoSize by animateDpAsState(
        targetValue = if (isScalingDone) largeLogoHeight else smallLogoHeight,
        animationSpec = tween(durationMillis = 3000)
    )

    val logoY by animateDpAsState(
        targetValue = if (isScalingDone) 0.dp else 94.dp,
        animationSpec = tween(durationMillis = 3000)
    )

    LaunchedEffect(Unit) {
        isScalingDone = true
        delay(3000)
        isAnimationComplete = true
        rotationState = 180f
    }

    val rotation by animateFloatAsState(
        targetValue = rotationState,
        animationSpec = tween(durationMillis = 1000)
    )

    LaunchedEffect(rotation) {
        if (rotation == 180f) {
            isRotationComplete = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .testTag("waitScreen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            logoAnimation(isRotationComplete, logoSize, logoY, rotation)
            appNameAnimation(isAnimationComplete, logoY)
        }

        if (isAnimationComplete) {
            decorationTop()
            decorationBottom()
        }
    }
}

@Composable
fun logoAnimation(isRotationComplete: Boolean, logoSize: Dp, logoY: Dp, rotation: Float) {
    if (!isRotationComplete) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Icon",
            modifier = Modifier
                .size(logoSize)
                .offset(y = logoY)
                .rotate(rotation)
        )
    } else {
        AndroidView(
            factory = { context ->
                ImageView(context).apply {
                    setImageResource(R.drawable.anim_logo)
                    (drawable as? AnimatedVectorDrawable)?.start()
                }
            },
            modifier = Modifier
                .size(150.dp)
                .testTag("HourglassAnimation")
        )
    }
}

@Composable
fun appNameAnimation(isAnimationComplete: Boolean, logoY: Dp) {
    if (!isAnimationComplete) {
        Image(
            painter = painterResource(id = R.drawable.text),
            contentDescription = "App Name",
            modifier = Modifier
                .size(200.dp)
                .testTag("AppNameImage")
                .offset(y = logoY)
                .alpha(0f)
        )
    } else {
        AndroidView(
            factory = { context ->
                ImageView(context).apply {
                    setImageResource(R.drawable.anim_name)
                    (drawable as? AnimatedVectorDrawable)?.start()
                }
            },
            modifier = Modifier
                .size(200.dp)
                .testTag("NameAnimation")
                .offset(x = 8.dp)
        )
    }
}

@Composable
fun decorationTop() {
    decoration(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp),
        colors = listOf(Main4, Main2, Main1, Main3, Main5),
        animate = true,
        startingAnimation = true
    )
}

@Composable
fun decorationBottom() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        decoration(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
                .fillMaxWidth(),
            colors = listOf(Main2, Main4, Main5, Main3, Main1),
            animate = true,
            startingAnimation = true

        )
    }
}
