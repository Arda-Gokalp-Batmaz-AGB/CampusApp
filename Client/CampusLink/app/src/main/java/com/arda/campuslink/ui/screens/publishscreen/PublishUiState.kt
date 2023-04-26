package com.arda.campuslink.ui.screens.publishscreen

import android.graphics.Bitmap
import com.arda.campuslink.domain.model.FeedPost
import com.arda.campuslink.domain.model.User
import com.arda.mainapp.auth.Resource

data class PublishUiState(
    val postCreateFlow: Resource<FeedPost>? = null,
    val currentMinimizedUser: User? = null,
    val description : String = "",
    val image : Bitmap? = null,
)
