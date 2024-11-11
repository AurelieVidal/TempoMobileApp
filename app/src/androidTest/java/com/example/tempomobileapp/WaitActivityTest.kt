package com.example.tempomobileapp

import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.tempomobileapp.ui.theme.TempoMobileAppTheme
import org.junit.Rule
import org.junit.Test

class WaitActivityTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testWaitLayoutIsDisplayed() {
        Log.d("Test", "Test is starting")
        composeTestRule.setContent {
            TempoMobileAppTheme {
                WaitLayout(name = "Android")
            }
        }

        // Use the test tag to find the Image
        composeTestRule.onNodeWithTag("AppNameImage").assertIsDisplayed()

        // Or use the content description to find the Image
        // composeTestRule.onNodeWithContentDescription("App Name").assertIsDisplayed()
    }
}