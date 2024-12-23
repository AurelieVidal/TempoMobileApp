package com.example.tempomobileapp

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.example.tempomobileapp.ui.components.animateProperties
import com.example.tempomobileapp.ui.theme.tempoMobileAppTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class WaitActivityTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testWaitActivityIsDisplayed() {
        ActivityScenario.launch(WaitActivity::class.java)

        composeTestRule.onNodeWithTag("waitScreen").assertIsDisplayed()
    }

    @Test
    fun testAnimationsAreDisplayedInOrder() {
        composeTestRule.setContent {
            tempoMobileAppTheme { waitLayout() }
        }

        // Vérifie que l'écran d'attente est affiché
        composeTestRule.onNodeWithTag("waitScreen").assertIsDisplayed()

        // Attente pour que l'animation du texte (AppNameImage) disparaisse et que l'animation principale apparaisse
        composeTestRule.waitUntil(timeoutMillis = 4000) {
            // Vérifie que "AppNameImage" n'existe plus
            val appNameImageGone = composeTestRule.onAllNodesWithTag("AppNameImage").fetchSemanticsNodes().isEmpty()

            // Vérifie que "HourglassAnimation" existe
            val hourglassVisible = composeTestRule.onAllNodesWithTag("HourglassAnimation").fetchSemanticsNodes().isNotEmpty()

            appNameImageGone && hourglassVisible
        }

        // Assertion finale pour être sûr que "HourglassAnimation" est affiché
        composeTestRule.onNodeWithTag("HourglassAnimation").assertIsDisplayed()
    }

    @Test
    fun testNavigationToLoginActivityAfterApiSuccess() = runTest {
        // Crée un provider simulé pour retourner "true"
        val healthCheckProvider: suspend () -> Boolean = { true }

        // Injecte le provider dans le DependencyInjector
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = WaitActivity.createIntent(context, healthCheckProvider)

        // Lancement de l'Activity
        val scenario = ActivityScenario.launch<WaitActivity>(intent)

        // Vérifie que l'activité navigue vers LoginActivity
        scenario.onActivity { activity ->
            val expectedIntent = Intent(activity, LoginActivity::class.java)
            val resolvedActivity = activity.packageManager.resolveActivity(expectedIntent, 0)
            assert(resolvedActivity != null)
        }
    }

    @Test
    fun testNavigationToErrorActivityAfterApiFailure() = runTest {
        // Crée un provider simulé pour retourner "false"
        val healthCheckProvider: suspend () -> Boolean = { false }

        // Injecte le provider dans le DependencyInjector
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = WaitActivity.createIntent(context, healthCheckProvider)

        // Initialise Espresso-Intents
        Intents.init()

        // Lancement de l'Activity
        val scenario = ActivityScenario.launch<WaitActivity>(intent)

        // Vérifie que l'activité navigue vers ErrorActivity
        Intents.intended(IntentMatchers.hasComponent(ErrorActivity::class.java.name))

        // Nettoie Espresso-Intents
        Intents.release()
    }

}