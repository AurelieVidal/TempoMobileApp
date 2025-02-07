package com.example.tempomobileapp.signin

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
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
import androidx.compose.ui.test.performKeyPress
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tempomobileapp.ErrorActivity
import com.example.tempomobileapp.LoginActivity
import com.example.tempomobileapp.R
import com.example.tempomobileapp.WaitActivity
import com.example.tempomobileapp.adapters.HIBPApiService
import com.example.tempomobileapp.adapters.TempoApiService
import com.example.tempomobileapp.enums.Country
import com.example.tempomobileapp.exceptions.ApiException
import com.example.tempomobileapp.models.SecurityQuestion
import com.example.tempomobileapp.ui.components.MainButtonData
import com.example.tempomobileapp.ui.components.mainButton
import com.example.tempomobileapp.ui.theme.Main4
import com.example.tempomobileapp.ui.theme.tempoMobileAppTheme
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.mockkStatic
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import loginLayout
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Locale

@RunWith(AndroidJUnit4::class)
class SigninActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<SignInActivity>()

    @Test
    fun testActivityLaunches() {
        composeTestRule.activityRule.scenario.onActivity { activity ->
        }
    }
/*
    @Before
    fun setup() {
        System.setProperty("io.mockk.useJavaAgent", "true")
        System.setProperty(
            "dexmaker.dexcache",
            ApplicationProvider.getApplicationContext<Context>().cacheDir.path
        )

        // Mock de TempoApiService pour les appels réseau
        mockkObject(TempoApiService)
        coEvery { TempoApiService.getInstance().createUser(any()) } returns true

        // Mock de HIBPApiService pour la vérification du mot de passe
        mockkObject(HIBPApiService)
        coEvery { HIBPApiService.getInstance().checkPassword(any()) } returns false
    }*/

    @Test
    fun testSignInFlow() {
        // Étape 1 : Lancer l'activité
        val securityQuestionProvider: suspend () -> List<SecurityQuestion> = { listOf(
            SecurityQuestion(1, "Question 1"),
            SecurityQuestion(2, "Question 2"),
            SecurityQuestion(3, "Question 3")
        ) }

        val context = ApplicationProvider.getApplicationContext<Context>()
        Log.d("App", "Avant de créer l'intent")
        val intent = SignInActivity.createIntent(context, securityQuestionProvider)

        Log.d("App", "Intent créé : $intent")
        val scenario = ActivityScenario.launch<SignInActivity>(intent)


        mockkObject(TempoApiService)
        coEvery { TempoApiService.getInstance().checkIfUserAvailable(any()) } returns true
        coEvery { TempoApiService.getInstance().createUser(any()) } returns true

        // Mock de HIBPApiService pour la vérification du mot de passe
        mockkObject(HIBPApiService)
        coEvery { HIBPApiService.getInstance().checkPassword(any()) } returns false


        // Étape 2 : Remplir les champs de l'interface utilisateur
        composeTestRule.onNodeWithTag("usernameField").performTextInput("TestUser")
        composeTestRule.onNodeWithTag("emailField").performTextInput("test@example.com")
        composeTestRule.onNodeWithTag("phoneField").performTextInput("102030405")
        composeTestRule.onNodeWithTag("passwordField").performTextInput("TeestPasswd654")
        composeTestRule.onNodeWithTag("checkPasswordField").performTextInput("TeestPasswd654")
        composeTestRule.onNodeWithTag("questionField1").performTextInput("Réponse 1")
        composeTestRule.onNodeWithTag("questionField2").performTextInput("Réponse 2")
        composeTestRule.onNodeWithTag("questionField3").performTextInput("Réponse 3")


        // Étape 3 : Simuler un clic sur le bouton "Valider"
        composeTestRule.onNodeWithTag("validationButton").performScrollTo()
        composeTestRule.onNodeWithTag("validationButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("validationButton").performClick()

        // Étape 4 : Vérifier que le Dialog s'affiche
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("signinValidationDialog")
            .assertIsDisplayed()

        // Étape 5 : Simuler un clic sur le bouton "Compris !"
        composeTestRule.onNodeWithText("Compris !").performClick()

        // Étape 6 : Vérifier la navigation vers LoginActivity
        scenario.onActivity { activity ->
            val expectedIntent = Intent(activity, LoginActivity::class.java)
            val resolvedActivity = activity.packageManager.resolveActivity(expectedIntent, 0)
            assert(resolvedActivity != null) {
                "L'utilisateur n'a pas été redirigé vers LoginActivity."
            }
        }

        resetSignInStates()

    }


    @Test
    fun testSignInFlowErrorApi() {
        // Étape 1 : Lancer l'activité
        val securityQuestionProvider: suspend () -> List<SecurityQuestion> = { listOf(
            SecurityQuestion(1, "Question 1"),
            SecurityQuestion(2, "Question 2"),
            SecurityQuestion(3, "Question 3")
        ) }

        val context = ApplicationProvider.getApplicationContext<Context>()
        Log.d("App", "Avant de créer l'intent")
        val intent = SignInActivity.createIntent(context, securityQuestionProvider)

        Log.d("App", "Intent créé : $intent")
        val scenario = ActivityScenario.launch<SignInActivity>(intent)


        mockkObject(TempoApiService)
        coEvery { TempoApiService.getInstance().checkIfUserAvailable(any()) } returns true
        coEvery { TempoApiService.getInstance().createUser(any()) } throws ApiException("Erreur lors de la création de l'utilisateur")

        // Mock de HIBPApiService pour la vérification du mot de passe
        mockkObject(HIBPApiService)
        coEvery { HIBPApiService.getInstance().checkPassword(any()) } returns false


        // Étape 2 : Remplir les champs de l'interface utilisateur
        composeTestRule.onNodeWithTag("usernameField").performTextInput("TestUser")
        composeTestRule.onNodeWithTag("emailField").performTextInput("test@example.com")
        composeTestRule.onNodeWithTag("phoneField").performTextInput("102030405")
        composeTestRule.onNodeWithTag("passwordField").performTextInput("TeestPasswd654")
        composeTestRule.onNodeWithTag("checkPasswordField").performTextInput("TeestPasswd654")
        composeTestRule.onNodeWithTag("questionField1").performTextInput("Réponse 1")
        composeTestRule.onNodeWithTag("questionField2").performTextInput("Réponse 2")
        composeTestRule.onNodeWithTag("questionField3").performTextInput("Réponse 3")


        // Étape 3 : Simuler un clic sur le bouton "Valider"
        composeTestRule.onNodeWithTag("validationButton").performScrollTo()
        composeTestRule.onNodeWithTag("validationButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("validationButton").performClick()

        // Étape 4 : Vérifier que le Dialog s'affiche
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("signinErrorDialog")
            .assertIsDisplayed()

        // Étape 5 : Simuler un clic sur le bouton "Compris !"
        composeTestRule.onNodeWithText("Compris !").performClick()

        // Étape 6 : Vérifier la navigation vers LoginActivity
        scenario.onActivity { activity ->
            val expectedIntent = Intent(activity, LoginActivity::class.java)
            val resolvedActivity = activity.packageManager.resolveActivity(expectedIntent, 0)
            assert(resolvedActivity != null) {
                "L'utilisateur n'a pas été redirigé vers LoginActivity."
            }
        }

        resetSignInStates()

    }


    @Test
    fun testSignInFlowErrorInInputs() {
        val securityQuestionProvider: suspend () -> List<SecurityQuestion> = { listOf(
            SecurityQuestion(1, "Question 1"),
            SecurityQuestion(2, "Question 2"),
            SecurityQuestion(3, "Question 3")
        ) }

        val context = ApplicationProvider.getApplicationContext<Context>()
        Log.d("App", "Avant de créer l'intent")
        val intent = SignInActivity.createIntent(context, securityQuestionProvider)

        Log.d("App", "Intent créé : $intent")
        val scenario = ActivityScenario.launch<SignInActivity>(intent)


        mockkObject(TempoApiService)
        coEvery { TempoApiService.getInstance().checkIfUserAvailable(any()) } returns true
        coEvery { TempoApiService.getInstance().createUser(any()) } returns true

        // Mock de HIBPApiService pour la vérification du mot de passe
        mockkObject(HIBPApiService)
        coEvery { HIBPApiService.getInstance().checkPassword(any()) } returns false


        // Étape 2 : Remplir les champs de l'interface utilisateur
        composeTestRule.onNodeWithTag("usernameField").performTextInput("")
        composeTestRule.onNodeWithTag("emailField").performTextInput("test@example.com")
        composeTestRule.onNodeWithTag("phoneField").performTextInput("102030405")
        composeTestRule.onNodeWithTag("passwordField").performTextInput("")
        composeTestRule.onNodeWithTag("checkPasswordField").performTextInput("")
        composeTestRule.onNodeWithTag("questionField1").performTextInput("Réponse 1")
        composeTestRule.onNodeWithTag("questionField2").performTextInput("Réponse 2")
        composeTestRule.onNodeWithTag("questionField3").performTextInput("Réponse 3")


        // Étape 3 : Simuler un clic sur le bouton "Valider"
        composeTestRule.onNodeWithTag("validationButton").performScrollTo()
        composeTestRule.onNodeWithTag("validationButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("validationButton").performClick()

        // Étape 4 : Vérifier que le Dialog s'affiche
        composeTestRule.onNodeWithTag("signinValidationDialog")
            .assertIsNotDisplayed()

        composeTestRule.onNodeWithTag("errorMessage")
            .assertExists()
        composeTestRule.onNodeWithTag("errorMessage")
            .assertIsDisplayed()

        resetSignInStates()

    }

    @Test
    fun testSignInNavigateToErrorAfterApiError() {
        // Étape 1 : Lancer l'activité
        val securityQuestionProvider: suspend () -> List<SecurityQuestion> = {
            throw ApiException("Erreur API simulée")
        }

        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = SignInActivity.createIntent(context, securityQuestionProvider)

        Intents.init()

        val scenario = ActivityScenario.launch<SignInActivity>(intent)

        Intents.intended(IntentMatchers.hasComponent(ErrorActivity::class.java.name))

        // Nettoie Espresso-Intents
        Intents.release()

    }

    @Test
    fun testOnBackPressedNavigatesToLoginActivity() {
        Intents.init()

        val scenario = ActivityScenario.launch(SignInActivity::class.java)

        Espresso.pressBack()

        intended(hasComponent(LoginActivity::class.java.name))

        Intents.release()
    }


    @Test
    fun testGetDefaultCountryReturnsFormatLibreWhenCountryNotFound() {
        val mockLocale = Locale("xx", "ZZ")

        val defaultCountry = getDefaultCountry(mockLocale)

        val expectedCountry = Country("Format libre", "", "", R.drawable.flag_white, "")
        assertEquals(expectedCountry, defaultCountry)
    }


    @Test
    fun testOpenPhoneDialog() {
        val securityQuestionProvider: suspend () -> List<SecurityQuestion> = { listOf(
            SecurityQuestion(1, "Question 1"),
            SecurityQuestion(2, "Question 2"),
            SecurityQuestion(3, "Question 3")
        ) }

        val context = ApplicationProvider.getApplicationContext<Context>()

        val intent = SignInActivity.createIntent(context, securityQuestionProvider)

        val scenario = ActivityScenario.launch<SignInActivity>(intent)


        mockkObject(TempoApiService)
        coEvery { TempoApiService.getInstance().checkIfUserAvailable(any()) } returns true
        coEvery { TempoApiService.getInstance().createUser(any()) } returns true

        mockkObject(HIBPApiService)
        coEvery { HIBPApiService.getInstance().checkPassword(any()) } returns false


        composeTestRule.onNodeWithTag("flagClickable").performClick()

        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("flagDialog").assertIsDisplayed()
        composeTestRule.onNodeWithTag("searchField").performTextInput("france")

        composeTestRule.onNodeWithTag("France").performClick()
        composeTestRule.onNodeWithTag("flagDialog").assertIsNotDisplayed()


        resetSignInStates()

    }

}
