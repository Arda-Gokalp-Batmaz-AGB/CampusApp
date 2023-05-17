package com.arda.campuslink.ui.screens.commentscreen

import android.graphics.Bitmap
import com.arda.campuslink.domain.model.Comment
import com.arda.campuslink.domain.model.FeedPost
import com.arda.campuslink.domain.model.User
import com.arda.mainapp.auth.Resource

data class CommentUiState(
    val isFeedRefreshing : Boolean = false,
    val feedFlow: Resource<ArrayList<Comment>>? = null,
    val currentFeed: HashMap<Comment,ArrayList<Comment>> = hashMapOf<Comment,ArrayList<Comment>>(),
    val commentCreateFlow: Resource<Comment>? = null,
    val currentMinimizedUser: User? = null,
    val description : String = "",
    val focusedComponentId: String = "",
    val currentPost: FeedPost? = null,
)
