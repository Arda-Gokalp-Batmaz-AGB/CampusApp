package com.arda.campuslink.domain.usecase

import com.arda.campuslink.domain.model.Comment
import com.arda.campuslink.domain.model.FeedPost
import com.arda.campuslink.domain.repository.CommentRepository
import com.arda.campuslink.domain.repository.PostRepository
import com.arda.mainapp.auth.Resource
import javax.inject.Inject

class UserCommentUseCase @Inject constructor(
    private val loggedUserUseCase: LoggedUserUseCase,
    private val commentRepository: CommentRepository,
) {
    suspend fun fetchNewComments(post: FeedPost): Resource<ArrayList<Comment>> {
        return commentRepository.getPostComments(post)
    }
}
