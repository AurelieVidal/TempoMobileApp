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
import junit.framework.TestCase.assertTrue
import loginLayout
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ErrorActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<LoginActivity>()

    @Test
    fun testActivityLaunchesSuccessfully() {
        ActivityScenario.launch(ErrorActivity::class.java)
        composeTestRule.onNodeWithTag("errorText").assertIsDisplayed()
    }

    @Test
    fun testGreetingText() {
        ActivityScenario.launch(ErrorActivity::class.java)
        composeTestRule.onNodeWithText("Une erreur est survenue ...").assertIsDisplayed()
    }

    @Test
    fun testAppClosesOnButtonClick() {
        // Assure-toi que le comportement de fermeture de l'application soit spécifique aux tests
        ErrorActivity.isTesting = true

        // Lance l'activité ErrorActivity
        val scenario = ActivityScenario.launch(ErrorActivity::class.java)

        // Vérifie que le texte d'erreur est affiché (l'activité est en cours)
        composeTestRule.onNodeWithTag("errorText").assertIsDisplayed()

        // Trouve le bouton "Fermer l'application" et effectue un clic
        composeTestRule.onNodeWithText("Fermer l'application").performClick()

        // Vérifie que l'activité est fermée
        scenario.onActivity { activity ->
            // Vérifie que l'activité est fermée sans fermer tout le processus
            assertTrue(activity.isFinishing)
        }

        // Réinitialiser isTesting pour d'autres tests
        ErrorActivity.isTesting = false
    }

}