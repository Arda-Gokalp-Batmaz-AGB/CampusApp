package com.arda.campuslink.ui.auth

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.arda.campuslink.ui.theme.CampusLinkTheme
import com.arda.campuslink.ui.theme.ThemeController
import com.arda.campuslink.util.LangStringUtil
import com.arda.campuslink.R
import com.arda.campuslink.ui.navigation.NavigationScreen
import com.arda.campuslink.util.DebugTags
import com.arda.campuslink.util.GoogleOneTapClient
import com.arda.mainapp.auth.Resource

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Actions(model: AuthViewModel,navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                }
            )

        }
    )
    {
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
                .fillMaxHeight(1f),
            contentAlignment = Alignment.Center
        )
        {
            Column(
                modifier = Modifier
                    .matchParentSize()
                    .padding(top = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                imageLogo()
                AuthLogic(model,navController)
            }
        }
    }
}

@Composable
fun AuthLogic(model: AuthViewModel,navController: NavController) {
    val state by model.uiState.collectAsState()
    if (state.currentAuthScreenState.equals("Login")) {
        LoginWithEmailPage(model,navController)
    } else if (state.currentAuthScreenState.equals("Register")) {
        RegisterWithEmailPage(model,navController)
    }
}

@Composable
fun authMainLayout(model: AuthViewModel = hiltViewModel<AuthViewModel>(),navController: NavController) {
    CampusLinkTheme(ThemeController.themeIsDark) {
        model.changeAuthScreenState("Login")
        Actions(model,navController)
    }
}

@Composable
fun imageLogo() {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(200.dp)
            .clip(CircleShape),
        shape = RoundedCornerShape(15.dp),
        elevation = 5.dp,
    )
    {
        Image(
            modifier = Modifier
                .fillMaxSize(),
            painter = painterResource(R.drawable.logo),
            contentScale = ContentScale.Crop,
            contentDescription = "Logo"
        )
    }

}

@Composable
fun emailText(model: AuthViewModel) {
    Column(modifier = Modifier.wrapContentSize()) {
        val state by model.uiState.collectAsState()
        var color by remember { mutableStateOf(Color.Gray) }
        var errorType = state.emailError
        if (errorType) {
            color = MaterialTheme.colors.error
        } else {
            color = MaterialTheme.colors.primary
        }
        var focused by remember { mutableStateOf(false) }
        TextField(
            modifier = Modifier
                .width(300.dp)
                .border(1.dp, color, shape = RoundedCornerShape(15.dp))
                .background(
                    MaterialTheme.colors.secondaryVariant,
                    shape = RoundedCornerShape(15.dp)
                )
                .onFocusChanged {
                    focused = it.isFocused == true
                },
            isError = errorType,
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                errorCursorColor = Color.Black,
                errorLeadingIconColor = Color.Red
            ),
            shape = RoundedCornerShape(15.dp),
            placeholder = { if (focused == false) Text(LangStringUtil.getLangString(R.string.email)) },
            label = { Text(LangStringUtil.getLangString(R.string.email), color = Color.Black) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIcon = {
                Icon(
                    Icons.Filled.Email,
                    contentDescription = LangStringUtil.getLangString(R.string.email),
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
            },

            value = state.enteredEmail, onValueChange = { newText ->
                run {
                    model.updateEnteredEmail(newText)
                }
            })
        errorText(LangStringUtil.getLangString(R.string.invalid_email), errorType)
    }
}

@Composable
fun passwordText(model: AuthViewModel) {
    Column(modifier = Modifier.wrapContentSize()) {
        val state by model.uiState.collectAsState()
        var color by remember { mutableStateOf(Color.Gray) }
        var errorType = state.passwordError
        if (errorType) {
            color = MaterialTheme.colors.error
        } else {
            color = MaterialTheme.colors.primary
        }
        var passwordVisible by rememberSaveable { mutableStateOf(false) }
        var focused by remember { mutableStateOf(false) }
        TextField(
            value = state.enteredPassword,
            onValueChange = { model.updateEnteredPassword(it) },
            label = { Text(LangStringUtil.getLangString(R.string.password), color = Color.Black) },
            modifier = Modifier
                .width(300.dp)
                .border(1.dp, color, shape = RoundedCornerShape(15.dp))
                .background(
                    MaterialTheme.colors.secondaryVariant,
                    shape = RoundedCornerShape(15.dp)
                )
                .onFocusChanged {
                    focused = it.isFocused == true
                },
            isError = errorType,
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                errorCursorColor = Color.Black,
                errorLeadingIconColor = Color.Red
            ),
            shape = RoundedCornerShape(15.dp),
            singleLine = true,
            placeholder = { if (focused == false) Text(LangStringUtil.getLangString(R.string.password)) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {

                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, description)
                }
            },
            leadingIcon = {
                Icon(
                    Icons.Filled.Lock,
                    contentDescription = "",
                    modifier = Modifier.size(ButtonDefaults.IconSize),
                )
            }

        )
        errorText(LangStringUtil.getLangString(R.string.invalid_password_length), errorType)
    }
}

@Composable
fun switchLoginType(newLoginType: String, model: AuthViewModel) {
    val state by model.uiState.collectAsState()
    Button(
        modifier = Modifier
            .width(300.dp),
        shape = RoundedCornerShape(15.dp),
        enabled = state.submitButtonOn,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primaryVariant
        ),
        onClick = {
            model.changeAuthScreenState(newLoginType)
            Log.d("LOG", state.currentAuthScreenState)
        })
    {
        if (state.currentAuthScreenState.contains("Register")) {
            if (newLoginType.equals("Login")) {
                Icon(
                    Icons.Filled.Email,
                    contentDescription = "",
                    modifier = Modifier.size(ButtonDefaults.IconSize),
                )
                Text(text = LangStringUtil.getLangString(R.string.login_w_email))
            }
        }
    }
}

