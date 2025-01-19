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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.tempomobileapp.ui.theme.Main1
import com.example.tempomobileapp.ui.theme.background

@Composable
fun inputField(
    inputFieldData: InputFieldData
) {
    val borderColor = inputFieldData.borderColor
    val maxLength = inputFieldData.maxLength
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
            value = inputFieldData.value,
            onValueChange = { newValue ->
                if (newValue.length <= maxLength) {
                    inputFieldData.onValueChange(newValue)
                }
            },
            placeholder = {
                if (!inputFieldData.placeholder.isNullOrEmpty()) {
                    Text(text = inputFieldData.placeholder, color = Color.Gray)
                }
            },
            visualTransformation = if (inputFieldData.isPassword && !passwordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            keyboardOptions = inputFieldData.keyboardOptions,
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
                if (inputFieldData.isPassword) {
                    passwordVisibilityToggleIcon(
                        showPassword = passwordVisible,
                        onTogglePasswordVisibility = { passwordVisible = !passwordVisible },
                        iconColor = borderColor,
                        iconTestTag = if (passwordVisible) "hidePasswordIcon" else "showPasswordIcon"
                    )
                } else if (inputFieldData.trailingIcon != null) {
                    Icon(
                        imageVector = inputFieldData.trailingIcon,
                        contentDescription = "Custom icon",
                        tint = borderColor
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(background, shape = RoundedCornerShape(15.dp))
                .testTag(inputFieldData.testTag)
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
    val image = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
    val contentDescription = if (showPassword) "Hide password icon" else "Show password icon"

    IconButton(
        onClick = onTogglePasswordVisibility,
        modifier = Modifier.testTag(iconTestTag)
    ) {
        Icon(imageVector = image, contentDescription = contentDescription, tint = iconColor)
    }
}

/**
 * Properties of an input field
 */
data class InputFieldData(
    val value: String,
    val onValueChange: (String) -> Unit,
    val keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    val isPassword: Boolean = false,
    val testTag: String = "",
    val placeholder: String? = null, // Nouveau paramètre pour le placeholder
    val trailingIcon: ImageVector? = null, // Nouvelle option pour une icône personnalisée
    val maxLength: Int = 20,
    val borderColor: Color = Main1
)
