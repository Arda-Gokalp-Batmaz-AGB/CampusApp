package com.arda.campuslink.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.arda.campuslink.ui.auth.AuthViewModel
import com.arda.campuslink.ui.auth.authMainLayout
import com.arda.campuslink.ui.screens.profilescreen.ProfileScreen
import com.arda.campuslink.util.DebugTags

@Composable
fun NavGraph(
    authViewModel: AuthViewModel,
    navController: NavHostController
) {
    val initialRoute : String
    if(authViewModel.currentUser != null)
    {
        initialRoute = NavigationScreen.Home.route
    }
    else
    {
        initialRoute = NavigationScreen.Login.route
    }
    Log.v(DebugTags.UITag.tag,"Test: " + initialRoute)
    NavHost(
        navController = navController,
        startDestination = initialRoute
    )
    {
        composable(route = NavigationScreen.Login.route)
        {
            authMainLayout(navController=navController)
        }
        composable(route = NavigationScreen.Home.route)
        {
          //  PlayScreen(playViewModel)
        }
        composable(route = NavigationScreen.Profile.route)
        {
           ProfileScreen(navController)
        }
        composable(route = NavigationScreen.Stats.route)
        {
          //  StatsScreen(playViewModel, statsViewModel, navController)
        }
    }
}
