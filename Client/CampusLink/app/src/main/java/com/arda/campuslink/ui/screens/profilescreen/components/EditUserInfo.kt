package com.arda.campuslink.ui.screens.profilescreen.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import com.arda.campuslink.ui.screens.profilescreen.ProfileViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.arda.campuslink.ui.theme.ThemeController
import coil.compose.rememberImagePainter
import com.arda.campuslink.ui.screens.profilescreen.ProfileUiState
import com.arda.campuslink.util.LangStringUtil
import com.arda.campuslink.R

@Composable
fun EditUserInfo(state: ProfileUiState, model: ProfileViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        editTopBar(state,model)
        AccountImage(state,model)
        AccountDisplayName(state,model)
    }

}


@Composable
private fun editTopBar(state: ProfileUiState,model: ProfileViewModel) {
    val context = LocalContext.current
    var color =  colorResource(R.color.darkgreen)

    var textColor = R.color.pop_up_text
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    )
    {
        IconButton(
            modifier = Modifier
                .weight(weight = 1f, fill = false),
            onClick = {
                model.resetEnteredValues()
            }) {


            Column(
                modifier = Modifier.wrapContentSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    imageVector = Icons.Outlined.GppBad,
                    contentDescription = "Edit Details",
                    tint = Color.Red
                )
                Text(
                    text = "Cancel",
                    style = MaterialTheme.typography.subtitle2,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = textColor)
                )
            }

        }
        IconButton(
            modifier = Modifier
                .weight(weight = 1f, fill = false),
            onClick = {
                model.updateProfile()
            }) {
            Column(
                modifier = Modifier.wrapContentSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {

                Icon(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = "Edit Details",
                    tint = color
                )
                Text(
                    text = "Save",
                    style = MaterialTheme.typography.subtitle2,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = textColor)
                )
            }

        }
    }
//    profileFlow?.value?.let {
//        when (it) {
//            is Resource.Failure<*> -> {
//                Toast.makeText(
//                    context, LangStringUtil.getLangString(R.string.network_error),
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//            Resource.Loading -> {
//                Log.v("REPO", "UPDATE loading!")
//            }
//            is Resource.Sucess -> {
//                Log.v("REPO", "UPDATE SUCESS!")
//            }
//        }
//    }
}

@Composable
private fun AccountImage(state: ProfileUiState, model: ProfileViewModel) {
    val painter = rememberImagePainter(
        if (state.currentProfileUser?.avatar== null) {
            R.drawable.person
        } else {
            state.currentProfileUser!!.avatar
        }
    )
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { state.currentProfileUser?.avatar = it }
    }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .size(100.dp)
                .clickable { if (state.editMode) launcher.launch("image/*") },
            shape = CircleShape
        )
        {

            Image(
                modifier = Modifier
                    .fillMaxSize(),
                painter = painter,
                contentDescription = "Profile Image",
                contentScale = ContentScale.Crop
            )
        }
        var textColor = R.color.pop_up_text
        if (!ThemeController.themeIsDark) {
            textColor = R.color.pop_up_text_light
        }
        Text(
            text = LangStringUtil.getLangString(R.string.change_profile_picture),
            color = colorResource(id = textColor)
        )

    }
}

@Composable
private fun AccountDisplayName(state: ProfileUiState, model: ProfileViewModel) {
    var focused by remember { mutableStateOf(false) }
    TextField(
        modifier = Modifier
            .width(250.dp)
            .border(1.dp, MaterialTheme.colors.primary, shape = RoundedCornerShape(15.dp))
            .onFocusChanged {
                focused = it.isFocused == true
            },
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
        placeholder = { if (!focused) Text(LangStringUtil.getLangString(R.string.displayname)) },
        label = { Text(LangStringUtil.getLangString(R.string.displayname)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        leadingIcon = {
            Icon(
                Icons.Filled.Badge,
                contentDescription = "DisplayName",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
        },
        value = state.enteredUserName, onValueChange = { newText ->
            run {
                if (newText.length <= 25) {
                    state.enteredUserName = newText
                }
            }
        })

}

//@Composable
//private fun AccountEmail(model: ProfileViewModel) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp),
//        horizontalArrangement = Arrangement.spacedBy(20.dp)
//    )
//    {
//        if (!model.currentUser?.displayName.isNullOrEmpty()) {
//            Text(text = model.currentUser!!.email!!)
//        } else {
//            Text(text = "......")
//        }
//        var focused by remember { mutableStateOf(false) }
//        TextField(
//            modifier = Modifier
//                .width(250.dp)
//                .border(1.dp, MaterialTheme.colors.primary, shape = RoundedCornerShape(15.dp))
//                .onFocusChanged {
//                    focused = it.isFocused == true
//                },
//            colors = TextFieldDefaults.textFieldColors(
//                textColor = Color.Black,
//                disabledTextColor = Color.Transparent,
//                focusedIndicatorColor = Color.Transparent,
//                unfocusedIndicatorColor = Color.Transparent,
//                disabledIndicatorColor = Color.Transparent,
//                errorIndicatorColor = Color.Transparent,
//                errorCursorColor = Color.Black,
//                errorLeadingIconColor = Color.Red
//            ),
//            shape = RoundedCornerShape(15.dp),
//            placeholder = { if (!focused) Text(LangStringUtil.getLangString(R.string.email)) },
//            label = { Text(LangStringUtil.getLangString(R.string.email)) },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
//            leadingIcon = {
//                Icon(
//                    Icons.Filled.Badge,
//                    contentDescription = "Email",
//                    modifier = Modifier.size(ButtonDefaults.IconSize)
//                )
//            },
//            value = model.enteredEmail, onValueChange = { newText ->
//                run {
//                    if (newText.length <= 30) {
//                        model.enteredEmail = newText
//                    }
//                }
//            })
//    }
//
//}