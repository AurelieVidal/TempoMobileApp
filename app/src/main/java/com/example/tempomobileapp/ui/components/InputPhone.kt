import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.tempomobileapp.R
import com.example.tempomobileapp.enums.Country
import com.example.tempomobileapp.enums.countries
import com.example.tempomobileapp.ui.components.InputFieldData
import com.example.tempomobileapp.ui.components.inputField
import com.example.tempomobileapp.ui.theme.Main1
import com.example.tempomobileapp.ui.theme.Main4
import com.example.tempomobileapp.ui.theme.background
import com.example.tempomobileapp.ui.theme.background_dark
import com.example.tempomobileapp.ui.theme.text
import com.example.tempomobileapp.utils.getLightenColor
import com.gandiva.neumorphic.LightSource
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Pressed
import com.gandiva.neumorphic.shape.RoundedCorner

var isPopupOpen by mutableStateOf(false)

@Composable
fun inputPhone(
    phoneData: InputPhoneData
) {
    val borderColor = phoneData.borderColor

    var phoneNumber by remember { mutableStateOf(TextFieldValue(phoneData.selectedCountry.value.phonePrefix)) }

    Box(
        modifier = Modifier
            .background(
                color = borderColor,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(start = 2.dp, top = 2.dp, end = 2.dp, bottom = 8.dp)
    ) {
        Column {
            TextField(
                value = phoneNumber,
                onValueChange = { newValue ->
                    val formattedNumber =
                        formatPhoneNumber(newValue.text, phoneData.selectedCountry.value)
                    phoneNumber = TextFieldValue(
                        formattedNumber,
                        selection = TextRange(formattedNumber.length)
                    )
                    phoneData.onValueChange(formattedNumber)
                },
                leadingIcon = {
                    leadIcon(borderColor, phoneData)
                },
                keyboardOptions = phoneData.keyboardOptions,
                visualTransformation = VisualTransformation.None,
                colors = getTextFieldColors(borderColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(background, shape = RoundedCornerShape(15.dp))
            )

            if (isPopupOpen) {
                flagDialog(
                    onDismiss = { isPopupOpen = false },
                    onCountrySelected = { country ->
                        phoneData.selectedCountry.value = country
                        phoneNumber = TextFieldValue(
                            text = country.phonePrefix,
                            selection = TextRange(country.phonePrefix.length)
                        )
                        isPopupOpen = false
                    }
                )
            }
        }
    }
}

fun formatPhoneNumber(input: String, country: Country): String {
    val formatted = StringBuilder()

    var index = 0
    val digitsOnly = input.replace(Regex("\\D"), "")
    val digitsOnlyPrefix =
        country.phonePrefix.replace(Regex("\\D"), "")

    if (digitsOnly.length <= digitsOnlyPrefix.length - 1) {
        return country.phonePrefix
    }

    formatted.append(country.phonePrefix)
    val format = country.phoneFormat.substring(country.phonePrefix.length)
    val inputSubPrefix = digitsOnly.substring(digitsOnlyPrefix.length)

    for (char in format) {
        if (index >= inputSubPrefix.length) {
            break
        }

        if (char == '#') {
            formatted.append(inputSubPrefix[index])
            index++
        } else if (char == ' ') {
            formatted.append(" ")
        }
    }
    return formatted.toString()
}

@Composable
private fun getTextFieldColors(borderColor: Color): TextFieldColors {
    return TextFieldDefaults.colors(
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
    )
}

@Composable
fun flagDialog(
    onDismiss: () -> Unit,
    onCountrySelected: (Country) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(background)
                .padding(16.dp)
        ) {
            Box(
                Modifier
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
                dialogContent(onCountrySelected)
            }
        }
    }
}

@Composable
private fun dialogContent(onCountrySelected: (Country) -> Unit) {
    Column {
        Text(
            text = "Sélectionnez un pays",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            fontSize = 24.sp,
            color = text
        )
        Spacer(modifier = Modifier.height(16.dp))

        var searchQuery by remember { mutableStateOf("") }

        inputField(
            InputFieldData(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                isPassword = false,
                placeholder = "Rechercher un pays",
                trailingIcon = Icons.Filled.Search
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        val filteredCountries = countries.filter { country ->
            country.name.contains(searchQuery, ignoreCase = true) ||
                country.phonePrefix.contains(
                    searchQuery,
                    ignoreCase = true
                ) ||
                country.code.contains(searchQuery, ignoreCase = true)
        }

        LazyColumn {
            items(filteredCountries) { country ->
                countryRow(country, onCountrySelected)
            }
        }
    }
}

@Composable
fun countryRow(country: Country, onCountrySelected: (Country) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onCountrySelected(country)
            }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = country.flagResId),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = if (country.code != "") "${country.name} (${country.code})" else country.name,
                textAlign = TextAlign.Left,
                fontSize = 14.sp,
                color = text
            )
        }

        Text(
            text = country.phonePrefix,
            textAlign = TextAlign.Left,
            fontSize = 14.sp,
            color = text,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Composable
private fun leadIcon(borderColor: Color, phoneData: InputPhoneData) {
    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(
                indication = rememberRipple(color = Main4, bounded = true),
                interactionSource = remember { MutableInteractionSource() }
            ) { isPopupOpen = true }
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = phoneData.selectedCountry.value.flagResId),
                contentDescription = "Flag",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                painter = painterResource(id = R.drawable.chevron_down),
                contentDescription = "Chevron",
                tint = borderColor,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

/**
 * Properties of a phone input field
 */
data class InputPhoneData(
    val onValueChange: (String) -> Unit,
    var phoneNumber: String,
    var selectedCountry: MutableState<Country>,
    val keyboardOptions: KeyboardOptions,
    val borderColor: Color = Main1
)
