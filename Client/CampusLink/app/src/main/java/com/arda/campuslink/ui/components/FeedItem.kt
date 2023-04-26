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
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.arda.campuslink.domain.model.FeedPost
import kotlinx.coroutines.CoroutineScope
import com.arda.campuslink.R
import com.arda.campuslink.ui.screens.profilescreen.ProfileScreen
import com.arda.campuslink.util.LangStringUtil

@Composable
fun FeedItem(
    feedPost: FeedPost,
    coroutineScope: CoroutineScope,
    navController: NavController
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
            .padding(top = 8.dp).background(color = Color.White)
    ) {
        PostTopItem(
            feedPost,
            modifier = Modifier.layoutId("post_top_bar"),
            coroutineScope,
            navController
        )
        FollowButton(modifier = Modifier.layoutId("post_follow_button"))
        PostTextAndImage(
            feedPost = feedPost,
            modifier = Modifier.layoutId("post_text_and_image")
        )
        PostOptions(feedPost = feedPost, modifier = Modifier.layoutId("post_options"))
    }

}

@Composable
fun PostTopItem(
    feedPost: FeedPost,
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope,
    navController: NavController
) {
    val openProfile = remember { mutableStateOf(false) }
    if (openProfile.value) {
        feedPost?.let { ProfileScreen(openProfile,user = feedPost.user) }
    }
    Row(
        modifier = modifier.padding(top = 8.dp)
            .clickable {
                openProfile.value = true
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Image(
            painter = rememberImagePainter(feedPost.user.avatar),
            contentDescription = "",
            modifier = Modifier.padding(start = 8.dp)
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
        modifier = modifier.padding(end= 8.dp, top = 8.dp)
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
fun PostOptions(modifier: Modifier = Modifier, feedPost: FeedPost) {
    Column(modifier = modifier.padding(4.dp)) {

        LikesReactions(
            modifier = Modifier.padding(top = 8.dp),
            icons = listOf(
                Icons.Filled.ThumbUp,
                Icons.Filled.SentimentSatisfied,
                Icons.Filled.EmojiObjects
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
            PostItem(LangStringUtil.getLangString(R.string.like), Icons.Filled.ThumbUp)
            PostItem(LangStringUtil.getLangString(R.string.comment), Icons.Filled.Comment)
            PostItem(LangStringUtil.getLangString(R.string.Share), Icons.Filled.Share)
            PostItem(LangStringUtil.getLangString(R.string.Send), Icons.Filled.Send)
        }
    }
}

@Composable
private fun PostItem(title: String, icon: ImageVector) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { //TO-DO Make them interactive
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
fun LikesReactions(modifier: Modifier = Modifier, icons: List<ImageVector>, feedPost: FeedPost) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LazyRow {
                items(icons.size) { idx ->
                    Image(
                        icons[idx],
                        contentDescription = "",
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
            Text(
                text = feedPost.likes.toString(), color = Color.DarkGray,
                modifier = Modifier.padding(start = 8.dp),
                style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold
                )
            )
        }
        Row {
            if (feedPost.comments != 0) {
                Text(
                    text = "${feedPost.comments} ${LangStringUtil.getLangString(R.string.comment)}"
                            + getLikesOrCommentsString(
                        feedPost.likes
                    ),
                    color = Color.DarkGray,
                    style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold)
                )
            }
            Text(
                text = " • ", color = Color.DarkGray,
                style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold)
            )
            if (feedPost.sharings != 0) {
                Text(
                    text = "${feedPost.sharings} ${LangStringUtil.getLangString(R.string.Share)}"
                            + getLikesOrCommentsString(
                        feedPost.sharings
                    ), color = Color.DarkGray,
                    style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

fun getLikesOrCommentsString(numberOfLikesOrSharings: Int): String {
    return if (numberOfLikesOrSharings != 1) "s" else ""
}
