package com.arda.campuslink.ui.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.hilt.navigation.compose.hiltViewModel

import coil.compose.rememberImagePainter
import com.arda.campuslink.domain.model.FeedPost
import com.arda.campuslink.R
import com.arda.campuslink.domain.model.Comment
import com.arda.campuslink.ui.screens.commentscreen.CommentScreen
import com.arda.campuslink.ui.screens.commentscreen.CommentViewModel
import com.arda.campuslink.ui.screens.homescreen.HomeViewModel
import com.arda.campuslink.ui.screens.profilescreen.ProfileScreen
import com.arda.campuslink.util.LangStringUtil

@Composable
fun FeedItem(
    feedPost: FeedPost,
    commentViewModel: CommentViewModel? = null,
    homeViewModel: HomeViewModel
    // coroutineScope: CoroutineScope,
    // navController: NavController
) {

    val constraints = ConstraintSet {
        val postTopBar = createRefFor("post_top_bar")
        val postFollowButton = createRefFor("post_follow_button")
        val postTextAndImage = createRefFor("post_text_and_image")
        val postOptions = createRefFor("post_options")

        constrain(postTopBar) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
        }

        constrain(postFollowButton) {
            top.linkTo(parent.top)
            end.linkTo(parent.end)
        }

        constrain(postTextAndImage) {
            top.linkTo(postTopBar.bottom)
            start.linkTo(parent.start)
        }

        constrain(postOptions) {
            top.linkTo(postTextAndImage.bottom)
            end.linkTo(parent.end)
            start.linkTo(parent.start)
        }

    }

    ConstraintLayout(
        constraints, modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
            .background(color = Color.White)
    ) {
        PostTopItem(
            feedPost,
            modifier = Modifier.layoutId("post_top_bar"),
            //  coroutineScope,
            // navController
        )
        FollowButton(modifier = Modifier.layoutId("post_follow_button"))
        PostTextAndImage(
            feedPost = feedPost,
            modifier = Modifier.layoutId("post_text_and_image")
        )
        PostOptions(
            feedPost = feedPost,
            commentViewModel = commentViewModel,
            modifier = Modifier.layoutId("post_options"),
            homeViewModel = homeViewModel
        )
    }

}

@Composable
fun PostTopItem(
    feedPost: FeedPost,
    modifier: Modifier = Modifier,
    //coroutineScope: CoroutineScope,
    //navController: NavController
) {
    val openProfile = remember { mutableStateOf(false) }
    if (openProfile.value) {
        feedPost?.let { ProfileScreen(openProfile, user = feedPost.user) }
    }
    Row(
        modifier = modifier
            .padding(top = 8.dp)
            .clickable {
                openProfile.value = true
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Image(
            painter = rememberImagePainter(feedPost.user.avatar),
            contentDescription = "",
            modifier = Modifier
                .padding(start = 8.dp)
                .size(40.dp)
                .clip(shape = RoundedCornerShape(25.dp)),
            contentScale = ContentScale.Crop,
        )

        Spacer(modifier = Modifier.size(8.dp))
        Column {
            Column {
                Text(
                    color = Black,
                    text = "${feedPost.user.userName}",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 12.sp)
                )
                Text(
                    color = Color.Gray,
                    text = feedPost.user.jobTitle,
                    style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 12.sp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    color = Color.Gray,
                    text = "${feedPost.timeAgo()} • ",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 12.sp)
                )
                Icon(
                    Icons.Filled.Language,
                    contentDescription = "",
                    tint = Color.DarkGray,
                    modifier = Modifier.size(10.dp)
                )
            }
        }

    }
}

@Composable
private fun FollowButton(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .padding(end = 8.dp, top = 8.dp)
            .clickable {
                //To-DO Follow
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            Icons.Filled.Add,
            contentDescription = "",
            tint = Blue
        )
        Text(
            text = LangStringUtil.getLangString(R.string.follow),
            color = Blue,
            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun PostTextAndImage(modifier: Modifier = Modifier, feedPost: FeedPost) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        ExpandableText(
            text = feedPost.description,
            style = TextStyle(fontSize = 12.sp),
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
        )

        if (feedPost.image != null) {
            Image(
                contentScale = ContentScale.FillWidth,
                painter = painterResource(id = feedPost.image),
                contentDescription = "",
                modifier = Modifier.fillMaxWidth()
            )
        }

    }
}

@Composable
fun PostOptions(
    modifier: Modifier = Modifier,
    commentViewModel: CommentViewModel?,
    feedPost: FeedPost,
    homeViewModel: HomeViewModel
) {
    Column(modifier = modifier.padding(4.dp)) {

        postLikesReactions(
            modifier = Modifier.padding(top = 8.dp),
            icons = listOf(
                Icons.Filled.ThumbUp,

            ),
            feedPost = feedPost,
        )

        Row {
            Divider(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val openComments = remember { mutableStateOf(false) }
            if (openComments.value) {
                feedPost?.let {
                    CommentScreen(
                        openPost = openComments,
                        feedPost = feedPost,
                        homeViewModel = homeViewModel
                    )
                }
            }
            PostItem(LangStringUtil.getLangString(R.string.like), Icons.Filled.ThumbUp)
            {
                homeViewModel.interactWithPost(feedPost,"like")
            }
            PostItem(LangStringUtil.getLangString(R.string.dislike), Icons.Filled.ThumbDown)
            {
                homeViewModel.interactWithPost(feedPost,"dislike")

            }
            PostItem(LangStringUtil.getLangString(R.string.comment), Icons.Filled.Comment)
            {
                if (commentViewModel == null) {
                    openComments.value = true
                } else {
                    commentViewModel.updateFocusedComponent(feedPost.postId)
                }
            }
//            PostItem(LangStringUtil.getLangString(R.string.Share), Icons.Filled.Share)
//            PostItem(LangStringUtil.getLangString(R.string.Send), Icons.Filled.Send)
        }
    }
}

@Composable
private fun PostItem(title: String, icon: ImageVector, triggerFunc: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { //TO-DO Make them interactive
            triggerFunc.invoke()
        }
    ) {
        Icon(
            icon,
            contentDescription = "",
            tint = Color.DarkGray,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = title, color = Color.DarkGray,
            style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold)
        )
    }
}
@Composable
fun postLikesReactions(
    modifier: Modifier = Modifier,
    icons: List<ImageVector>,
    feedPost: FeedPost
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        Row()
        {
            Image(
                Icons.Filled.ThumbUp,
                contentDescription = "",
                modifier = Modifier.size(20.dp),
            )
            Text(
                text = feedPost.likedUsers.size.toString(), color = Color.DarkGray,
                modifier = Modifier.padding(start = 8.dp),
                style = TextStyle(
                    fontSize = 10.sp, fontWeight = FontWeight.Bold
                )
            )
        }
        Row()
        {
            Image(
                Icons.Filled.ThumbDown,
                contentDescription = "",
                modifier = Modifier.size(20.dp),
            )
            Text(
                text = feedPost.disLikedUsers.size.toString(), color = Color.DarkGray,
                modifier = Modifier.padding(start = 8.dp),
                style = TextStyle(
                    fontSize = 10.sp, fontWeight = FontWeight.Bold
                )
            )
        }
    }
}
fun getLikesOrCommentsString(numberOfLikesOrSharings: Int): String {
    return if (numberOfLikesOrSharings != 1) "s" else ""
}
