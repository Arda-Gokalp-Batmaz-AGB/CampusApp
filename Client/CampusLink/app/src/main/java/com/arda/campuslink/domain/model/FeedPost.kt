package com.arda.campuslink.domain.model

import java.util.*
import kotlin.collections.ArrayList

data class FeedPost(
    val postId: String,
    val user: User,
    val description: String,
    val image: Int?,
    var likedUsers: ArrayList<String>,
    var disLikedUsers: ArrayList<String>,
    val comments: Int,
    val shares: Int,
    val views: Int,
    val timestamp: Long,
    val hashTags: ArrayList<String>,
    val priority: Int = 1,
) {

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