package com.arda.campuslink.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arda.campuslink.R
import com.arda.campuslink.ui.screens.leftbarpopup.LeftBarPopUpUiState
import com.arda.campuslink.ui.screens.mainscreen.MainScreenViewModel
import com.arda.campuslink.util.LangStringUtil

@Composable
fun SearchBar(state: LeftBarPopUpUiState, mainScreenViewmodel: MainScreenViewModel, textValue : String = "") {
    Row(
        modifier = Modifier
            .width(280.dp)
            .height(45.dp)
            .clip(shape = RoundedCornerShape(2.dp))
            .background(color = MaterialTheme.colors.secondaryVariant)
            .padding(top = 0.dp)
    ) {

        var focused by remember { mutableStateOf(false) }
        TextField(
            modifier = Modifier
                .width(450.dp)
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
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 11.sp,
            ),
        shape = RoundedCornerShape(15.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            leadingIcon = {
                Icon(
                    Icons.Filled.Search,
                    tint = Color.Black,
                    modifier = Modifier
                        .width(24.dp)
                        .padding(start = 4.dp, top = 0.dp),
                    contentDescription = "messages icon",
                )
            },
            value = state.enteredTextParameter, onValueChange = { newText ->
                run {
                    if (newText.length <= 55) {
                        mainScreenViewmodel.updateResearchText(newText)
                    }
                }
            })
    }
}
