package com.arda.campuslink.ui.screens.publishscreen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.arda.campuslink.ui.components.PublishTopBar

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PublishScreen(navController: NavHostController) {
    val openDialog = remember { mutableStateOf(true) }
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
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Yellow),
                topBar = {
                    PublishTopBar(openDialog,navController)
                },
            )
            {
                Box(
                ) {
                    Text("QDSADSADS")
                }
            }


        }

    }

}