package com.arda.campuslink.ui.screens.homescreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.arda.campuslink.data.dummyFeedData
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
            items(dummyFeedData.size) { idx ->
                FeedItem(
                    linkedinPost = dummyFeedData[idx],
                    coroutineScope = coroutineScope,
                    navController = navController
                )
            }

        }
    }
}