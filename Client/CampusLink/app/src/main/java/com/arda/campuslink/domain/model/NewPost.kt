package com.arda.campuslink.domain.model

import android.graphics.Bitmap

data class NewPost(
    val user: User,
    val description: String,
    val image: Bitmap?,
    val hashTags : Array<String>,
    val timestamp: Long
)
