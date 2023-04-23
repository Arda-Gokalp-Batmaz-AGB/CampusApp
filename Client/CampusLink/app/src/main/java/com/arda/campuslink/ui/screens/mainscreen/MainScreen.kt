package com.arda.campuslink.ui.screens.mainscreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.arda.campuslink.ui.auth.AuthViewModel
import com.arda.campuslink.ui.navigation.NavGraph
import com.arda.campuslink.ui.navigation.NavigationScreen

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val mContext = LocalContext.current
    val authViewmodel = hiltViewModel<AuthViewModel>()
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            if (navController.currentBackStackEntryAsState().value?.destination?.route.toString() != NavigationScreen.Login.route
            ) {
                BottomBar(navController = navController)
            }
        }
    )
    {

        NavGraph(
            authViewmodel,
            navController = navController
        )
    }
}


@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        NavigationScreen.Home,
        NavigationScreen.Profile,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )

        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: NavigationScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    BottomNavigationItem(

        label = {
            Text(text = screen.title)
        },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "Navigation Icon"
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}