package com.arda.campuslink.domain.usecase

import android.graphics.Bitmap
import android.util.Log
import com.arda.campuslink.domain.model.Comment
import com.arda.campuslink.domain.model.FeedPost
import com.arda.campuslink.domain.model.NewComment
import com.arda.campuslink.domain.model.NewPost
import com.arda.campuslink.domain.repository.CommentRepository
import com.arda.campuslink.domain.repository.PostRepository
import com.arda.campuslink.util.DebugTags
import com.arda.mainapp.auth.Resource
import javax.inject.Inject

class CreateCommentUseCase @Inject constructor(
    private val loggedUserUseCase: LoggedUserUseCase,
    private val commentRepository: CommentRepository,
    ) {
    suspend fun createNewComment(
        description: String,
        focusedComponentId: String,
        currentPost: FeedPost?
    ): Resource<Comment> {
        var parentId = ""
        if(focusedComponentId != currentPost!!.postId)
        {
            parentId = focusedComponentId
            Log.v(DebugTags.DomainTag.tag,"Comment Added to a child comment")
        }
        else
        {
            Log.v(DebugTags.DomainTag.tag,"Comment Added to a parent comment")
        }
        val post = NewComment(
            postID = currentPost!!.postId,
            parentID = parentId,
            user = loggedUserUseCase.getMinProfileOfCurrentUser(),
            description = description,
            timestamp = System.currentTimeMillis()
        )
        return commentRepository.createComment(post)
    }
}