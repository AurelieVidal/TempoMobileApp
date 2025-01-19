import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
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
import com.example.tempomobileapp.ui.theme.Main2
import com.example.tempomobileapp.ui.theme.Main3
import com.example.tempomobileapp.ui.theme.Main4
import com.example.tempomobileapp.ui.theme.background
import com.example.tempomobileapp.ui.theme.background_dark
import com.example.tempomobileapp.ui.theme.text
import com.example.tempomobileapp.utils.getDarkenColor
import com.example.tempomobileapp.utils.getLightenColor
import com.gandiva.neumorphic.LightSource
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Pressed
import com.gandiva.neumorphic.shape.RoundedCorner

data class InputPhoneData(
    val onValueChange: (String) -> Unit,
    var phoneNumber: String, // Ajout pour récupérer la valeur du téléphone
    var selectedCountry: MutableState<Country>, // Ajout du pays sélectionné
    val keyboardOptions: KeyboardOptions,
    val borderColor: Color = Main1
)

@Composable
fun inputPhone(
    phoneData: InputPhoneData
) {
    val borderColor = phoneData.borderColor

    // État pour gérer la saisie du numéro de téléphone
    var phoneNumber by remember { mutableStateOf(TextFieldValue(phoneData.selectedCountry.value.phonePrefix)) }

    // État pour gérer l'ouverture du popup
    var isPopupOpen by remember { mutableStateOf(false) }

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

                    // Formater le numéro de téléphone en utilisant la fonction que tu as déjà
                    val formattedNumber = formatPhoneNumber(newValue.text, phoneData.selectedCountry.value)

                    // Mettre à jour phoneNumber avec le texte formaté et placer le curseur à la fin
                    phoneNumber = TextFieldValue(
                        formattedNumber,
                        selection = TextRange(formattedNumber.length)  // Positionner le curseur à la fin
                    )
                    phoneData.onValueChange(formattedNumber)
                },
                leadingIcon = {
                    // Icône cliquable avec le drapeau et un chevron
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
                },
                keyboardOptions = phoneData.keyboardOptions,
                visualTransformation = VisualTransformation.None,
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
                modifier = Modifier
                    .fillMaxWidth()
                    .background(background, shape = RoundedCornerShape(15.dp))
            )

            // Popup pour afficher les pays disponibles
            if (isPopupOpen) {
                Dialog(
                    onDismissRequest = { isPopupOpen = false },
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
                                            country.phonePrefix.contains(searchQuery, ignoreCase = true) ||
                                            country.code.contains(searchQuery, ignoreCase = true)
                                }

                                LazyColumn {
                                    items(filteredCountries) { country ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    phoneData.selectedCountry.value = country
                                                    phoneNumber = TextFieldValue(
                                                        text = country.phonePrefix,  // Le texte du préfixe du pays
                                                        selection = TextRange(country.phonePrefix.length)  // Placer le curseur à la fin du texte
                                                    )
                                                    isPopupOpen = false
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
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun formatPhoneNumber(input: String, country: Country): String {
    val formatted = StringBuilder()

    var index = 0
    val digitsOnly = input.replace(Regex("\\D"), "") // Ne garder que les chiffres
    val digitsOnly_prefix = country.phonePrefix.replace(Regex("\\D"), "") // Ne garder que les chiffres

    // Empêcher la suppression du prefix
    if (digitsOnly.length <= digitsOnly_prefix.length -1) {
        return country.phonePrefix
    }

    formatted.append(country.phonePrefix) // Ajouter le préfixe

    // Le format sans le préfixe
    val format = country.phoneFormat.substring(country.phonePrefix.length)

    // Utiliser les chiffres restants après le préfixe
    val input_sub_prefix = digitsOnly.substring(digitsOnly_prefix.length)

    // Boucle pour appliquer le format
    for (char in format) {
        if (index >= input_sub_prefix.length) {
            break
        }

        // Si le caractère dans le format est un '#', ajoute le chiffre
        if (char == '#') {
            formatted.append(input_sub_prefix[index])
            index++
        } else if (char == ' ') {
            // Ajoute un espace pour le format
            formatted.append(" ")
        }
    }

    return formatted.toString()
}


