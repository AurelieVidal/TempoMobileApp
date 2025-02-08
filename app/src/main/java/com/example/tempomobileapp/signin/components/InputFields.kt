import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tempomobileapp.adapters.TempoApiService
import com.example.tempomobileapp.models.SecurityQuestion
import com.example.tempomobileapp.signin.checkError
import com.example.tempomobileapp.signin.debounceJob
import com.example.tempomobileapp.signin.email
import com.example.tempomobileapp.signin.emailError
import com.example.tempomobileapp.signin.password
import com.example.tempomobileapp.signin.passwordCheck
import com.example.tempomobileapp.signin.passwordError
import com.example.tempomobileapp.signin.phoneError
import com.example.tempomobileapp.signin.phoneNumber
import com.example.tempomobileapp.signin.securityAnswers
import com.example.tempomobileapp.signin.securityErrors
import com.example.tempomobileapp.signin.selectedCountry
import com.example.tempomobileapp.signin.username
import com.example.tempomobileapp.signin.usernameError
import com.example.tempomobileapp.signin.validUsername
import com.example.tempomobileapp.signin.validators.checkPassword
import com.example.tempomobileapp.ui.components.InputFieldData
import com.example.tempomobileapp.ui.components.inputField
import com.example.tempomobileapp.ui.theme.Main1
import com.example.tempomobileapp.ui.theme.Main3
import com.example.tempomobileapp.ui.theme.text
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
internal fun usernameField(coroutineScope: CoroutineScope, dispatcher: CoroutineDispatcher = Dispatchers.IO) {
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = "Mon pseudo",
        textAlign = TextAlign.Left,
        modifier = Modifier.fillMaxWidth(),
        fontSize = 18.sp,
        color = text
    )

    Spacer(modifier = Modifier.height(4.dp))
    inputField(
        InputFieldData(
            value = username,
            onValueChange = { newValue ->

                val value = newValue.trimEnd()
                username = value

                if (value.isNotBlank()) {
                    usernameError.value = null
                    validUsername = true
                }

                // Cancel any running debounce job
                debounceJob?.cancel()

                debounceJob = coroutineScope.launch {
                    delay(1000L)
                    if (value.isNotBlank()) {
                        val isAvailable = withContext(dispatcher) {
                            TempoApiService.getInstance()
                                .checkIfUserAvailable(value)
                        }
                        if (!isAvailable) {
                            usernameError.value = "Ce pseudo est déjà pris."
                            validUsername = false
                        }
                    }
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            isPassword = false,
            borderColor = if (usernameError.value != null) Main3 else Main1,
            testTag = "usernameField"
        )
    )
    if (usernameError.value != null) {
        Text(
            text = usernameError.value ?: "",
            color = Main3,
            fontSize = 12.sp,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth(),
            lineHeight = 14.sp
        )
    }

    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
internal fun emailField() {
    Text(
        text = "Mon email",
        textAlign = TextAlign.Left,
        modifier = Modifier.fillMaxWidth(),
        fontSize = 18.sp,
        color = text
    )
    Spacer(modifier = Modifier.height(4.dp))

    inputField(
        InputFieldData(
            value = email,
            onValueChange = { newValue ->
                email = newValue
                emailError = null
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isPassword = false,
            maxLength = 250,
            borderColor = if (emailError != null) Main3 else Main1,
            testTag = "emailField"
        )
    )
    if (emailError != null) {
        Text(
            text = emailError ?: "",
            color = Main3,
            fontSize = 12.sp,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth(),
            lineHeight = 14.sp
        )
    }

    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
internal fun phoneField() {
    Text(
        text = "Mon téléphone",
        textAlign = TextAlign.Left,
        modifier = Modifier.fillMaxWidth(),
        fontSize = 18.sp,
        color = text
    )

    Spacer(modifier = Modifier.height(4.dp))

    inputPhone(
        phoneData = InputPhoneData(
            onValueChange = { newPhone ->
                phoneNumber = newPhone
                phoneError = null
            },
            phoneNumber = phoneNumber,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            selectedCountry = selectedCountry,
            borderColor = if (phoneError != null) Main3 else Main1,
            testTag = "phoneField"
        )
    )
    if (phoneError != null) {
        Text(
            text = phoneError ?: "",
            color = Main3,
            fontSize = 12.sp,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth(),
            lineHeight = 14.sp
        )
    }

    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
internal fun passwordField() {
    Text(
        text = "Mon mot de passe",
        textAlign = TextAlign.Left,
        modifier = Modifier.fillMaxWidth(),
        fontSize = 18.sp,
        color = text
    )
    Spacer(modifier = Modifier.height(4.dp))

    var passwordTouched by remember { mutableStateOf(false) }

    LaunchedEffect(passwordTouched, password) {
        if (passwordTouched) {
            checkPassword(password, username, email) { error ->
                passwordError = error
            }
        }
    }

    inputField(
        InputFieldData(
            value = password,
            onValueChange = { newValue ->
                if (!passwordTouched) {
                    passwordTouched = true
                }
                password = newValue
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            isPassword = true,
            maxLength = 30,
            borderColor = if (passwordError != null) Main3 else Main1,
            testTag = "passwordField"
        )
    )
    if (passwordError != null) {
        Text(
            text = passwordError ?: "",
            color = Main3,
            fontSize = 12.sp,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth(),
            lineHeight = 14.sp
        )
    }

    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
internal fun confirmPasswordField() {
    Text(
        text = "Confirmer mon mot de passe",
        textAlign = TextAlign.Left,
        modifier = Modifier.fillMaxWidth(),
        fontSize = 18.sp,
        color = text
    )

    Spacer(modifier = Modifier.height(4.dp))

    inputField(
        InputFieldData(
            value = passwordCheck,
            onValueChange = { newValue ->
                passwordCheck = newValue
                checkError = null
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            isPassword = true,
            maxLength = 30,
            borderColor = if (checkError != null) Main3 else Main1,
            testTag = "checkPasswordField"
        )
    )
    if (checkError != null) {
        Text(
            text = checkError ?: "",
            color = Main3,
            fontSize = 12.sp,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth(),
            lineHeight = 14.sp
        )
    }
}

@Composable
internal fun securityQuestionsFields(securityQuestions: List<SecurityQuestion>) {
    val testTags: List<String> = listOf("questionField1", "questionField2", "questionField3")
    securityQuestions.forEachIndexed { index, question ->
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = question.question,
            textAlign = TextAlign.Left,
            modifier = Modifier.fillMaxWidth(),
            fontSize = 18.sp,
            color = text
        )
        Spacer(modifier = Modifier.height(4.dp))

        inputField(
            InputFieldData(
                value = securityAnswers[index].value,
                onValueChange = { newAnswer ->
                    securityAnswers[index].value = newAnswer
                    securityErrors[index].value = null
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                borderColor = if (securityErrors[index].value != null) Main3 else Main1,
                testTag = testTags[index]
            )

        )
        if (securityErrors[index].value != null) {
            Text(
                text = securityErrors[index].value ?: "",
                color = Main3,
                fontSize = 12.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth(),
                lineHeight = 14.sp
            )
        }
    }
}
