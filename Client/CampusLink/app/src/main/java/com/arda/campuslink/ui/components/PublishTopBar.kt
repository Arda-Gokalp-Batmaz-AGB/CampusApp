package com.arda.campuslink.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.arda.campuslink.R
import com.arda.campuslink.data.dummyUserData
import com.arda.campuslink.ui.screens.publishscreen.PublishUiState
import com.arda.campuslink.ui.screens.publishscreen.PublishViewModel
import com.arda.campuslink.util.LangStringUtil
import kotlinx.coroutines.launch

@Composable
fun PublishTopBar(
    openDialog: MutableState<Boolean>,
    publishViewModel: PublishViewModel,
    navController: NavHostController
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        elevation = 5.dp,
    ) {
        Row(
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                Icons.Filled.Close,
                modifier = Modifier
                    .size(34.dp)
                    .clip(shape = RoundedCornerShape(17.dp))
                    .clickable {
                        openDialog.value = false
                        navController.popBackStack()

                    },
                contentScale = ContentScale.Crop,
                contentDescription = "profile drawer icon",
            )
            Button(
                onClick = { publishViewModel.createPost() },
                shape = RoundedCornerShape(15.dp),
            ) {
                Text(LangStringUtil.getLangString(R.string.post))
            }
        }
    }
}