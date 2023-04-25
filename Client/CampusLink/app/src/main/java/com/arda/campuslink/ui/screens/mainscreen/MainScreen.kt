package com.arda.campuslink.ui.screens.mainscreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.arda.campuslink.ui.auth.AuthViewModel
import com.arda.campuslink.ui.components.LeftBarPopUp
import com.arda.campuslink.ui.components.TopBar
import com.arda.campuslink.ui.navigation.NavGraph
import com.arda.campuslink.ui.navigation.NavigationScreen

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val authViewmodel = hiltViewModel<AuthViewModel>()
    val mainScreenViewmodel = hiltViewModel<MainScreenViewModel>()
    val navController = rememberNavController()
    val state = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = state,
        drawerContent = {
            LeftBarPopUp()
        },
        topBar = {
            TopBar(scope, state,mainScreenViewmodel)
        },
        bottomBar = {
            if ((navController.currentBackStackEntryAsState().value?.destination?.route.toString()
                        != NavigationScreen.Login.route)
            ) {
                BottomBar(navController = navController)
            }
        }
    )
    {
        NavGraph(
            authViewmodel,
            navController = navController,
            state,
            scope,
        )
    }
}


@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        NavigationScreen.Home,
        NavigationScreen.MyNetwork,
        NavigationScreen.Publish,
        NavigationScreen.Notifications,
        NavigationScreen.JobPostings,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation() {
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
            Text(
                text = screen.title,
                color = MaterialTheme.colors.secondary,
                softWrap = false,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
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