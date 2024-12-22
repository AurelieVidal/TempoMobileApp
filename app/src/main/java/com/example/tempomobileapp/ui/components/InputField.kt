package com.example.tempomobileapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.tempomobileapp.ui.theme.Main1
import com.example.tempomobileapp.ui.theme.background

@Composable
fun inputField(
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isPassword: Boolean = false,
    testTag: String = ""
) {
    val borderColor = Main1
    val maxLength = 20
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .background(
                color = borderColor,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(start = 2.dp, top = 2.dp, end = 2.dp, bottom = 8.dp)
    ) {
        TextField(
            value = value,
            onValueChange = { newValue ->
                if (newValue.length <= maxLength) {
                    onValueChange(newValue)
                }
            },
            visualTransformation = if (isPassword && !passwordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            keyboardOptions = keyboardOptions,
            colors = TextFieldDefaults.colors(
                focusedTextColor = borderColor,
                unfocusedTextColor = borderColor,
                disabledTextColor = borderColor,
                errorTextColor = borderColor,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                cursorColor = borderColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            trailingIcon = {
                // Password visibility toggle icon
                if (isPassword) {
                    passwordVisibilityToggleIcon(
                        showPassword = passwordVisible,
                        onTogglePasswordVisibility = { passwordVisible = !passwordVisible },
                        iconColor = borderColor,
                        iconTestTag = if (passwordVisible) "hidePasswordIcon" else "showPasswordIcon"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(background, shape = RoundedCornerShape(15.dp))
                .testTag(testTag) // Ajout du testTag global pour l'inputField
        )
    }
}

@Composable
fun passwordVisibilityToggleIcon(
    showPassword: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    iconColor: Color,
    iconTestTag: String
) {
    // Determine the icon based on password visibility
    val image = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
    val contentDescription = if (showPassword) "Hide password icon" else "Show password icon"

    // IconButton to toggle password visibility
    IconButton(
        onClick = onTogglePasswordVisibility,
        modifier = Modifier.testTag(iconTestTag) // Ajout du testTag pour l'icône
    ) {
        Icon(imageVector = image, contentDescription = contentDescription, tint = iconColor)
    }
}
