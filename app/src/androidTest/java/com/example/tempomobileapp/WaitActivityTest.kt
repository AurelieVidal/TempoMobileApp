package com.example.tempomobileapp

import android.content.Intent
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ActivityScenario
import com.example.tempomobileapp.ui.theme.tempoMobileAppTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class WaitActivityTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testWaitLayoutIsDisplayed() {
        composeTestRule.setContent {
            tempoMobileAppTheme {
                waitLayout()
            }
        }

        composeTestRule.onNodeWithTag("AppNameImage").assertIsDisplayed()
    }

    @Test
    fun testWaitActivityIsDisplayed() {
        ActivityScenario.launch(WaitActivity::class.java)

        composeTestRule.onNodeWithTag("waitScreen").assertIsDisplayed()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testNavigationToLoginActivityAfterDelay() = runTest {
        val scenario = ActivityScenario.launch(WaitActivity::class.java)

        delay(30000)

        scenario.onActivity { activity ->
            val intent = Intent(activity, LoginActivity::class.java)
            val currentActivity = activity.baseContext.packageManager.resolveActivity(intent, 0)
            assert(currentActivity != null)
        }
    }

    @Test
    fun testUIResponsiveness() {
        ActivityScenario.launch(WaitActivity::class.java)
        composeTestRule.onNodeWithTag("waitScreen").assertIsDisplayed()
    }
}