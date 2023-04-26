package com.arda.campuslink.ui.screens.homescreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.arda.campuslink.data.DUMMY_FEED_DATA
import com.arda.campuslink.ui.components.FeedItem
import kotlinx.coroutines.CoroutineScope

@Composable
fun HomeScreen(
    navController: NavController,
    coroutineScope: CoroutineScope,
) {
    Box(
        Modifier

    ) {
        LazyColumn(
            modifier = Modifier,
            //state = scrollState
        )
        {
            items(DUMMY_FEED_DATA.size) { idx ->
                FeedItem(
                    feedPost = DUMMY_FEED_DATA[idx],
                    coroutineScope = coroutineScope,
                    navController = navController
                )
            }

        }
    }
}