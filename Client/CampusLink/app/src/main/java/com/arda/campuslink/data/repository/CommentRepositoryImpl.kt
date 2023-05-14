package com.arda.campuslink.data.repository

import com.arda.campuslink.data.DUMMYDATA
import com.arda.campuslink.domain.model.Comment
import com.arda.campuslink.domain.model.FeedPost
import com.arda.campuslink.domain.repository.CommentRepository
import com.arda.campuslink.domain.repository.PostRepository
import com.arda.mainapp.auth.Resource
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRepositoryImpl @Inject constructor(
    private val firebaseFunctions: FirebaseFunctions,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : CommentRepository {
    override suspend fun createComment(comment: Comment): Resource<Comment> {
        TODO("Not yet implemented")
    }

    override suspend fun interactComment(comment: Comment): Resource<Comment> {
        TODO("Not yet implemented")
    }

    override suspend fun removeComment(commentID: String): Resource<Comment> {
        TODO("Not yet implemented")
    }

    override suspend fun getPostComments(post: FeedPost): Resource<ArrayList<Comment>> =
        withContext(
            dispatcher
        ) {

            val comments = arrayListOf<Comment>(
                Comment(
                    postId = post.postId,
                    parentCommentId = "sad",
                    commentId = "asd",
                    user=post.user,
                    description = "asdsadasdsdasdas",
                    likes = 5,
                    dislikes = 9,
                    timestamp = System.currentTimeMillis()
                ),
                Comment(
                    postId = post.postId,
                    parentCommentId = "asd",
                    commentId = "",
                    user=post.user,
                    description = "asdsadasdsdasdas",
                    likes = 5,
                    dislikes = 9,
                    timestamp = System.currentTimeMillis()
                ),
                Comment(
                    postId = post.postId,
                    parentCommentId = "sad4",
                    commentId = "",
                    user=post.user,
                    description = "asdsadasdsdasdsadsadsadas",
                    likes = 0,
                    dislikes = 0,
                    timestamp = System.currentTimeMillis()
                )
            )
        return@withContext try {
            Resource.Sucess(comments!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure<Exception>(e)
        }
    }
}