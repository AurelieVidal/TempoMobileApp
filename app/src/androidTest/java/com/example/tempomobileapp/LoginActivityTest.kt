package com.example.tempomobileapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tempomobileapp.ui.theme.tempoMobileAppTheme
import loginLayout
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<LoginActivity>()

    @Test
    fun testActivityLaunches() {
        composeTestRule.activityRule.scenario.onActivity { activity ->
        }
    }

    @Test
    fun testNavigationToHome() {
        composeTestRule.waitForIdle()
        composeTestRule.onRoot().printToLog("TEST")
        composeTestRule.onNodeWithTag("loginButton").performClick()
        composeTestRule.onNodeWithTag("greetingText").assertIsDisplayed()
    }



    @Test
    fun testPasswordVisibilityToggle() {
        // Entrez un mot de passe
        val testPassword = "testPassword123"
        composeTestRule.onNodeWithTag("passwordInputField") // Assurez-vous que ce tag est utilisé
            .performTextInput(testPassword)

        // Vérifie que le mot de passe est masqué par défaut
        composeTestRule.onNodeWithTag("passwordInputField")
            .assertTextContains("•••••••••••••••") // Vérifie le masquage avec des points (ou la transformation visuelle)

        // Bascule la visibilité du mot de passe
        composeTestRule.onNodeWithTag("showPasswordIcon").performClick()

        // Vérifie que le mot de passe est visible
        composeTestRule.onNodeWithTag("passwordInputField")
            .assertTextEquals(testPassword)

    }

}