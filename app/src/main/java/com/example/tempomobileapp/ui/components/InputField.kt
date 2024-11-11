package com.example.tempomobileapp.ui.components


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.tempomobileapp.ui.theme.Main1
import com.example.tempomobileapp.ui.theme.Purple40
import com.example.tempomobileapp.ui.theme.background


@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isPassword: Boolean = false
) {
    val borderColor = Main1 // Define border color
    val maxLength = 20
    var passwordVisible by remember { mutableStateOf(false) } // State for password visibility

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
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None, // Conditional transformation
            keyboardOptions = keyboardOptions,
            colors = TextFieldDefaults.colors(
                focusedTextColor = borderColor,
                unfocusedTextColor = borderColor,
                disabledTextColor = borderColor,
                errorTextColor = borderColor,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                //errorContainerColor = FilledTextFieldTokens.ContainerColor.value,
                cursorColor = borderColor,
                //errorCursorColor = FilledTextFieldTokens.ErrorFocusCaretColor.value,
                //selectionColors = LocalTextSelectionColors.current,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
                //    .copy(alpha = FilledTextFieldTokens.DisabledActiveIndicatorOpacity),
                //errorIndicatorColor = FilledTextFieldTokens.ErrorActiveIndicatorColor.value,
                //focusedLeadingIconColor = FilledTextFieldTokens.FocusLeadingIconColor.value,
                //unfocusedLeadingIconColor = FilledTextFieldTokens.LeadingIconColor.value,
                //disabledLeadingIconColor = FilledTextFieldTokens.DisabledLeadingIconColor.value
                //    .copy(alpha = FilledTextFieldTokens.DisabledLeadingIconOpacity),
                //errorLeadingIconColor = FilledTextFieldTokens.ErrorLeadingIconColor.value,
                //focusedTrailingIconColor = FilledTextFieldTokens.FocusTrailingIconColor.value,
                //unfocusedTrailingIconColor = FilledTextFieldTokens.TrailingIconColor.value,
                //disabledTrailingIconColor = FilledTextFieldTokens.DisabledTrailingIconColor.value
                //    .copy(alpha = FilledTextFieldTokens.DisabledTrailingIconOpacity),
                //errorTrailingIconColor = FilledTextFieldTokens.ErrorTrailingIconColor.value,
                //focusedLabelColor = FilledTextFieldTokens.FocusLabelColor.value,
                //unfocusedLabelColor = FilledTextFieldTokens.LabelColor.value,
                //disabledLabelColor = FilledTextFieldTokens.DisabledLabelColor.value
                //    .copy(alpha = FilledTextFieldTokens.DisabledLabelOpacity),
                //errorLabelColor = FilledTextFieldTokens.ErrorLabelColor.value,
                //focusedPlaceholderColor = FilledTextFieldTokens.InputPlaceholderColor.value,
                //unfocusedPlaceholderColor = FilledTextFieldTokens.InputPlaceholderColor.value,
                //disabledPlaceholderColor = FilledTextFieldTokens.DisabledInputColor.value
                //    .copy(alpha = FilledTextFieldTokens.DisabledInputOpacity),
                //errorPlaceholderColor = FilledTextFieldTokens.InputPlaceholderColor.value,
                //focusedSupportingTextColor = FilledTextFieldTokens.FocusSupportingColor.value,
                //unfocusedSupportingTextColor = FilledTextFieldTokens.SupportingColor.value,
                //disabledSupportingTextColor = FilledTextFieldTokens.DisabledSupportingColor.value
                //    .copy(alpha = FilledTextFieldTokens.DisabledSupportingOpacity),
                //errorSupportingTextColor = FilledTextFieldTokens.ErrorSupportingColor.value,
                //focusedPrefixColor = FilledTextFieldTokens.InputPrefixColor.value,
                //unfocusedPrefixColor = FilledTextFieldTokens.InputPrefixColor.value,
                //disabledPrefixColor = FilledTextFieldTokens.InputPrefixColor.value
                //    .copy(alpha = FilledTextFieldTokens.DisabledInputOpacity),
                //errorPrefixColor = FilledTextFieldTokens.InputPrefixColor.value,
                //focusedSuffixColor = FilledTextFieldTokens.InputSuffixColor.value,
                //unfocusedSuffixColor = FilledTextFieldTokens.InputSuffixColor.value,
                //disabledSuffixColor = FilledTextFieldTokens.InputSuffixColor.value
                //    .copy(alpha = FilledTextFieldTokens.DisabledInputOpacity),
                //errorSuffixColor = FilledTextFieldTokens.InputSuffixColor.value,
            ),
            trailingIcon = {
                // Password visibility toggle icon
                if (isPassword) {
                PasswordVisibilityToggleIcon(
                    showPassword = passwordVisible,
                    onTogglePasswordVisibility = { passwordVisible = !passwordVisible },
                    iconColor = borderColor
                )
            }},
            modifier = Modifier
                .fillMaxWidth()
                .background(background, shape = RoundedCornerShape(15.dp)) // White background for inner TextField
        )
    }
}


@Composable
fun PasswordVisibilityToggleIcon(
    showPassword: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    iconColor: Color
) {
    // Determine the icon based on password visibility
    val image = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
    val contentDescription = if (showPassword) "Hide password icon" else "Show password icon"

    // IconButton to toggle password visibility
    IconButton(onClick = onTogglePasswordVisibility) {
        Icon(imageVector = image, contentDescription = contentDescription, tint = iconColor)
    }
}

