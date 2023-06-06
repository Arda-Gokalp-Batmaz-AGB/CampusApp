package com.arda.campuslink.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.arda.campuslink.R
import com.arda.campuslink.domain.model.Comment
import com.arda.campuslink.ui.screens.commentscreen.CommentViewModel
import com.arda.campuslink.ui.screens.profilescreen.ProfileScreen
import com.arda.campuslink.util.LangStringUtil

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun CommentListItem(
    feedComment: Comment,
    commentViewModel: CommentViewModel,
    isChild: Boolean = false,
) {
    var padding = 0.dp
    if (isChild) {
        padding = 40.dp
    }
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = padding)
    )
    {
        ReplyProfileImage(
            feedComment = feedComment
        )
        Card(
            modifier = Modifier
                .padding(start = 4.dp, end = 10.dp, top = 4.dp, bottom = 4.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 0.dp, vertical = 5.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = feedComment.user.userName,
                        )
                        Text(
                            text = feedComment.user.jobTitle,
                        )
                    }
                }
                Text(
                    text = feedComment.description,
                    maxLines = 2,
                    color = MaterialTheme.colors.primary,
                    overflow = TextOverflow.Ellipsis
                )
                CommentItemOptions(feedComment = feedComment, commentViewModel = commentViewModel)

            }
        }
    }

}

@Composable
fun ReplyProfileImage(
    feedComment: Comment
) {
    val openProfile = remember { mutableStateOf(false) }
    if (openProfile.value) {
        feedComment?.let { ProfileScreen(openProfile, user = feedComment.user) }
    }
    Image(
        painter = rememberImagePainter(feedComment.user.avatar),
        contentDescription = "",
        modifier = Modifier
            .padding(start = 8.dp)
            .size(40.dp)
            .clip(shape = RoundedCornerShape(25.dp))
            .clickable {
                openProfile.value = true
            },
        contentScale = ContentScale.Crop,
    )
}


@Composable
fun CommentItemOptions(
    modifier: Modifier = Modifier,
    feedComment: Comment,
    commentViewModel: CommentViewModel,
) {
    Column(modifier = modifier.padding(4.dp)) {

        CommentLikesReactions(
            modifier = Modifier.padding(top = 8.dp),
            icons = listOf(
                Icons.Filled.ThumbUp,
            ),
            feedComment = feedComment,
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
            //yeni viewmodel lazm
            CommentItem(LangStringUtil.getLangString(R.string.like), Icons.Filled.ThumbUp)
            {
                commentViewModel.interactWithComment(feedComment, "like")
            }
            CommentItem(LangStringUtil.getLangString(R.string.dislike), Icons.Filled.ThumbDown)
            {
                commentViewModel.interactWithComment(feedComment, "dislike")
            }
            CommentItem(LangStringUtil.getLangString(R.string.comment), Icons.Filled.Comment)
            {
                commentViewModel.updateFocusedComponent(feedComment.commentId)
            }
        }
    }
}

@Composable
private fun CommentItem(title: String, icon: ImageVector, triggerFunc: () -> Unit) {
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
fun CommentLikesReactions(
    modifier: Modifier = Modifier,
    icons: List<ImageVector>,
    feedComment: Comment
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
                text = feedComment.likedUsers.size.toString(), color = Color.DarkGray,
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
                text = feedComment.disLikedUsers.size.toString(), color = Color.DarkGray,
                modifier = Modifier.padding(start = 8.dp),
                style = TextStyle(
                    fontSize = 10.sp, fontWeight = FontWeight.Bold
                )
            )
        }
    }
}
