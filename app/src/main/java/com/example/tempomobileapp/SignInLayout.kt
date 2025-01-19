import android.app.Activity
import android.graphics.Bitmap
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.tempomobileapp.ErrorActivity
import com.example.tempomobileapp.R
import com.example.tempomobileapp.adapters.HIPBApiService
import com.example.tempomobileapp.adapters.TempoApiService
import com.example.tempomobileapp.enums.Country
import com.example.tempomobileapp.enums.countries
import com.example.tempomobileapp.models.SecurityQuestion
import com.example.tempomobileapp.ui.components.InputFieldData
import com.example.tempomobileapp.ui.components.MainButtonData
import com.example.tempomobileapp.ui.components.decoration
import com.example.tempomobileapp.ui.components.inputField
import com.example.tempomobileapp.ui.components.mainButton
import com.example.tempomobileapp.ui.theme.Main1
import com.example.tempomobileapp.ui.theme.Main2
import com.example.tempomobileapp.ui.theme.Main3
import com.example.tempomobileapp.ui.theme.Main4
import com.example.tempomobileapp.ui.theme.Main5
import com.example.tempomobileapp.ui.theme.background
import com.example.tempomobileapp.ui.theme.background_dark
import com.example.tempomobileapp.ui.theme.text
import com.example.tempomobileapp.utils.getLightenColor
import com.gandiva.neumorphic.LightSource
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Pressed
import com.gandiva.neumorphic.shape.RoundedCorner
import com.spr.jetpack_loading.components.indicators.BallPulseSyncIndicator
import com.spr.jetpack_loading.components.indicators.PacmanIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import android.provider.Settings
import android.content.Context


private data class SignInData(
    val username: String,
    val onUsernameChange: (String) -> Unit,
    val email: String,
    val onEmailChange: (String) -> Unit,
    val phoneNumber: String,
    val password: String,
    val onPasswordChange: (String) -> Unit,
    val passwordCheck: String,
    val onPasswordCheckChange: (String) -> Unit,

)
var debounceJob: Job? = null
val selectedCountry = mutableStateOf(getDefaultCountry())  // Utilise mutableStateOf sans `remember`
var isLoading = mutableStateOf(false)


