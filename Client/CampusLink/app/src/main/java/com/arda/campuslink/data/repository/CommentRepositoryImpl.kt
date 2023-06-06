package com.arda.campuslink.data.repository

import android.net.Uri
import android.util.Log
import com.arda.campuslink.data.DUMMYDATA
import com.arda.campuslink.domain.model.Comment
import com.arda.campuslink.domain.model.FeedPost
import com.arda.campuslink.domain.model.NewComment
import com.arda.campuslink.domain.model.User
import com.arda.campuslink.domain.repository.CommentRepository
import com.arda.campuslink.domain.repository.PostRepository
import com.arda.campuslink.util.DebugTags
import com.arda.mainapp.auth.Resource
import com.arda.mainapp.auth.utils.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseFunctions: FirebaseFunctions,
    private val auth: FirebaseAuth,

    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : CommentRepository {
    val userCommentFeed: ArrayList<Comment> = arrayListOf()
    private val commentRef = firebaseFirestore.collection("/Comment")
    private val userRef = firebaseFirestore.collection("/User")

    override suspend fun createComment(newComment: NewComment): Resource<Comment> =
        withContext(
            dispatcher
        ) {
            return@withContext try {
                val dbComment = hashMapOf(
                    "postID" to newComment.postID,
                    "userID" to newComment.user.UID,
                    "description" to newComment.description,
                    "timestamp" to newComment.timestamp,
                    "likes" to listOf<String>(),
                    "dislikes" to listOf<String>(),
                    "parentCommentId" to newComment.parentID,
                )
                val result = commentRef.add(dbComment).await()
                val comment = Comment(
                    commentId = result.id,
                    postId = newComment.postID,
                    parentCommentId = newComment.parentID,
                    user = newComment.user,
                    description = newComment.description,
                    likedUsers = arrayListOf<String>(),
                    disLikedUsers = arrayListOf<String>(),
                    timestamp = newComment.timestamp
                )
                userCommentFeed.add(comment)
                Resource.Sucess(comment)
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Failure<Exception>(e)
            }
        }

    override suspend fun likeComment(comment: Comment): Resource<Comment> =
        withContext(
            dispatcher
        ) {
            return@withContext try {
                commentRef.document(comment.commentId)
                    .update("likes", FieldValue.arrayUnion(auth.currentUser!!.uid)).await()
                commentRef.document(comment.commentId)
                    .update("dislikes", FieldValue.arrayRemove(auth.currentUser!!.uid)).await()

                Resource.Sucess(comment)
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Failure<Exception>(e)
            }
        }

    override suspend fun disLikeComment(comment: Comment): Resource<Comment> =
        withContext(
            dispatcher
        ) {
            return@withContext try {
                commentRef.document(comment.commentId)
                    .update("dislikes", FieldValue.arrayUnion(auth.currentUser!!.uid)).await()
                commentRef.document(comment.commentId)
                    .update("likes", FieldValue.arrayRemove(auth.currentUser!!.uid)).await()
                Resource.Sucess(comment)
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Failure<Exception>(e)
            }
        }

    override suspend fun resetLikeDislikeComment(comment: Comment): Resource<Comment> =
        withContext(
            dispatcher
        ) {
            return@withContext try {
                commentRef.document(comment.commentId)
                    .update("dislikes", FieldValue.arrayRemove(auth.currentUser!!.uid)).await()
                commentRef.document(comment.commentId)
                    .update("likes", FieldValue.arrayRemove(auth.currentUser!!.uid)).await()
                Resource.Sucess(comment)
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Failure<Exception>(e)
            }
        }

    override suspend fun removeComment(commentID: String): Resource<Comment> {
        TODO("Not yet implemented")
    }

    override suspend fun getPostComments(post: FeedPost): Resource<ArrayList<Comment>> =
        withContext(
            dispatcher
        ) {
            return@withContext try {
                val documentResults =
                    commentRef.whereEqualTo("postID", post.postId).get().await().documents
                val comments = arrayListOf<Comment>()
                documentResults.forEach { x ->
                    val authorUser = userRef.document("${x.get("userID").toString()}").get().await()
                    val user = User(
                        UID = authorUser.id,
                        userName = authorUser.get("userName").toString(),
                        jobTitle = authorUser.get("jobTitle").toString(),
                        avatar = Uri.parse(authorUser.get("avatar").toString()),
                    )

                    val likesArray = arrayListOf<String>()
                    val disLikesArray = arrayListOf<String>()
                    likesArray.addAll(x.get("likes") as Collection<String>)
                    disLikesArray.addAll(x.get("dislikes") as Collection<String>)
                    val current = Comment(
                        postId = x.get("postID").toString(),
                        parentCommentId = x.get("parentCommentId").toString(),
                        commentId = x.id,
                        user = user,
                        description = x.get("description").toString(),
                        likedUsers = likesArray,
                        disLikedUsers = disLikesArray,
                        timestamp = x.get("timestamp").toString().toLong()
                    )
                    Log.v(DebugTags.DataTag.tag,"${likesArray}")
                    comments.add(current)
                }

                Resource.Sucess(comments!!)
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Failure<Exception>(e)
            }
        }

    override fun getNewlyAddedCommentsByUser(): ArrayList<Comment> {
        val comments = arrayListOf<Comment>()
        comments.addAll(userCommentFeed)
        userCommentFeed.clear()
        return comments
    }
}