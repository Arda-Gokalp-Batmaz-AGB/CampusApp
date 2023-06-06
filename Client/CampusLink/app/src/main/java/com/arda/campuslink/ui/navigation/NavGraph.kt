package com.arda.campuslink.ui.navigation

import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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
    coroutineScope: CoroutineScope,
) {
    val initialRoute: String
    var lastRoute: String

    if (authViewModel.currentUser != null) {
        initialRoute = NavigationScreen.Home.route
        lastRoute = NavigationScreen.Home.route
    } else {
        initialRoute = NavigationScreen.Login.route
        lastRoute = NavigationScreen.Login.route
    }


    NavHost(
        navController = navController,
        startDestination = initialRoute
    )
    {
        Log.v(DebugTags.UITag.tag, "TTTT")
        composable(route = NavigationScreen.Login.route)
        {
            authMainLayout(navController = navController)
        }
        composable(route = NavigationScreen.Home.route)
        {
            Log.v(DebugTags.UITag.tag, "Last Route: " + lastRoute)
            Log.v(DebugTags.UITag.tag, "Current Route: " + navController.currentBackStackEntry?.destination?.route.toString())

            lastRoute = navController.currentBackStackEntry?.destination?.route.toString()
            Log.v(DebugTags.UITag.tag, "TTTT")
            HomeScreen(
                navController = navController,
                coroutineScope = coroutineScope,
            )
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
            PublishScreen( navController = navController)
        }
    }
}
