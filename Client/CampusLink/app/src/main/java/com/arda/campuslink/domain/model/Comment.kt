package com.arda.campuslink.domain.model

import java.util.*

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
{
    fun timeAgo(): String {
        val currentTime = Date().time;
        val timeDiff = currentTime - timestamp;
        if (timeDiff >= (1000 * 60 * 60 * 24)) {
            return "${timeDiff / (1000 * 60 * 60 * 24)}d";
        } else if (timeDiff >= (1000 * 60 * 60)) {
            return "${timeDiff / (1000 * 60 * 60)}h";
        } else if (timeDiff >= (1000 * 60)) {
            return "${timeDiff / (1000 * 60)}m";
        } else if (timeDiff >= 1000) {
            return "${timeDiff / 1000}s";
        }
        return "0s";
    }
}