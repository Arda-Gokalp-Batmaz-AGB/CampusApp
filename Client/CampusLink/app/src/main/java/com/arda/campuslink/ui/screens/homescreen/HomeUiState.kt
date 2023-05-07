package com.arda.campuslink.ui.screens.homescreen

import android.graphics.Bitmap
import com.arda.campuslink.domain.model.FeedPost
import com.arda.campuslink.domain.model.User
import com.arda.mainapp.auth.Resource

data class HomeUiState(
    val isFeedRefreshing : Boolean = false,
    val feedFlow: Resource<ArrayList<FeedPost>>? = null,
    val currentFeed: ArrayList<FeedPost> = arrayListOf(),
)
