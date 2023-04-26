package com.arda.campuslink.ui.screens.publishscreen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.arda.campuslink.R
import com.arda.campuslink.ui.components.PublishTopBar
import com.arda.campuslink.util.LangStringUtil
import com.arda.mainapp.auth.Resource

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PublishScreen(navController: NavHostController) {
    val openDialog = remember { mutableStateOf(true) }
    AnimatedVisibility(visible = openDialog.value, enter = fadeIn(), exit = fadeOut()) {

        Dialog(
            onDismissRequest = {
                openDialog.value = false
                navController.popBackStack()
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false // experimental
            )
        ) {
            if (openDialog.value) {
                val publishViewModel = hiltViewModel<PublishViewModel>()
                val state by publishViewModel.uiState.collectAsState()

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    topBar = {
                        PublishTopBar(openDialog, publishViewModel, navController)
                    },
                )
                {
                    state.postCreateFlow?.let {
                        when (it) {
                            is Resource.Failure<*> -> {
                            }
                            Resource.Loading -> {
                                CircularProgressIndicator()
                            }
                            is Resource.Sucess -> {
                                LaunchedEffect(Unit)
                                {
                                    //TO-DO Toast message
                                    openDialog.value = false
                                    navController.popBackStack()
                                }

                            }
                        }
                    }

                    postArea(state, publishViewModel)
                }


            }

        }
    }

}

@Composable
fun postArea(state: PublishUiState, publishViewModel: PublishViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()

    )
    {
        postTopArea(state)
        postTextArea(state, publishViewModel)
        postBottomArea()
    }

}

@Composable
fun postTopArea(state: PublishUiState) {

    Row(
        modifier = Modifier
            .padding(top = 8.dp)
            .clickable {
                //To-DO Go Profile
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = rememberImagePainter(state.currentMinimizedUser?.avatar),
            contentDescription = "",
            modifier = Modifier
                .padding(start = 8.dp)
                .size(50.dp)
                .clip(shape = RoundedCornerShape(25.dp))
                .clickable {
                },
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.size(8.dp))
        Column {
            Column {
                Text(
                    color = Color.Black,
                    text = "${state.currentMinimizedUser?.userName}",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)
                )
            }
        }

    }
}

@Composable
fun postTextArea(state: PublishUiState, publishViewModel: PublishViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.93f)
    ) {
        TextField(
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.background
            ), value = state.description,
            label = {
                Text(LangStringUtil.getLangString(R.string.what_hint))
            },
            onValueChange = { publishViewModel.updateDescription(it) })
    }
}

@Composable
fun postBottomArea() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(1f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.background),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically

        )
        {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    Icons.Filled.PhotoCamera,
                    contentDescription = "",
                    modifier = Modifier
                        .size(30.dp),
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    Icons.Filled.Image,
                    contentDescription = "",
                    modifier = Modifier
                        .size(30.dp),
                )
            }
        }
    }
}