package com.example.tempomobileapp.signin.components

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.tempomobileapp.R
import com.example.tempomobileapp.exceptions.ApiException
import com.example.tempomobileapp.models.SecurityQuestion
import com.example.tempomobileapp.signin.createUser
import com.example.tempomobileapp.signin.isLoading
import com.example.tempomobileapp.signin.resetSignInStates
import com.example.tempomobileapp.signin.validators.validateUserInputs
import com.example.tempomobileapp.ui.components.MainButtonData
import com.example.tempomobileapp.ui.components.decoration
import com.example.tempomobileapp.ui.components.mainButton
import com.example.tempomobileapp.ui.theme.Main1
import com.example.tempomobileapp.ui.theme.Main2
import com.example.tempomobileapp.ui.theme.Main3
import com.example.tempomobileapp.ui.theme.Main4
import com.example.tempomobileapp.ui.theme.Main5
import com.example.tempomobileapp.ui.theme.background
import com.example.tempomobileapp.ui.theme.background_dark
import com.example.tempomobileapp.ui.theme.text
import com.example.tempomobileapp.utils.getLightenColor
import com.gandiva.neumorphic.LightSource
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Pressed
import com.gandiva.neumorphic.shape.RoundedCorner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
internal fun topDecoration() {
    decoration(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp),
        colors = listOf(Main4, Main2, Main1, Main3, Main5)
    )
}

@Composable
internal fun bottomDecoration() {
    Box(modifier = Modifier.fillMaxSize()) {
        decoration(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            colors = listOf(Main2, Main4, Main5, Main3, Main1)
        )
    }
}

@Composable
internal fun topTexts() {
    Text(
        text = "Inscription",
        fontSize = 32.sp,
        color = text,
        modifier = Modifier
            .padding(bottom = 16.dp)
            .testTag("errorText")
    )

    Spacer(modifier = Modifier.height(32.dp))
    Text(
        text = "Mon identité",
        textAlign = TextAlign.Left,
        modifier = Modifier.fillMaxWidth(),
        fontSize = 24.sp,
        color = text
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        text = "Prends un moment pour remplir ces informations. " +
            "Elles resteront bien protégées et nous en avons besoin pour " +
            "créer ton compte en toute sécurité. 😊",
        textAlign = TextAlign.Left,
        modifier = Modifier.fillMaxWidth(),
        fontSize = 12.sp,
        color = text,
        lineHeight = 14.sp
    )
}

@Composable
internal fun midTexts() {
    Spacer(modifier = Modifier.height(64.dp))
    Text(
        text = "Ma sécurité",
        textAlign = TextAlign.Left,
        modifier = Modifier.fillMaxWidth(),
        fontSize = 24.sp,
        color = text
    )

    Spacer(modifier = Modifier.height(4.dp))
    Text(
        text = "Réponds à ces questions en un mot et note bien tes réponses, " +
            "nous en auront besoin dans le cas où tu perdrait tes " +
            "identifiants 😉",
        textAlign = TextAlign.Left,
        modifier = Modifier.fillMaxWidth(),
        fontSize = 12.sp,
        color = text,
        lineHeight = 14.sp
    )
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
internal fun userErrorText() {
    Text(
        text = "Attention, certains champs ne sont pas remplis correctement. Jette un œil aux " +
            "questions, il manque peut-être un détail. 😥",
        color = Main3,
        fontSize = 12.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("errorMessage"),
        lineHeight = 14.sp
    )
}

@Composable
internal fun valitationButton(
    securityQuestions: List<SecurityQuestion>,
    context: Context,
    navController: NavHostController
) {
    val usernameErrorState = remember { mutableStateOf(false) }
    val dialogState = remember { mutableStateOf(false) }
    val dialogErrorState = remember { mutableStateOf(false) }

    if (usernameErrorState.value) {
        userErrorText()
    }
    Spacer(modifier = Modifier.height(32.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 64.dp)
    ) {
        mainButton(
            MainButtonData(
                onClick = {
                    if (!isLoading.value) {
                        isLoading.value = true
                        CoroutineScope(Dispatchers.Main).launch {
                            val validUser = validateUserInputs(securityQuestions)
                            if (validUser) {
                                usernameErrorState.value = false
                                try {
                                    createUser(securityQuestions, context)
                                    dialogState.value = true
                                } catch (e: ApiException) {
                                    Log.d("App", "Error: ${e.message}")
                                    dialogErrorState.value = true
                                }
                            } else {
                                usernameErrorState.value = true
                                dialogState.value = false
                            }
                            isLoading.value = false
                        }
                    }
                },
                text = "Valider",
                color = Main4,
                modifier = Modifier.testTag("validationButton"),
            ),
            isLoading = isLoading.value
        )
    }
    Spacer(modifier = Modifier.height(64.dp))

    if (dialogState.value) {
        successDialog(navController)
    }

    if (dialogErrorState.value) {
        errorDialog(navController)
    }
}

@Composable
internal fun errorDialog(navController: NavController) {
    Dialog(
        onDismissRequest = {
            navController.navigate("login")
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .testTag("signinErrorDialog")
        ) {
            Box(
                Modifier
                    .align(Alignment.Center)
                    .neu(
                        lightShadowColor = getLightenColor(background),
                        darkShadowColor = background_dark,
                        shadowElevation = 4.dp,
                        lightSource = LightSource.LEFT_TOP,
                        shape = Pressed(RoundedCorner(16.dp))
                    )
                    .background(background, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    dialogContent()

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
        }
    }
}

@Composable
private fun dialogContent() {
    Column {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Il y a eu un problème ...",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = text,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
                .testTag("errorText")
        )
        Spacer(modifier = Modifier.height(8.dp))

        Image(
            painter = painterResource(id = R.drawable.mental_health_sad_guy),
            contentDescription = "App Name",
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 32.dp)
                .align(Alignment.CenterHorizontally)
        )

        Text(
            text = "On a un petit souci de notre côté 😕. Désolé pour ça ! On fait de notre mieux " +
                "pour régler ça vite. Réessaye un peu plus tard !",
            fontSize = 16.sp,
            color = text,
            modifier = Modifier
                .padding(start = 2.dp, end = 2.dp, top = 0.dp, bottom = 32.dp)
                .fillMaxWidth()
        )
    }
}
