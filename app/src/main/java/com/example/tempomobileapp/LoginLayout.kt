import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tempomobileapp.ui.components.MainButtonData
import com.example.tempomobileapp.ui.components.decoration
import com.example.tempomobileapp.ui.components.inputField
import com.example.tempomobileapp.ui.components.mainButton
import com.example.tempomobileapp.ui.components.secondaryButton
import com.example.tempomobileapp.ui.theme.Main1
import com.example.tempomobileapp.ui.theme.Main2
import com.example.tempomobileapp.ui.theme.Main3
import com.example.tempomobileapp.ui.theme.Main4
import com.example.tempomobileapp.ui.theme.Main5
import com.example.tempomobileapp.ui.theme.background
import com.example.tempomobileapp.ui.theme.text

private data class LoginData(
    val username: String,
    val password: String,
    val onUsernameChange: (String) -> Unit,
    val onPasswordChange: (String) -> Unit,
    val navController: NavHostController
)

@Composable
fun loginLayout(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val loginData = LoginData(
        username = username,
        password = password,
        onUsernameChange = { username = it },
        onPasswordChange = { password = it },
        navController = navController
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background) // Apply background color to the Box
    ) {
        topDecoration()
        loginContent(loginData)
        bottomDecoration()
    }
}

@Composable
private fun topDecoration() {
    decoration(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp), // Add top padding
        colors = listOf(Main4, Main2, Main1, Main3, Main5)
    )
}

@Composable
private fun bottomDecoration() {
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
private fun loginContent(
    loginData: LoginData
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        loginTitle()
        usernameInput(loginData.username, loginData.onUsernameChange)
        passwordInput(loginData.password, loginData.onPasswordChange)
        forgotPasswordButton()
        loginButtons(loginData.navController)
    }
}

@Composable
private fun loginTitle() {
    Text(
        text = "Connexion",
        fontSize = 40.sp,
        color = text
    )
}

@Composable
private fun usernameInput(username: String, onUsernameChange: (String) -> Unit) {
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = "Mon pseudo",
        textAlign = TextAlign.Left,
        modifier = Modifier.fillMaxWidth(),
        fontSize = 24.sp,
        color = text
    )
    inputField(
        value = username,
        onValueChange = onUsernameChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        isPassword = false
    )
}

@Composable
private fun passwordInput(password: String, onPasswordChange: (String) -> Unit) {
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = "Mon mot de passe",
        textAlign = TextAlign.Left,
        modifier = Modifier.fillMaxWidth(),
        fontSize = 24.sp,
        color = text
    )
    inputField(
        value = password,
        onValueChange = onPasswordChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        isPassword = true,
        testTag = "passwordInputField"
    )
}

@Composable
private fun forgotPasswordButton() {
    Spacer(modifier = Modifier.height(5.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.CenterEnd)
    ) {
        mainButton(
            MainButtonData(
                onClick = { /* Handle button click */ },
                text = "Mot de passe oublié",
                color = Main3,
                modifier = Modifier.fillMaxWidth(0.5f),
                isSmall = true
            )
        )
    }
}

@Composable
private fun loginButtons(navController: NavHostController) {
    Spacer(modifier = Modifier.height(16.dp))
    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        mainButton(
            MainButtonData(
                onClick = {
                    Log.d("Login", "Button clicked")
                    navController.navigate("home")
                },
                text = "Connexion",
                color = Main2,
                modifier = Modifier.testTag("loginButton")
            )
        )

        Spacer(modifier = Modifier.height(16.dp))
        secondaryButton(
            onClick = {
                Log.d("Login", "Button clicked")
                navController.navigate("home")
            },
            text = "M'inscrire",
            borderColor = Main1
        )
    }
}
