package com.arda.campuslink.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.arda.campuslink.R
import com.arda.campuslink.util.LangStringUtil

sealed class NavigationScreen(
    val route: String,
    val icon: ImageVector
) {
    open val title: String
        get() {
            return ""
        }

    object Home : NavigationScreen(
        route = "home",
        icon = Icons.Filled.Home
    ) {
        override val title: String
            get() {
                return LangStringUtil.getLangString(R.string.home)
            }
    }
    object Login : NavigationScreen(
        route = "login",
        icon = Icons.Filled.Key
    ) {
        override val title: String
            get() {
                return LangStringUtil.getLangString(R.string.login)
            }
    }
    object Profile : NavigationScreen(
        route = "profile",
        icon = Icons.Filled.Person4
    ) {
        override val title: String
            get() {
                return LangStringUtil.getLangString(R.string.profile)
            }
    }

    object Stats : NavigationScreen(
        route = "stats",
        icon = Icons.Filled.Monitor
    ) {
        override val title: String
            get() {
                return LangStringUtil.getLangString(R.string.stats)
            }
    }
}