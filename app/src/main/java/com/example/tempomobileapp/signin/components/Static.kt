package com.example.tempomobileapp.signin.components

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tempomobileapp.models.SecurityQuestion
import com.example.tempomobileapp.signin.createUser
import com.example.tempomobileapp.signin.isLoading
import com.example.tempomobileapp.signin.userError
import com.example.tempomobileapp.signin.validators.validateUserInputs
import com.example.tempomobileapp.ui.components.MainButtonData
import com.example.tempomobileapp.ui.components.decoration
import com.example.tempomobileapp.ui.components.mainButton
import com.example.tempomobileapp.ui.theme.Main1
import com.example.tempomobileapp.ui.theme.Main2
import com.example.tempomobileapp.ui.theme.Main3
import com.example.tempomobileapp.ui.theme.Main4
import com.example.tempomobileapp.ui.theme.Main5
import com.example.tempomobileapp.ui.theme.text
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
        text = "Réponds à ces questions en un mot et notes tes réponses quelque " +
            "part, nous en auront besoin dans le cas où tu perdrait tes " +
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
        text = "Attention, un ou plusieurs champs n'est pas rempli correctement, " +
            "essaye de repasser sur les différentes question pour voir si tu " +
            "n'aurais pas oublié quelque chose 😥",
        color = Main3,
        fontSize = 12.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
        lineHeight = 14.sp
    )
}

@Composable
internal fun valitationButton(securityQuestions: List<SecurityQuestion>, context: Context) {
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
                                createUser(securityQuestions, context)
                            } else {
                                Log.d("App", "Invalid user")
                                userError = true
                            }
                            isLoading.value = false
                        }
                    }
                },
                text = "Valider",
                color = Main4,
                modifier = Modifier
            ),
            isLoading = isLoading.value
        )
    }
    Spacer(modifier = Modifier.height(64.dp))
}
