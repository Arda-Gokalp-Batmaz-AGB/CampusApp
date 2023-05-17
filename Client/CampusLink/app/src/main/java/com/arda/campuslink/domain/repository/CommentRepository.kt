package com.arda.campuslink.domain.repository

import com.arda.campuslink.domain.model.Comment
import com.arda.campuslink.domain.model.FeedPost
import com.arda.campuslink.domain.model.NewComment
import com.arda.campuslink.domain.model.NewPost
import com.arda.mainapp.auth.Resource

interface CommentRepository {
    suspend fun createComment(newComment: NewComment) : Resource<Comment>
    suspend fun interactComment(comment: Comment) : Resource<Comment>
    suspend fun removeComment(commentID: String) : Resource<Comment>
    suspend fun getPostComments(post: FeedPost) : Resource<ArrayList<Comment>>
    fun getNewlyAddedCommentsByUser() : ArrayList<Comment>

}