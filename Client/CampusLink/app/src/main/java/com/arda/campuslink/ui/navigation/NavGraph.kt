package com.arda.campuslink.ui.navigation

import android.util.Log
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.arda.campuslink.ui.auth.AuthViewModel
import com.arda.campuslink.ui.auth.authMainLayout
import com.arda.campuslink.ui.screens.homescreen.HomeScreen
import com.arda.campuslink.ui.screens.mynetworkscreen.MyNetworkScreen
import com.arda.campuslink.ui.screens.notificationsscreen.NotificationScreen
import com.arda.campuslink.ui.screens.openingsscreen.OpeningsScreen
import com.arda.campuslink.ui.screens.profilescreen.ProfileScreen
import com.arda.campuslink.ui.screens.publishscreen.PublishScreen
import com.arda.campuslink.util.DebugTags
import kotlinx.coroutines.CoroutineScope

@Composable
fun NavGraph(
    authViewModel: AuthViewModel,
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope
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
          HomeScreen(navController = navController, scaffoldState = scaffoldState, coroutineScope = coroutineScope)
        }
        composable(route = NavigationScreen.MyNetwork.route)
        {
            MyNetworkScreen()
        }
        composable(route = NavigationScreen.Notifications.route)
        {
            NotificationScreen()
        }
        composable(route = NavigationScreen.JobPostings.route)
        {
            OpeningsScreen()
        }
        composable(route = NavigationScreen.Publish.route)
        {
            PublishScreen()
        }
        composable(route = NavigationScreen.Profile.route)
        {
           ProfileScreen(navController)
        }
    }
}
