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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
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
    var isAnimationComplete by remember { mutableStateOf(false) } // Nouvelle variable d'état
    var isRotationComplete by remember { mutableStateOf(false) } // Nouvelle variable d'état
    val largeLogoHeight = 150.dp
    val smallLogoHeight = 111.dp



    // Animation de mise à l'échelle
    val logoSize by animateDpAsState(
        targetValue = if (isScalingDone) largeLogoHeight else smallLogoHeight,
        animationSpec = tween(durationMillis = 3000) // Durée de l'animation de scale
    )

    val logoY by animateDpAsState(
        targetValue = if (isScalingDone) 0.dp else 94.dp,
        animationSpec = tween(durationMillis = 3000) // Durée de l'animation de scale
    )

    // Lancer les animations dans l'ordre
    LaunchedEffect(Unit) {

        isScalingDone = true // Lancer l'animation de mise à l'échelle
        delay(3000) // Attendre la fin de la mise à l'échelle
        isAnimationComplete = true // Déclencher la fin de l'animation après 3 secondes
        rotationState = 180f // Lancer la rotation
    }

    // Animation de rotation
    val rotation by animateFloatAsState(
        targetValue = rotationState,
        animationSpec = tween(durationMillis = 1000) // Durée de l'animation de rotation
    )

    LaunchedEffect(rotation) {
        if (rotation == 180f) {
            isRotationComplete = true // Rotation terminée
        }
    }

    // Conteneur principal
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .testTag("waitScreen")
    ) {
        // Contenu principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo avec animation de scale et rotation

            if (!isRotationComplete) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "App Icon",
                    modifier = Modifier
                        .size(logoSize) // Mise à l'échelle anim
                        .offset(y = logoY)
                        .rotate(rotation) // Rotation animée

                )
            } else {
                // Animation de l'icône
                AndroidView(
                    factory = { context ->
                        ImageView(context).apply {
                            setImageResource(R.drawable.anim_logo) // Logo animé
                            val animatedDrawable = drawable as? AnimatedVectorDrawable
                            animatedDrawable?.start()
                        }
                    },
                    modifier = Modifier
                        .size(150.dp)
                        .testTag("HourglassAnimation")
                )
            }

            // Nom de l'application (affiché après la rotation si besoin)
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
                // Animation de l'icône
                AndroidView(
                    factory = { context ->
                        ImageView(context).apply {
                            setImageResource(R.drawable.anim_name) // Logo animé
                            val animatedDrawable = drawable as? AnimatedVectorDrawable
                            animatedDrawable?.start()
                        }
                    },
                    modifier = Modifier
                        .size(200.dp)
                        .testTag("NameAnimation")
                        .offset(x = 8.dp)
                )
            }


        }

        // Décorations affichées après les animations (optionnel)
        if (isAnimationComplete) {
            decoration(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp),
                colors = listOf(Main4, Main2, Main1, Main3, Main5),
                animate = true,
                startingAnimation = true
            )
            decoration(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                colors = listOf(Main2, Main4, Main5, Main3, Main1),
                animate = true,
                startingAnimation = true
            )
        }
    }
}
