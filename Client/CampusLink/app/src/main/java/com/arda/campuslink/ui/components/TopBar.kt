package com.arda.campuslink.ui.components

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.arda.campuslink.ui.screens.mainscreen.MainScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TopBar(
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState,
    mainScreenViewmodel: MainScreenViewModel
) {
    val state by mainScreenViewmodel.uiState.collectAsState()

    TopAppBar(
        backgroundColor = Color.White,
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
                painter = rememberAsyncImagePainter(state.currentMinimizedUser?.avatar),
                modifier = Modifier
                    .size(34.dp)
                    .clip(shape = RoundedCornerShape(17.dp))
                    .clickable {
                        coroutineScope.launch { scaffoldState.drawerState.open() }
                    },
                contentScale = ContentScale.Crop,
                contentDescription = "profile drawer icon",
            )

            SearchBar(state,mainScreenViewmodel)

            Icon(
                Icons.Filled.Message,
                tint = Color.Black,
                modifier = Modifier.size(24.dp),
                contentDescription = "messages icon",
            )
        }
    }
}