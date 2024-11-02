import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tempomobileapp.ui.components.Decoration
import com.example.tempomobileapp.ui.components.InputField
import com.example.tempomobileapp.ui.components.MainButton
import com.example.tempomobileapp.ui.components.SecondaryButton
import com.example.tempomobileapp.ui.theme.KanitFontFamily
import com.example.tempomobileapp.ui.theme.Main1
import com.example.tempomobileapp.ui.theme.Main2
import com.example.tempomobileapp.ui.theme.Main3
import com.example.tempomobileapp.ui.theme.Main4
import com.example.tempomobileapp.ui.theme.Main5
import com.example.tempomobileapp.ui.theme.background
import com.example.tempomobileapp.ui.theme.text

@Composable
fun LoginLayout(navController: NavHostController) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background) // Apply background color to the Box
    ) {
        Decoration(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp), // Add top padding
            colors = listOf(Main4, Main2, Main1, Main3, Main5)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Text(
                text = "Connexion",
                fontSize = 40.sp,
                color = text
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Mon pseudo",
                textAlign = TextAlign.Left,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 24.sp,
                color = text
            )

            InputField(
                value = username,
                onValueChange = { username = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                isPassword = false
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Mon mot de passe",
                textAlign = TextAlign.Left,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 24.sp,
                color = text
            )

            InputField(
                value = password,
                onValueChange = { password = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isPassword = true
            )

            Spacer(modifier = Modifier.height(5.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth() // Fill the entire width
                    .wrapContentSize(Alignment.CenterEnd) // Align content to the right
            ) {
                MainButton(
                    onClick = { /* Handle button click */ },
                    text = "Mot de passe oublié",
                    color = Main3,
                    modifier = Modifier.fillMaxWidth(0.5f),
                    isSmall = true // Create small version
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                MainButton(
                    onClick = {
                        Log.d("Login", "Button clicked")
                        navController.navigate("home")
                    },
                    text = "Connexion",
                    color = Main2
                )

                Spacer(modifier = Modifier.height(16.dp))
                SecondaryButton(
                    onClick = {
                        Log.d("Login", "Button clicked")
                        navController.navigate("home")
                    },
                    text = "M'inscrire",
                    borderColor = Main1
                )
            }

        }
        Decoration(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter) // Align to bottom
                .padding(bottom = 16.dp), // Add bottom padding
            colors = listOf(Main2, Main4, Main5, Main3, Main1)
        )
    }
}