@Composable
fun SignInLayout(
    securityQuestions: List<SecurityQuestion> = emptyList(),
    navController: NavHostController
) {
    val context = LocalContext.current


    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordCheck by remember { mutableStateOf("") }




    var usernameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var passwordCheckError by remember { mutableStateOf<String?>(null) }
    var userError by remember { mutableStateOf<Boolean?>(null) }
    var validUsername by remember { mutableStateOf<Boolean?>(null) }
    var openDialog by remember { mutableStateOf(false) }






    Log.d("App", "Questions affichées dans le composable : $securityQuestions")

    val signInData = SignInData(
        username = username,
        onUsernameChange = { username = it },
        email = email,
        onEmailChange = { email = it },
        phoneNumber = phoneNumber,
        password = password,
        onPasswordChange = { password = it },
        passwordCheck = passwordCheck,
        onPasswordCheckChange = { passwordCheck = it }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        topDecoration()

        if(securityQuestions.isNotEmpty()) {
            val securityAnswers = remember {
                securityQuestions.map { mutableStateOf("") }.toMutableList() // Liste de mutableStateOf
            }
            val securityErrors = remember {
                securityQuestions.map { mutableStateOf<String?>(null) }.toMutableList() // Liste de mutableStateOf
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 90.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 32.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    Text(
                        text = "Inscription",
                        fontSize = 32.sp,
                        color = text,
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .testTag("errorText")
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "Mon identité",
                        textAlign = TextAlign.Left,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 24.sp,
                        color = text
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Prends un moment pour remplir ces informations. Elles resteront bien protégées et nous en avons besoin pour créer ton compte en toute sécurité. 😊",
                        textAlign = TextAlign.Left,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 12.sp,
                        color = text,
                        lineHeight = 14.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Mon pseudo",
                        textAlign = TextAlign.Left,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 18.sp,
                        color = text
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val coroutineScope = rememberCoroutineScope()

                    inputField(
                        InputFieldData(
                            value = signInData.username,
                            onValueChange = { newValue ->

                                val newValue = newValue.trimEnd()
                                signInData.onUsernameChange(newValue)

                                // Réinitialiser l'erreur si le champ n'est plus vide
                                if (newValue.isNotBlank()) {
                                    usernameError = null
                                    validUsername = true
                                }

                                // Annuler tout job de debounce en cours
                                debounceJob?.cancel()

                                // Lancer un nouveau job avec un délai d'attente
                                debounceJob = coroutineScope.launch {
                                    delay(1000L) // Attendre 1 seconde
                                    if (newValue.isNotBlank()) {
                                        val isAvailable = withContext(Dispatchers.IO) {
                                            TempoApiService.getInstance().checkIfUserAvailable(newValue)
                                        }
                                        if (!isAvailable) {
                                            usernameError = "Ce pseudo est déjà pris."
                                            validUsername = false
                                        }
                                    }
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            isPassword = false,
                            borderColor = if (usernameError != null) Main3 else Main1
                        )
                    )
                    if (usernameError != null) {
                    Text(
                        text = usernameError ?: "",
                        color = Main3,
                        fontSize = 12.sp,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth(),
                        lineHeight = 14.sp
                    )
                }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Mon email",
                        textAlign = TextAlign.Left,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 18.sp,
                        color = text
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    inputField(
                        InputFieldData(
                            value = signInData.email,
                            onValueChange = { newValue ->
                                signInData.onEmailChange(newValue)
                                emailError = null
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            isPassword = false,
                            maxLength = 250,
                            borderColor = if (emailError != null) Main3 else Main1
                        )
                    )
                    if (emailError != null) {
                        Text(
                            text = emailError ?: "",
                            color = Main3,
                            fontSize = 12.sp,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth(),
                            lineHeight = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Mon téléphone",
                        textAlign = TextAlign.Left,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 18.sp,
                        color = text
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    inputPhone(
                        phoneData = InputPhoneData(
                            onValueChange = { newPhone ->
                                phoneNumber = newPhone
                                phoneError = null
                            },
                            phoneNumber = signInData.phoneNumber,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            selectedCountry = selectedCountry,  // Passe l'état mutable à inputPhone
                            borderColor = if (phoneError != null) Main3 else Main1
                        )
                    )
                    //phone
                    if (phoneError != null) {
                        Text(
                            text = phoneError ?: "",
                            color = Main3,
                            fontSize = 12.sp,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth(),
                            lineHeight = 14.sp
                        )
                    }


                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Mon mot de passe",
                        textAlign = TextAlign.Left,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 18.sp,
                        color = text
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    inputField(
                        InputFieldData(
                            value = signInData.password,
                            onValueChange = { newValue ->
                                coroutineScope.launch {
                                    signInData.onPasswordChange(newValue)
                                    val valid_password = checkPassword(
                                        newValue,
                                        signInData.username,
                                        signInData.email
                                    ){ error ->
                                        Log.d("App", "error : $error")
                                        passwordError = error
                                    }
                                    Log.d("App", "Password error: $passwordError")
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            isPassword = true,
                            maxLength = 30,
                            borderColor = if (passwordError != null) Main3 else Main1
                        )
                    )
                    if (passwordError != null) {
                        Text(
                            text = passwordError ?: "",
                            color = Main3,
                            fontSize = 12.sp,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth(),
                            lineHeight = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Confirmer mon mot de passe",
                        textAlign = TextAlign.Left,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 18.sp,
                        color = text
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    inputField(
                        InputFieldData(
                            value = signInData.passwordCheck,
                            onValueChange = { newValue ->
                                signInData.onPasswordCheckChange(newValue)
                                //phoneNumber = newPhone
                                passwordCheckError = null
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            isPassword = true,
                            maxLength = 30,
                            borderColor =  if(passwordCheckError != null) Main3 else Main1
                        )
                    )
                    if (passwordCheckError != null) {
                        Text(
                            text = passwordCheckError ?: "",
                            color = Main3,
                            fontSize = 12.sp,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth(),
                            lineHeight = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(64.dp))
                    Text(
                        text = "Ma sécurité",
                        textAlign = TextAlign.Left,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 24.sp,
                        color = text
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Réponds à ces questions en un mot et notes tes réponses quelque part, nous en auront besoin dans le cas où tu perdrait tes identifiants 😉",
                        textAlign = TextAlign.Left,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 12.sp,
                        color = text,
                        lineHeight = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    securityQuestions.forEachIndexed { index, question ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = question.question,
                            textAlign = TextAlign.Left,
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 18.sp,
                            color = text
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        // Utilisation de mutableStateOf pour chaque réponse
                        inputField(
                            InputFieldData(
                                value = securityAnswers[index].value,  // Utiliser le state de réponse pour chaque question
                                onValueChange = { newAnswer ->
                                    securityAnswers[index].value = newAnswer
                                    securityErrors[index].value = null
                                                },  // Mettre à jour la réponse
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                borderColor =  if(securityErrors[index].value != null) Main3 else Main1
                            )

                        )
                        if (securityErrors[index].value != null) {
                            Text(
                                text = securityErrors[index].value ?: "",
                                color = Main3,
                                fontSize = 12.sp,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth(),
                                lineHeight = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    if (userError == true) {
                        Text(
                            text = "Attention, un ou plusieurs champs n'est pas rempli correctement, essaye de repasser sur les différentes question pour voir si tu n'aurais pas oublié quelque chose 😥",
                            color = Main3,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            lineHeight = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 64.dp)
                    ) {

                        mainButton(
                            MainButtonData(
                                onClick = {

                                    Log.d("App", "is loading ? ${isLoading.value}")

                                    if (!isLoading.value) {

                                        isLoading.value = true

                                    Log.d("App", "Button clicked")

                                    CoroutineScope(Dispatchers.Main).launch {
                                        var validUser = true

                                        // Vérification des champs avec gestion des erreurs
                                        val isUsernameValidResult = isUsernameValid(username) { error ->
                                            usernameError = error
                                        }

                                        if (!isUsernameValidResult) {
                                            validUser = false
                                        }

                                        // Email:

                                        // Vérification des champs avec gestion des erreurs
                                        val isEmailValidResult = isEmailValid(email) { error ->
                                            emailError = error
                                        }

                                        if (!isEmailValidResult) {
                                            validUser = false
                                        }

                                        // Email:

                                        Log.d("App", selectedCountry.value.name)

                                        // Vérification des champs avec gestion des erreurs
                                        val isPhoneValidResult = isPhoneNumberValid(phoneNumber, getDefaultCountry()) { error ->
                                            phoneError = error
                                        }

                                        if (!isPhoneValidResult) {
                                            validUser = false
                                        }

                                        // password
                                        val isPasswordValidResult = checkPassword(password, username, email) { error ->
                                            passwordError = error
                                        }

                                        if (!isPasswordValidResult) {
                                            validUser = false
                                        }

                                        //check if password match
                                        Log.d("App", "password : $password")
                                        Log.d("App", "passwordcheck : $passwordCheck")
                                        if (passwordCheck != password) {
                                            passwordCheckError = "Les mots de passe ne correspondent pas."
                                            validUser = false
                                        }

                                        securityQuestions.forEachIndexed { index, question ->
                                            if (securityAnswers[index].value.isBlank()){
                                                securityErrors[index].value = "Veuillez répondre à la question."
                                                validUser = false
                                            }
                                        }


                                        // Actions basées sur la validation
                                        if (validUser) {
                                            userError = false
                                            Log.d("App", "Valid user")
                                            Log.d("App", "Button clicked")
                                            Log.d("App", "Username: $username")
                                            Log.d("App", "Email: $email")
                                            Log.d("App", "Phone: $phoneNumber")
                                            Log.d("App", "Password: $password")
                                            Log.d("App", "Password Check: $passwordCheck")
                                            Log.d(
                                                "App",
                                                "Security Answers: ${securityAnswers.joinToString(", ") { it.value }}"
                                            )
                                            openDialog = true

                                            securityQuestions.forEachIndexed { index, securityQuestion ->
                                                securityQuestion.response = securityAnswers[index].value
                                            }

                                            // Récupération du context pour l'ID unique de l'appareil

                                            val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
                                            Log.d("App", "Device ID: $deviceId")

                                            // POST to create the user

                                            TempoApiService.getInstance().createUser(username, password, email, phoneNumber, securityQuestions, deviceId)



                                        } else {
                                            Log.d("App", "Invalid user")
                                            userError = true
                                        }
                                        isLoading.value = false
                                    }

                                    }

                                },
                                text = "Valider",
                                color = Main4,
                                modifier = Modifier
                            ),
                            isLoading = isLoading.value
                        )
                    }
                    Spacer(modifier = Modifier.height(64.dp))
                }
            }

            if (openDialog){

                Dialog(
                    onDismissRequest = {
                        openDialog = false
                        Log.d("App", "Dialog closed  vbcvghv")
                        navController.navigate("login")
                                       },
                    properties = DialogProperties(
                        dismissOnBackPress = true,
                        dismissOnClickOutside = true,
                        usePlatformDefaultWidth = false
                    )
                ) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {


                        Box(
                            Modifier
                                .align(Alignment.Center)
                                .neu(
                                    lightShadowColor = getLightenColor(background),
                                    darkShadowColor = background_dark,
                                    shadowElevation = 4.dp,
                                    lightSource = LightSource.LEFT_TOP,
                                    shape = Pressed(RoundedCorner(16.dp))
                                )
                                .background(background, shape = RoundedCornerShape(16.dp))
                                .padding(16.dp)
                        ) {
                            Column {

                                val vectorDrawable = ContextCompat.getDrawable(LocalContext.current, R.drawable.fond_etoiles)
                                val originalBitmap = Bitmap.createBitmap(
                                    vectorDrawable!!.intrinsicWidth,
                                    vectorDrawable.intrinsicHeight,
                                    Bitmap.Config.ARGB_8888
                                )

                                val canvas = android.graphics.Canvas(originalBitmap)
                                vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
                                vectorDrawable.draw(canvas)

                                // Redimensionnement du bitmap
                                val scaleFactor = 2.05f // Ajustez ce facteur pour agrandir ou réduire le motif
                                val scaledBitmap = Bitmap.createScaledBitmap(
                                    originalBitmap,
                                    (originalBitmap.width * scaleFactor).toInt(),
                                    (originalBitmap.height * scaleFactor).toInt(),
                                    true
                                )

                                val pattern = scaledBitmap.asImageBitmap()

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp) // Ajustez la hauteur selon vos besoins
                                        .background(Color.Transparent)
                                ) {
                                    Canvas(
                                        modifier = Modifier
                                            .fillMaxSize()
                                    ) {
                                        // Configuration du Paint
                                        val paint = Paint().asFrameworkPaint().apply {
                                            isAntiAlias = true

                                            // Utilisation du shader avec le bitmap redimensionné
                                            shader = ImageShader(
                                                pattern,
                                                TileMode.Repeated,
                                                TileMode.Repeated
                                            )

                                            alpha = (0.3 * 255).toInt() // Transparence : 0.0 (invisible) à 1.0 (opaque)
                                        }

                                        // Limitez le dessin au rectangle correspondant à la Box
                                        val boxHeightPx = size.height
                                        val boxWidthPx = size.width

                                        drawIntoCanvas {
                                            it.nativeCanvas.save()
                                            it.nativeCanvas.clipRect(0f, 0f, boxWidthPx, boxHeightPx)
                                            it.nativeCanvas.drawPaint(paint)
                                            it.nativeCanvas.restore()
                                        }

                                        paint.reset()
                                    }


                                    // Contenu texte centré
                                    /*Text(
                                        text = "Inscription prise en compte !",
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.align(Alignment.Center),
                                        fontSize = 32.sp,
                                        color = text,
                                        lineHeight = 32.sp
                                    )*/

                                    Canvas(
                                        modifier = Modifier
                                            .fillMaxSize()
                                    ) {
                                        val paint = Paint().asFrameworkPaint().apply {
                                            isAntiAlias = true
                                            textSize = 80f // Taille du texte en pixels
                                            color = background.toArgb() // Couleur du contour
                                            style = android.graphics.Paint.Style.STROKE // Style contour
                                            strokeWidth = 8f // Épaisseur de la bordure
                                        }

                                        val textPaint = Paint().asFrameworkPaint().apply {
                                            isAntiAlias = true
                                            textSize = 80f // Même taille que le contour
                                            color = text.toArgb() // Couleur du texte
                                        }

                                        // Découpage du texte en plusieurs lignes
                                        val lines = arrayOf("Inscription prise","en compte !") // Découpe par espace
                                        val lineSpacing = 10f // Espacement entre les lignes
                                        val lineHeight = paint.textSize + lineSpacing // Hauteur totale par ligne
                                        val textStartX = size.width / 2
                                        val textStartY = size.height / 2 - (lines.size - 1) * lineHeight / 2 // Centrer les lignes

                                        lines.forEachIndexed { index, line ->
                                            val lineX = textStartX - paint.measureText(line) / 2 // Centrer chaque ligne
                                            val lineY = textStartY + index * lineHeight

                                            // Dessiner la bordure du texte
                                            drawIntoCanvas {
                                                it.nativeCanvas.drawText(line, lineX, lineY, paint)
                                            }

                                            // Dessiner le texte principal
                                            drawIntoCanvas {
                                                it.nativeCanvas.drawText(line, lineX, lineY, textPaint)
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))
                                }

                                Spacer(modifier = Modifier.height(12.dp))



                                Image(
                                    painter = painterResource(id = R.drawable.etoile),
                                    contentDescription = "App Icon",
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .size(80.dp)
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "Merci d'avoir pri le temps de rempir le formulaire d'inscription 😁 \n\nPour compléter ton inscription, nous avons besoin de vérifier test informations 🧐.\n\nNous t'avons envoyé une mail pour la suite de la procédure, une fois toutes les étapes complétées, tu pourra profiter pleinement de l'application !😎 Attention, le lien n'est valide que pendant 5 minutes",
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier.fillMaxWidth(),
                                    fontSize = 12.sp,
                                    color = text,
                                    lineHeight = 14.sp
                                )

                                Spacer(modifier = Modifier.height(32.dp))

                                mainButton(
                                    MainButtonData(
                                        onClick = {
                                            Log.d("App", "Button clicked")
                                            navController.navigate("login")
                                        },
                                        text = "Compris !",
                                        color = Main5,
                                        modifier = Modifier
                                    )

                                )

                                Spacer(modifier = Modifier.height(16.dp))


                            }
                        }
                    }
                }
            }
        }
        else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 90.dp),
                contentAlignment = Alignment.Center
            ) {
                BallPulseSyncIndicator(color = Main1, spaceBetweenBalls = 40f)
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

fun getDefaultCountry(): Country {

    val defaultCountryCode = Locale.getDefault().country
    return countries.firstOrNull { it.code == defaultCountryCode }
        ?: Country("Format libre", "", "", R.drawable.flag_white, "")
}

suspend fun isUsernameValid(
    username: String,
    onError: (String?) -> Unit
): Boolean {
    if (username.isBlank()) {
        onError("Le pseudo est obligatoire.")
        return false
    }

    val isAvailable = withContext(Dispatchers.IO) {
        TempoApiService.getInstance().checkIfUserAvailable(username)
    }

    if (!isAvailable) {
        onError("Ce pseudo est déjà pris.")
        return false
    }

    onError(null)
    return true
}

fun isEmailValid(
    email: String,
    onError: (String?) -> Unit
): Boolean {
    if (email.isBlank()) {
        onError("L'email est obligatoire.")
        return false
    }

    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
    if (!emailRegex.matches(email)) {
        onError("L'email n'est pas valide.")
        return false
    }

    onError(null)
    return true
}


fun isPhoneNumberValid(
    phoneNumber: String,
    selectedCountry: Country, // Objet contenant le format du numéro
    onError: (String?) -> Unit
): Boolean {
    if (phoneNumber.isBlank()) {
        onError("Le numéro de téléphone est obligatoire.")
        return false
    }

    // Conversion du format de téléphone en regex
    val phoneRegex = selectedCountry.phoneFormat
        .replace("#", "\\d") // Remplace les # par \\d (chiffre)
        .replace("+", "\\+") // Remplace les # par \\d (chiffre)
        .toRegex()

    // Validation du numéro
    if (!phoneRegex.matches(phoneNumber)) {
        onError("Le numéro de téléphone n'est pas valide ...")
        return false
    }

    onError(null)
    return true
}


suspend fun checkPassword(inputPassword: String, username: String, email: String, onError: (String?) -> Unit): Boolean {
    // Vérifie la longueur minimale
    if (inputPassword.length < 10) {
        onError("Le mot de passe ne doit au moiuns contenir 10 caractères")
        return false
    }

    // Vérifie qu'il n'y a pas plus de 3 caractères identiques à la suite
    val regexIdentiques = Regex("(.)\\1{2,}")
    if (regexIdentiques.containsMatchIn(inputPassword)) {
        onError("Le mot de passe ne doit pas contenir plus de 3 caractères identiques consécutifs")
        return false
    }

    // Génération dynamique des séries de caractères interdits
    val forbiddenSeries = generateSeries()
    for (series in forbiddenSeries) {
        if (inputPassword.contains(series)) {
            onError("Le mot de passe ne doit pas contenir de séries consécutives comme '123', 'abc', etc.")
            return false
        }
    }

    // Vérifie la présence d'au moins une majuscule
    if (!inputPassword.any { it.isUpperCase() }) {
        onError("Le mot de passe doit contenir au moins une lettre majuscule")
        return false
    }

    // Vérifie la présence d'au moins une minuscule
    if (!inputPassword.any { it.isLowerCase() }) {
        onError("Le mot de passe doit contenir au moins une lettre minuscule")
        return false
    }

    // Vérifie la présence d'au moins un chiffre
    if (!inputPassword.any { it.isDigit() }) {
        onError("Le mot de passe doit contenir au moins un chiffre")
        return false
    }

    // Extraire les chaînes significatives du username et de l'email
    val personalInfo = extractPersonalInfo(username, email)
    for (info in personalInfo) {
        if (info.length >= 4 && inputPassword.contains(info, ignoreCase = true)) {
            onError("Le mot de passe ne doit pas contenir d'informations personnelles comme le pseudo ou l'email")
            return false
        }
    }

    // HIPB verification
    val isPasswordValid = withContext(Dispatchers.IO) {
        HIPBApiService.getInstance().checkPassword(inputPassword)
    }
    if (isPasswordValid) {
        onError("Le mot de passe est trop faible")
        return false
    }


    // Si toutes les vérifications passent
    onError(null)
    return true
}

fun generateSeries(): List<String> {
    val series = mutableListOf<String>()

    // Séries de chiffres (012, 123, ..., 789)
    for (i in 0..8) {
        series.add((i..i + 2).joinToString("") { it.toString() })
    }

    // Séries de lettres (abc, bcd, ..., xyz)
    for (i in 'a'..'y') {
        series.add((i..i + 2).joinToString("") { it.toString() })
    }

    // Ajoute les séries en majuscules (ABC, BCD, ...)
    for (i in 'A'..'Y') {
        series.add((i..i + 2).joinToString("") { it.toString() })
    }


    return series
}

fun extractPersonalInfo(username: String, email: String): List<String> {
    val info = mutableSetOf<String>() // Utilisation d'un Set pour éviter les doublons

    // Fonction pour générer toutes les sous-chaînes d'une longueur minimale
    fun generateSubstrings(input: String) {
        val length = input.length
        for (i in 0 until length) {
            for (j in i + 4..length) { // Sous-chaînes de longueur >= 4
                info.add(input.substring(i, j))
            }
        }
    }

    // Ajouter les sous-chaînes du username
    generateSubstrings(username)

    // Extraire les parties de l'email
    val emailParts = email.split("@")
    if (emailParts.size == 2) {
        val beforeAt = emailParts[0] // Avant le @
        val afterAt = emailParts[1].substringBefore(".") // Entre @ et .

        // Ajouter les sous-chaînes des parties de l'email
        generateSubstrings(beforeAt)
        generateSubstrings(afterAt)
    }

    Log.d("App", "Extracted info: $info")
    return info.toList()
}