@Composable
fun switchRegisterType(newRegisterType: String, model: AuthViewModel) {
    val state by model.uiState.collectAsState()

    Button(
        modifier = Modifier
            .width(300.dp),
        shape = RoundedCornerShape(15.dp),
        enabled = state.submitButtonOn,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primaryVariant
        ),
        onClick = {
            model.changeAuthScreenState(newRegisterType)
            Log.d("LOG", state.currentAuthScreenState)
        }) {
        Icon(
            Icons.Filled.Email,
            contentDescription = "",
            modifier = Modifier.size(ButtonDefaults.IconSize),
        )
        if (state.currentAuthScreenState.contains("Login")) {
            Text(text = LangStringUtil.getLangString(R.string.register_w_email))
        } else if (state.currentAuthScreenState.equals("Register")) {
            Text(text = LangStringUtil.getLangString(R.string.register_w_email))
        }
    }
}

@Composable
fun loginWithGoogle(model: AuthViewModel) {
    val state by model.uiState.collectAsState()

    val mContext = LocalContext.current
    val startForResult =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (intent != null) {
                    model.googleLogin(intent)
                }
            }
        }
    Button(
        onClick = {
            startForResult.launch(GoogleOneTapClient.getOneTapClient().signInIntent)
        },
        modifier = Modifier
            .width(300.dp),
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Black,
            contentColor = Color.White
        ),
        enabled = state.submitButtonOn
    ) {
        Image(
            modifier = Modifier
                .size(ButtonDefaults.IconSize)
                .align(Alignment.CenterVertically),
            painter = painterResource(id = R.drawable.google_logo),
            contentDescription = ""
        )
        Text(
            modifier = Modifier.weight(1f),
            text = LangStringUtil.getLangString(R.string.google_sign_in),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun RegisterWithEmailPage(model: AuthViewModel,navController: NavController) {
    emailText(model)
    passwordText(model)
    genericSubmitButton(LangStringUtil.getLangString(R.string.register), model, "Register",navController)
    loginWithGoogle(model)
    switchLoginType("Login", model)
}

@Composable
fun LoginWithEmailPage(model: AuthViewModel,navController: NavController) {
    emailText(model)
    passwordText(model)
    genericSubmitButton(LangStringUtil.getLangString(R.string.login), model, "Login",navController)
    loginWithGoogle(model)
    switchRegisterType("Register", model)
}

@Composable
fun genericSubmitButton(text: String, model: AuthViewModel, type: String,navController: NavController) {
    val state by model.uiState.collectAsState()

    val mContext = LocalContext.current
    val signUpFlow = state.signUpFlow
    Button(
        modifier = Modifier
            .width(300.dp),
        shape = RoundedCornerShape(15.dp),
        enabled = state.submitButtonOn,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primaryVariant
        ),
        onClick = {
            when (type) {
                "Login" -> {
                    model.login()
                }
                "Register" -> {
                    model.register()
                }
            }
        }) {
        Text(text = text)
        Icon(
            Icons.Filled.Login,
            contentDescription = "",
            modifier = Modifier.size(ButtonDefaults.IconSize),
        )
    }
    Spacer(modifier = Modifier.height(20.dp))
    state.loginFlow?.toString()?.let { Log.v(DebugTags.UITag.tag, "Login Flow: " + it) }
    if (type.equals("Login")) {
        state.loginFlow?.let {
            when (it) {
                is Resource.Failure<*> -> {
                    LaunchedEffect(Unit)
                    {
                        if (!state.submitButtonOn) {
                            Toast.makeText(
                                mContext,
                                LangStringUtil.getLangString(R.string.email_password_wrong),
                                Toast.LENGTH_LONG
                            )
                                .show()
                            state.submitButtonOn = true
                        }
                    }

                }
                Resource.Loading -> {
                    state.submitButtonOn = false
                    CircularProgressIndicator()
                }
                is Resource.Sucess -> {
                    LaunchedEffect(Unit)
                    {
                        state.submitButtonOn = true
                        Toast.makeText(
                            mContext,
                            LangStringUtil.getLangString(R.string.login_success),
                            Toast.LENGTH_LONG
                        ).show()
                        Log.v(DebugTags.UITag.tag,"LOGIN SUCESS! ROUTE TO HOME")
                        model.clearState()
                        navController.navigate(NavigationScreen.Home.route)
                    }

                }
            }
        }
    } else if (type.equals("Register")) {
        state.signUpFlow?.let {
            when (it) {
                is Resource.Failure<*> -> {
                    if (!state.submitButtonOn) {
                        state.submitButtonOn = true
                        Toast.makeText(mContext, it.exception.message, Toast.LENGTH_LONG).show()
                    }

                }
                Resource.Loading -> {
                    state.submitButtonOn = false
                    CircularProgressIndicator()
                }
                is Resource.Sucess -> {
                    LaunchedEffect(Unit)
                    {
                        state.submitButtonOn = true
                        Toast.makeText(
                            mContext,
                            LangStringUtil.getLangString(R.string.register_success),
                            Toast.LENGTH_LONG
                        )
                            .show()
                        navController.navigate(NavigationScreen.Home.route)
                    }

                }
            }
        }
    }
}

@Composable
fun errorText(desc: String, show: Boolean) {
    if (show) {
        Text(
            text = desc,
            fontSize = 14.sp,
            color = MaterialTheme.colors.error
        )
    }
}
