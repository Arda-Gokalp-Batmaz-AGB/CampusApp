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
    object MyNetwork : NavigationScreen(
        route = "mynetwork",
        icon = Icons.Filled.Group
    ) {
        override val title: String
            get() {
                return LangStringUtil.getLangString(R.string.mynetwork)
            }
    }
    object Publish : NavigationScreen(
        route = "publish",
        icon = Icons.Filled.AddBox
    ) {
        override val title: String
            get() {
                return LangStringUtil.getLangString(R.string.publish)
            }
    }
    object Notifications : NavigationScreen(
        route = "notifications",
        icon = Icons.Filled.Notifications
    ) {
        override val title: String
            get() {
                return LangStringUtil.getLangString(R.string.notifications)
            }
    }
    object JobPostings : NavigationScreen(
        route = "jobpostings",
        icon = Icons.Filled.Work
    ) {
        override val title: String
            get() {
                return LangStringUtil.getLangString(R.string.jobpostings)
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
}