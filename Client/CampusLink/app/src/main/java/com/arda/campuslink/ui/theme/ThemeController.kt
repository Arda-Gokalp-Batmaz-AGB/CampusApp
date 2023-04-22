package com.arda.campuslink.ui.theme

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arda.mainapp.App

class ThemeController {
    companion object {

        private val defaultThemeDark = true
        var themeIsDark by mutableStateOf(defaultThemeDark)
        private val prefences = App.context.getSharedPreferences("settings", Context.MODE_PRIVATE)

        fun switchcurrentTheme() {
            themeIsDark = !themeIsDark
            saveTheme()
        }

        init {
            getSavedTheme()
        }

        private fun getSavedTheme() {
            val theme = prefences.getBoolean("theme", defaultThemeDark)
            themeIsDark = theme
            checkThemeMode()
        }

        private fun saveTheme() {
            val editor = prefences.edit()
            editor.putBoolean("theme", themeIsDark)
            editor.commit();
        }

        private fun checkThemeMode() {
            var mode: Int
            if (themeIsDark) {
                mode = Configuration.UI_MODE_NIGHT_YES
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                mode = Configuration.UI_MODE_NIGHT_NO
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            val config = App.context.resources.getConfiguration()
            val displayMetrics = App.context.getResources().getDisplayMetrics();
            config.uiMode = mode
            App.context.resources.updateConfiguration(config, displayMetrics)
            var theme = ""
            val nightModeFlags: Int = App.context.resources.getConfiguration().uiMode and
                    Configuration.UI_MODE_NIGHT_MASK
            when (nightModeFlags) {
                Configuration.UI_MODE_NIGHT_YES -> theme = "DARK"
                Configuration.UI_MODE_NIGHT_NO -> theme = "LIGHT"
                Configuration.UI_MODE_NIGHT_UNDEFINED -> theme = "UNDEFINED"
            }
            Log.v("REPO", "CURRENT THEME:" + theme)
            Log.v(
                "REPO",
                "CURRENT THEME2:" + AppCompatDelegate.getDefaultNightMode()
            )

        }
    }
}