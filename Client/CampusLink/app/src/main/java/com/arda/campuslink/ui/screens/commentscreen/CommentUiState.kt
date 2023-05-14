package com.arda.campuslink.ui.screens.commentscreen

import com.arda.campuslink.domain.model.Comment
import com.arda.campuslink.domain.model.FeedPost
import com.arda.mainapp.auth.Resource

data class CommentUiState(
    val isFeedRefreshing : Boolean = false,
    val feedFlow: Resource<ArrayList<Comment>>? = null,
    val currentFeed: HashMap<Comment,ArrayList<Comment>> = hashMapOf<Comment,ArrayList<Comment>>(),
)
