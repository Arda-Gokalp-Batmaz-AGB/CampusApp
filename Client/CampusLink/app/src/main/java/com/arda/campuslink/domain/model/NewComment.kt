package com.arda.campuslink.domain.model

data class NewComment(
    val postID : String,
    val parentID : String = "",
    val user: User,
    val description: String,
    val timestamp: Long
)
