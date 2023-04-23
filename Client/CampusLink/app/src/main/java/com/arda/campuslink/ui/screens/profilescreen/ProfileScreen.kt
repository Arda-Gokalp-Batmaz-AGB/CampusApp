package com.arda.campuslink.ui.screens.profilescreen

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.arda.campuslink.ui.auth.AuthViewModel
import com.arda.campuslink.ui.navigation.NavGraph
import com.arda.campuslink.ui.navigation.NavigationScreen

@Composable
fun ProfileScreen(navController: NavHostController) {
    val profileViewmodel = hiltViewModel<ProfileViewModel>()
    Button(onClick = {
        profileViewmodel.logout()
        navController.navigate(NavigationScreen.Login.route) {
            popUpTo(navController.graph.findStartDestination().id)
            launchSingleTop = true
        }
    }) {
        Text("Logout")

    }
}