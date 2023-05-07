package com.arda.campuslink.ui.screens.homescreen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.arda.campuslink.ui.components.FeedItem
import com.arda.campuslink.ui.screens.mainscreen.MainScreenViewModel
import com.arda.campuslink.util.DebugTags
import com.arda.mainapp.auth.Resource
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun HomeScreen(
    navController: NavController,
    coroutineScope: CoroutineScope,
) {
    val homeViewmodel = hiltViewModel<HomeViewModel>()
    val state by homeViewmodel.uiState.collectAsState()
    val listState = rememberLazyListState()
    Box(
        Modifier

    ) {
        Log.v(DebugTags.UITag.tag, "Home Triggered")
//        homeViewmodel.refreshCurrentFeed()
        homeViewmodel.getNewlyAddedPostsByUser()
        state.feedFlow?.let {
            when (it) {
                is Resource.Failure<*> -> {
                }
                Resource.Loading -> {
                    CircularProgressIndicator()
                }
                is Resource.Sucess -> {
                    LaunchedEffect(Unit)
                    {
                        Log.v(DebugTags.UITag.tag, "Feed Fetched Sucessfully!!")
                        homeViewmodel.updateCurrentFeed(it.result)
                    }

                }
            }
        }


        val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = state.isFeedRefreshing)

        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = { homeViewmodel.refreshCurrentFeed() }) {
            LazyColumn(
                modifier = Modifier,
                state = listState
            )
            {
                items(state.currentFeed.size) { idx ->
                    FeedItem(
                        feedPost = state.currentFeed[idx],
                        coroutineScope = coroutineScope,
                        navController = navController
                    )
                }

            }
            //if(state.currentFeed.size != 0)
            listState.OnBottomReached(buffer = 3) {
                homeViewmodel.fetchNewPosts()
            }
        }
    }
}

@Composable
fun LazyListState.OnBottomReached(
    // tells how many items before we reach the bottom of the list
    // to call onLoadMore function
    buffer: Int = 0,
    onLoadMore: () -> Unit
) {
    // Buffer must be positive.
    // Or our list will never reach the bottom.
    require(buffer >= 0) { "buffer cannot be negative, but was $buffer" }

    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf true

            Log.v(DebugTags.UITag.tag, "lastVisibleItem = ${lastVisibleItem.index}")
            Log.v(DebugTags.UITag.tag, "Layout Info = ${layoutInfo.totalItemsCount}")

            // subtract buffer from the total items
            lastVisibleItem.index >= layoutInfo.totalItemsCount - 1 - buffer
        }
    }

    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { Pair(shouldLoadMore.value, layoutInfo.totalItemsCount) }
            .collect { if (it.first) onLoadMore() }
    }
}