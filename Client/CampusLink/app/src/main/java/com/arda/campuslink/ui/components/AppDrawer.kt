package com.arda.campuslink.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arda.campuslink.data.dummyUserData

@Composable
fun AppDrawer() {
    Surface(
        color = Color.White
    ) {
        Column {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                Image(
                    painter = painterResource(id = dummyUserData[0].avatar),
                    modifier = Modifier
                        .size(50.dp)
                        .clip(shape = RoundedCornerShape(25.dp))
                        .clickable {

                        },
                    contentScale = ContentScale.Crop,
                    contentDescription = ""
                )
                Text(
                    text = dummyUserData[0].name,
                    style = TextStyle(color = Color.Black, fontSize = 16.sp),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Ver perfil",
                    style = TextStyle(color = Color.Gray, fontSize = 12.sp),
                )
                Divider(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp))
                Text(
                    text = "Grupos",
                    style = TextStyle(color = Color.Black, fontSize = 16.sp),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = "Eventos",
                    style = TextStyle(color = Color.Black, fontSize = 16.sp),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Spacer(modifier = Modifier.weight(weight = 1f))
                Divider()
                DrawerBottomItem(icon = Icons.Filled.Settings, title = "Configurações", isLink = false)

            }
        }

    }
}

@Composable
private fun DrawerBottomItem(title: String, icon: ImageVector, isLink: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            icon,
            contentDescription = "icon drawer",
            modifier = Modifier.size(20.dp),
        )
        Text(
            text = title,
            style = TextStyle(color = if(isLink) Color.Blue else Color.Black, fontSize = 14.sp),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}