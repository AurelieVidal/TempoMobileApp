package com.example.tempomobileapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tempomobileapp.ui.theme.tempoMobileAppTheme
import loginLayout
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<LoginActivity>()

    @Test
    fun testActivityLaunchesSuccessfully() {
        ActivityScenario.launch(HomeActivity::class.java)
        composeTestRule.onNodeWithTag("greetingText").assertIsDisplayed()
    }

    @Test
    fun testGreetingText() {
        ActivityScenario.launch(HomeActivity::class.java)
        composeTestRule.onNodeWithText("Hello Android!").assertIsDisplayed()
    }

}