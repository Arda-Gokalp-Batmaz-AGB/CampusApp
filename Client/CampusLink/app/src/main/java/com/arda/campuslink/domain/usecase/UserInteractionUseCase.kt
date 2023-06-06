package com.arda.campuslink.domain.usecase

import com.arda.campuslink.domain.model.Comment
import com.arda.campuslink.domain.model.FeedPost
import com.arda.campuslink.domain.repository.CommentRepository
import com.arda.campuslink.domain.repository.PostRepository
import com.arda.mainapp.auth.Resource
import javax.inject.Inject

class UserInteractionUseCase @Inject constructor(
    private val loggedUserUseCase: LoggedUserUseCase,
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
) {
    suspend fun likeComment(comment : Comment): Resource<Comment> {
        return commentRepository.likeComment(comment = comment)
    }
    suspend fun disLikeComment(comment : Comment): Resource<Comment> {
        return commentRepository.disLikeComment(comment = comment)

    }
    suspend fun likePost(post : FeedPost): Resource<FeedPost> {
        return postRepository.likePost(post = post)
    }
    suspend fun disLikePost(post : FeedPost): Resource<FeedPost> {
        return postRepository.disLikePost(post = post)

    }
    suspend fun resetPostInteraction(post : FeedPost): Resource<FeedPost> {
        return postRepository.resetLikeDislikePost(post = post)
    }
    suspend fun resetCommentInteraction(comment : Comment) : Resource<Comment>
    {
        return commentRepository.resetLikeDislikeComment(comment = comment)
    }
}