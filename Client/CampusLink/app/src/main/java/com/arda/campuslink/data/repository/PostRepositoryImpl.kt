package com.arda.campuslink.data.repository

import android.net.Uri
import com.arda.campuslink.domain.model.FeedPost
import com.arda.campuslink.domain.model.NewPost
import com.arda.campuslink.domain.model.User
import com.arda.campuslink.domain.repository.PostRepository
import com.arda.mainapp.auth.Resource
import com.arda.mainapp.auth.utils.await
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class PostRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseFunctions: FirebaseFunctions,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : PostRepository {
    val userPostFeed: ArrayList<FeedPost> = arrayListOf()
    private val postRef = firebaseFirestore.collection("/Post")
    private val userRef = firebaseFirestore.collection("/User")
    override suspend fun createPost(newPost: NewPost): Resource<FeedPost> =
        withContext(
            dispatcher
        )
        {
            return@withContext try {
                val dbPost = hashMapOf(
                    "userID" to newPost.user.UID,
                    "description" to newPost.description,
                    "timestamp" to newPost.timestamp,
                    "hashTags" to newPost.hashTags.toList(),
                    "likes" to listOf<String>(),
                    "dislikes" to listOf<String>(),
                    //image todo
                )
                val result = postRef.add(dbPost).await()
                val feedPost = FeedPost(
                    postId = result.id,
                    user = newPost.user,
                    description = newPost.description,
                    image = null,
                    likedUsers = arrayListOf<String>(),
                    disLikedUsers = arrayListOf<String>(),
                    comments = 0,
                    shares = 0,
                    views = 0,
                    timestamp = newPost.timestamp,
                    hashTags = arrayListOf<String>(),
                    priority = 0
                )
                userPostFeed.add(feedPost)
                Resource.Sucess(feedPost)
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Failure<Exception>(e)
            }
        }

    override suspend fun interactPost(post: FeedPost): Resource<FeedPost> =
        withContext(
            dispatcher
        )
        {
            TODO("Not yet implemented")
        }

    override suspend fun removePost(post: FeedPost): Resource<FeedPost> =
        withContext(
            dispatcher
        )
        {
            TODO("Not yet implemented")
        }

    override suspend fun getUserPosts(userID: String): Resource<Array<FeedPost>> =
        withContext(
            dispatcher
        ) {
            TODO("Not yet implemented")
        }

    override suspend fun getRecommendedPosts(userID: String): Resource<ArrayList<FeedPost>> =
        withContext(
            dispatcher
        ) {
            val documentResults = postRef.get().await().documents
            val posts = arrayListOf<FeedPost>()
            documentResults.forEach { x ->
                val t = x.get("userID").toString()
                val authorUser = userRef.document("${x.get("userID").toString()}").get().await()
                val user = User(
                    UID = authorUser.id,
                    userName = authorUser.get("userName").toString(),
                    jobTitle = authorUser.get("jobTitle").toString(),
                    avatar =  Uri.parse(authorUser.get("avatar").toString()),
                )
                val feedPost = FeedPost(
                    postId = x.id,
                    user = user,
                    description = x.get("description").toString(),
                    image = null,
                    likedUsers = ArrayList(authorUser.get("likes").toString().split(",")),
                    disLikedUsers =  ArrayList(authorUser.get("dislikes").toString().split(",")),
                    comments = 0,
                    shares = 0,
                    views = 0,
                    timestamp = x.get("timestamp").toString().toLong(),
                    hashTags = ArrayList(authorUser.get("hashTags").toString().split(",")),
                    priority = 1
                )
                posts.add(feedPost)
            }
            return@withContext try {
                Resource.Sucess(posts!!)
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Failure<Exception>(e)
            }
        }

    override fun getNewlyAddedPostsByUser(): ArrayList<FeedPost> {
        val posts = arrayListOf<FeedPost>()
        posts.addAll(userPostFeed)
        userPostFeed.clear()
        return posts
    }
}
