package com.example.tempomobileapp

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import com.example.tempomobileapp.ui.components.decoration
import com.example.tempomobileapp.ui.theme.Main1
import com.example.tempomobileapp.ui.theme.Main2
import com.example.tempomobileapp.ui.theme.Main3
import com.example.tempomobileapp.ui.theme.Main4
import com.example.tempomobileapp.ui.theme.Main5
import com.example.tempomobileapp.ui.theme.background

@Composable
fun waitLayout() {
    Log.d("WaitLayout", "WaitLayout is being rendered")
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        decoration(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
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
                painter = painterResource(id = R.drawable.icon),
                contentDescription = "App Icon",
                modifier = Modifier.size(150.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.name),
                contentDescription = "App Name",
                modifier = Modifier
                    .size(200.dp)
                    .semantics {
                        // Apply semantics for testing
                        testTag = "AppNameImage"
                        contentDescription = "App Name"
                    }
            )
        }
        decoration(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            colors = listOf(Main2, Main4, Main5, Main3, Main1),
            animate = true
        )
    }
}
