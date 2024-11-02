package com.example.tempomobileapp

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tempomobileapp.ui.components.Decoration
import com.example.tempomobileapp.ui.components.InputField
import com.example.tempomobileapp.ui.components.MainButton
import com.example.tempomobileapp.ui.components.SecondaryButton
import com.example.tempomobileapp.ui.theme.Main1
import com.example.tempomobileapp.ui.theme.Main2
import com.example.tempomobileapp.ui.theme.Main3
import com.example.tempomobileapp.ui.theme.Main4
import com.example.tempomobileapp.ui.theme.Main5
import com.example.tempomobileapp.ui.theme.background
import com.example.tempomobileapp.ui.theme.text

@Composable
fun WaitLayout(name: String, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background) // Apply background color to the Box
    ) {
        Decoration(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp), // Add top padding
            colors = listOf(Main4, Main2, Main1, Main3, Main5),
            animate = true
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Image(
                painter = painterResource(id = R.drawable.icon), // Replace with your icon drawable
                contentDescription = "App Icon",
                modifier = Modifier.size(150.dp) // Adjust size as needed
            )
            //Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = R.drawable.name), // Replace with your icon drawable
                contentDescription = "App Name",
                modifier = Modifier.size(200.dp) // Adjust size as needed
            )

        }
        Decoration(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter) // Align to bottom
                .padding(bottom = 16.dp), // Add bottom padding
            colors = listOf(Main2, Main4, Main5, Main3, Main1),
            animate = true
        )
    }

}
