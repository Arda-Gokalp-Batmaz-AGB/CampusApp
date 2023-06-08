package com.arda.campuslink.domain.repository

import com.arda.campuslink.domain.model.Comment
import com.arda.campuslink.domain.model.FeedPost
import com.arda.campuslink.domain.model.NewComment
import com.arda.campuslink.domain.model.NewPost
import com.arda.mainapp.auth.Resource

interface CommentRepository {
    suspend fun createComment(newComment: NewComment) : Resource<Comment>
    suspend fun likeComment(comment: Comment) : Resource<Comment>
    suspend fun disLikeComment(comment: Comment) : Resource<Comment>
    suspend fun resetLikeDislikeComment(comment: Comment) : Resource<Comment>
    suspend fun removeComment(commentID: String) : Resource<String>
    suspend fun getPostComments(post: FeedPost) : Resource<ArrayList<Comment>>
    fun getNewlyAddedCommentsByUser() : ArrayList<Comment>

}