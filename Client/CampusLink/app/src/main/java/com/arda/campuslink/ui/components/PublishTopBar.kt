package com.arda.campuslink.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.arda.campuslink.data.dummyUserData
import kotlinx.coroutines.launch

@Composable
fun PublishTopBar(openDialog: MutableState<Boolean>,navController: NavHostController) {
    TopAppBar(
        backgroundColor = Color.White,
        elevation = 5.dp,
    ) {
        Row(
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                Icons.Filled.Remove,
                modifier = Modifier
                    .size(34.dp)
                    .clip(shape = RoundedCornerShape(17.dp))
                    .clickable {
                        openDialog.value = false
                        navController.popBackStack()

                    },
                contentScale = ContentScale.Crop,
                contentDescription = "profile drawer icon",
            )

            Row(
                modifier = Modifier
                    .width(280.dp)
                    .height(40.dp)
                    .clip(shape = RoundedCornerShape(2.dp))
                    .background(color = MaterialTheme.colors.secondaryVariant)
            ) {
                Icon(
                    Icons.Filled.Search,
                    tint = Color.Black,
                    modifier = Modifier
                        .width(24.dp)
                        .padding(start = 4.dp, top = 10.dp),
                    contentDescription = "messages icon",
                )
                Text(
                    text = "Pesquisar",
                    style = TextStyle(color = Color.Black),
                    modifier = Modifier.padding(start = 8.dp, top = 10.dp)
                )
            }

            Icon(
                Icons.Filled.Add,
                tint = Color.Black,
                modifier = Modifier.size(24.dp),
                contentDescription = "messages icon",
            )
        }
    }
}