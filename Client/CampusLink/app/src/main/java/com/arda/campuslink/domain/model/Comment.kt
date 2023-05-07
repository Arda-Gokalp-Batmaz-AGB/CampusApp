package com.arda.campuslink.domain.model

data class Comment(
    val postId : String,
    val parentCommentId : String,
    val commentId : String,
    val user: User,
    val description: String,
    var likes: Int,
    var dislikes: Int,
    val timestamp: Long,
    )
