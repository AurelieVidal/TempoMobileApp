package com.example.tempomobileapp.signin.components

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.tempomobileapp.R
import com.example.tempomobileapp.signin.resetSignInStates
import com.example.tempomobileapp.ui.components.MainButtonData
import com.example.tempomobileapp.ui.components.dialogCustom
import com.example.tempomobileapp.ui.components.mainButton
import com.example.tempomobileapp.ui.theme.Main5
import com.example.tempomobileapp.ui.theme.background
import com.example.tempomobileapp.ui.theme.text

@Composable
fun successDialog(navController: NavController) {
    dialogCustom(
        onDismiss = { navController.navigate("login") },
        testTag = "signinValidationDialog"
    ) {
        Column {
            val vectorDrawable = ContextCompat.getDrawable(
                LocalContext.current,
                R.drawable.fond_etoiles
            )
            val originalBitmap = Bitmap.createBitmap(
                vectorDrawable!!.intrinsicWidth,
                vectorDrawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )

            val canvas = android.graphics.Canvas(originalBitmap)
            vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
            vectorDrawable.draw(canvas)

            val scaleFactor = 2.05f
            val scaledBitmap = Bitmap.createScaledBitmap(
                originalBitmap,
                (originalBitmap.width * scaleFactor).toInt(),
                (originalBitmap.height * scaleFactor).toInt(),
                true
            )
            val pattern = scaledBitmap.asImageBitmap()

            titleDialog(pattern)
            dialogBody(navController)
        }
    }
}

@Composable
private fun titleDialog(pattern: ImageBitmap) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.Transparent)
    ) {
        titleBackground(pattern)

        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val paint = Paint().asFrameworkPaint().apply {
                isAntiAlias = true
                textSize = 80f
                color = background.toArgb()
                style =
                    android.graphics.Paint.Style.STROKE
                strokeWidth = 8f
            }

            val textPaint = Paint().asFrameworkPaint().apply {
                isAntiAlias = true
                textSize = 80f
                color = text.toArgb()
            }

            val lines = arrayOf(
                "Inscription prise",
                "en compte !"
            )
            val lineSpacing = 10f
            val lineHeight =
                paint.textSize + lineSpacing
            val textStartX = size.width / 2
            val textStartY =
                size.height / 2 - (lines.size - 1) * lineHeight / 2

            lines.forEachIndexed { index, line ->
                val lineX =
                    textStartX - paint.measureText(line) / 2
                val lineY = textStartY + index * lineHeight

                drawIntoCanvas {
                    it.nativeCanvas.drawText(line, lineX, lineY, paint)
                }

                drawIntoCanvas {
                    it.nativeCanvas.drawText(
                        line,
                        lineX,
                        lineY,
                        textPaint
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun titleBackground(pattern: ImageBitmap) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val paint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true

            shader = ImageShader(
                pattern,
                TileMode.Repeated,
                TileMode.Repeated
            )

            alpha =
                (0.3 * 255).toInt()
        }

        val boxHeightPx = size.height
        val boxWidthPx = size.width

        drawIntoCanvas {
            it.nativeCanvas.save()
            it.nativeCanvas.clipRect(
                0f,
                0f,
                boxWidthPx,
                boxHeightPx
            )
            it.nativeCanvas.drawPaint(paint)
            it.nativeCanvas.restore()
        }

        paint.reset()
    }
}

@Composable
private fun dialogBody(navController: NavController) {
    Spacer(modifier = Modifier.height(12.dp))
    Column {
        Image(
            painter = painterResource(id = R.drawable.etoile),
            contentDescription = "App Icon",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(80.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Merci d'avoir pri le temps de rempir le formulaire d'inscription 😁 \n\n" +
                "Il ne reste plus qu’à vérifier tes informations. 🧐.\n\nNous t'avons envoyé un" +
                " mail pour la suite de t’a envoyé un mail avec la suite de la procédure. Une fois " +
                "terminé, tu pourras profiter pleinement de l’application ! 😎 Attention, le lien " +
                "est valide pendant 5 minutes.",
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth(),
            fontSize = 12.sp,
            color = text,
            lineHeight = 14.sp
        )
        Spacer(modifier = Modifier.height(32.dp))
        mainButton(
            MainButtonData(
                onClick = {
                    Log.d("App", "Closing dialog")
                    resetSignInStates()
                    navController.navigate("login")
                },
                text = "Compris !",
                color = Main5,
                modifier = Modifier
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}
