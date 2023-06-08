package com.arda.campuslink.ui.screens.profilescreen.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arda.campuslink.App.Companion.context
import com.arda.campuslink.MainActivity
import com.arda.campuslink.findActivity
import com.arda.campuslink.ui.screens.profilescreen.ProfileViewModel
import com.arda.campuslink.ui.theme.ThemeController
import com.arda.campuslink.util.LangStringUtil
import com.arda.campuslink.R
import com.arda.campuslink.ui.screens.profilescreen.ProfileUiState
import kotlinx.coroutines.coroutineScope

val optionsList: ArrayList<OptionsData> = ArrayList()

@Composable
fun OptionsItemStyle(item: OptionsData) {
    var iconColor = MaterialTheme.colors.primary
    if (item.iconColor != null)
        iconColor = item.iconColor

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = true) {
                item.callback.invoke()
            }
            .padding(all = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            modifier = Modifier
                .size(32.dp),
            imageVector = item.icon,
            contentDescription = item.title,
            tint = iconColor
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(weight = 3f, fill = false)
                    .padding(start = 16.dp)
            ) {

                Text(
                    text = item.title,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.sailec_bold, FontWeight.Medium))
                    )
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = item.subTitle,
                    style = TextStyle(
                        fontSize = 14.sp,
                        letterSpacing = (0.8).sp,
                        fontFamily = FontFamily(Font(R.font.sailec_regular, FontWeight.Normal)),
                        color = Color.Gray
                    )
                )

            }

            Icon(
                modifier = Modifier
                    .weight(weight = 1f, fill = false),
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = item.title,
                tint = Color.Black.copy(alpha = 0.70f)
            )
        }

    }
}

fun prepareOptionsData(state: ProfileUiState, model: ProfileViewModel, mContext: Context) {

    val appIcons = Icons.Outlined
    var themeIcon = appIcons.DarkMode
    var privacyIcon = appIcons.LockOpen
    var privacyText = LangStringUtil.getLangString(R.string.profile_open)

    var connectionIcon = appIcons.PersonAdd
    var connectionText = LangStringUtil.getLangString(R.string.add_connect)

    var iconColor = Color.DarkGray

    var themeText = LangStringUtil.getLangString(R.string.change_theme_dark)
    if (ThemeController.themeIsDark) {
        themeIcon = appIcons.LightMode
        themeText = LangStringUtil.getLangString(R.string.change_theme_light)
        iconColor = Color.Yellow
    }
    if (!state.currentProfileUser!!.profilePublic) {
        privacyIcon = appIcons.Lock
        privacyText = LangStringUtil.getLangString(R.string.profile_private)
    }
    val isAlreadyConnected = state.currentProfileUser!!.connections.contains(state.currentProfileUser!!.connections.find { it.UID == model.authenticatedUser.UID })
    if (isAlreadyConnected) {
        connectionText = LangStringUtil.getLangString(R.string.remove_connect)
    }
    if(model.authenticatedUser.UID != state.currentProfileUser!!.UID)
    {
        optionsList.add(
            OptionsData(
                icon = connectionIcon,
                iconColor = iconColor,
                title = LangStringUtil.getLangString(R.string.connect),
                subTitle = connectionText,
                callback = {
                    if(!isAlreadyConnected)
                    {
                        model.connectionRequestToUser()
                    }
                    else
                    {
                        model.removeConnectionToUser()
                    }
                }
            )
        )
    }

    if(model.authenticatedUser.UID == state.currentProfileUser!!.UID)
    {
        optionsList.add(
            OptionsData(
                icon = privacyIcon,
                iconColor = iconColor,
                title = LangStringUtil.getLangString(R.string.profile_visibility),
                subTitle = privacyText,
                callback = {
                    model.switchProfileVisibility()
                }
            )
        )
    }

    optionsList.add(
        OptionsData(
            icon = themeIcon,
            iconColor = iconColor,
            title = LangStringUtil.getLangString(R.string.theme),
            subTitle = themeText,
            callback = {
                ThemeController.switchcurrentTheme()
            }
        )
    )

    optionsList.add(
        OptionsData(
            icon = appIcons.Logout,
            title = LangStringUtil.getLangString(R.string.logout),
            subTitle = LangStringUtil.getLangString(R.string.logout_info),
            callback = {
                    model.logout()
            }
        )
    )

}

data class OptionsData(
    val icon: ImageVector,
    val iconColor: Color? = null,
    val title: String,
    val subTitle: String,
    val callback: () -> Unit
)