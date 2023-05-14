package com.arda.campuslink.ui.screens.commentscreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.arda.campuslink.domain.model.Comment
import com.arda.campuslink.domain.model.ExtendedUser
import com.arda.campuslink.domain.model.FeedPost
import com.arda.campuslink.ui.components.CommentItem
import com.arda.campuslink.ui.components.CommentTopBar
import com.arda.campuslink.ui.components.FeedItem
import com.arda.campuslink.ui.components.ProfileTopBar
import com.arda.campuslink.ui.screens.homescreen.HomeViewModel
import com.arda.campuslink.ui.screens.homescreen.OnBottomReached
import com.arda.campuslink.ui.screens.profilescreen.ProfileViewModel
import com.arda.campuslink.util.DebugTags
import com.arda.mainapp.auth.Resource
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.type.DateTime

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CommentScreen(openPost: MutableState<Boolean>, feedPost: FeedPost) {
//    val profileViewmodel = hiltViewModel<ProfileViewModel>()
//    val state by profileViewmodel.uiState.collectAsState()
//    Log.v(DebugTags.UITag.tag,"Current profile view model= ${profileViewmodel}")
    AnimatedVisibility(visible = openPost.value, enter = fadeIn(), exit = fadeOut()) {

        Dialog(
            onDismissRequest = {
                openPost.value = false
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false // experimental
            )
        ) {
//            LaunchedEffect(key1 = feedPost.UID)
//            {
//                Log.v(DebugTags.UITag.tag,"New Profile come")
//
//                profileViewmodel.getUserProfile(feedPost)
//            }
//            state.profileFlow?.let {
//                when (it) {
//                    is Resource.Failure<*> -> {
//                    }
//                    Resource.Loading -> {
//                        CircularProgressIndicator()
//                    }
//                    is Resource.Sucess -> {
//                        LaunchedEffect(Unit)
//                        {
//                            profileViewmodel.updateCurrentProfileUser(it.result)
//                        }
//
//                    }
//                }
//            }

            if (openPost.value)//&& state.currentProfileUser != null)
            {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    topBar = {
                        CommentTopBar(openPost)
                    },
                )
                {
                    CommentBody(feedPost)
                }
            }

        }
    }
}

@Composable
fun CommentBody(currentPost: FeedPost) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        backgroundColor = MaterialTheme.colors.background,
        elevation = 5.dp,
    )
    {
//        Column() {
//            Box(modifier = Modifier
//                .fillMaxWidth()
//                .wrapContentHeight())
//            {
//                FeedItem(
//                    feedPost = currentPost,
//                )
//            }

            CommentSection(currentPost)
//        }
    }


}

@Composable
fun CommentSection(currentPost: FeedPost) {
    val commentViewmodel = hiltViewModel<CommentViewModel>()
    val state by commentViewmodel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = state.isFeedRefreshing)
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
                    Log.v(DebugTags.UITag.tag, "Comments Fetched Sucessfully!!")
                    commentViewmodel.updateCurrentFeed(it.result)
                }

            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState
    )
    {
        item {
            Box(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight())
            {
                FeedItem(
                    feedPost = currentPost,
                )
            }
        }
        items(state.currentFeed.size) { idx ->
            CommentItem(
                feedComment = state.currentFeed[idx],
            )
        }

    }
    listState.OnBottomReached(buffer = 2) {
        commentViewmodel.fetchNewComments(currentPost)
    }


}
