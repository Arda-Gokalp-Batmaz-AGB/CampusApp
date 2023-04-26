package com.arda.campuslink.domain.model

data class NewPost(
    val user: User,
    val description: String,
    val image: Int?,
)
