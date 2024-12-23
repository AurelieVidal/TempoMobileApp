import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tempomobileapp.ErrorActivity
import com.example.tempomobileapp.R
import com.example.tempomobileapp.ui.components.MainButtonData
import com.example.tempomobileapp.ui.components.decoration
import com.example.tempomobileapp.ui.components.mainButton
import com.example.tempomobileapp.ui.theme.Main1
import com.example.tempomobileapp.ui.theme.Main2
import com.example.tempomobileapp.ui.theme.Main3
import com.example.tempomobileapp.ui.theme.Main4
import com.example.tempomobileapp.ui.theme.Main5
import com.example.tempomobileapp.ui.theme.background
import com.example.tempomobileapp.ui.theme.text

@Composable
@Preview
fun errorLayout() {
    val activity = LocalContext.current as? Activity

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        topDecoration()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.mental_health_sad_guy),
                contentDescription = "App Name",
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 32.dp)
            )

            Text(
                text = "Une erreur est survenue ...",
                fontSize = 24.sp,
                color = text,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .testTag("errorText")
            )

            Text(
                text = "Oups, il y a un petit souci \uD83D\uDE15. Essaie à nouveau un peu plus tard, on s'en occupe !",
                fontSize = 16.sp,
                color = text,
                modifier = Modifier
                    .padding(bottom = 32.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            ) {
                mainButton(
                    MainButtonData(
                        onClick = {
                            (activity as? ErrorActivity)?.closeApplication(activity)
                        },
                        text = "Fermer l'application",
                        color = Main3,
                        modifier = Modifier
                    )
                )
            }
        }
        bottomDecoration()
    }
}

@Composable
private fun topDecoration() {
    decoration(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp),
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
