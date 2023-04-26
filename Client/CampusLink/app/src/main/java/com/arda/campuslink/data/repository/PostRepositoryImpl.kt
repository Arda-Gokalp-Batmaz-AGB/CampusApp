package com.arda.campuslink.data.repository

import com.arda.campuslink.data.DUMMY_FEED_DATA
import com.arda.campuslink.domain.model.FeedPost
import com.arda.campuslink.domain.model.NewPost
import com.arda.campuslink.domain.repository.AuthRepository
import com.arda.campuslink.domain.repository.PostRepository
import com.arda.mainapp.auth.Resource
import com.arda.mainapp.auth.utils.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepositoryImpl @Inject constructor(
    private val firebaseFunctions: FirebaseFunctions,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : PostRepository {
    override suspend fun createPost(newPost: NewPost): Resource<FeedPost> =
        withContext(
            dispatcher
        )
        {
            return@withContext try {
                //val result = auth.signInWithEmailAndPassword(email, password).await()
                    //Return edilen postu FeedPost Yap öyle al
                Resource.Sucess(DUMMY_FEED_DATA[2]!!)
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

    override suspend fun getUserPosts(userID: String): Resource<Array<FeedPost>> {
        TODO("Not yet implemented")
    }

    override suspend fun getRecommendedPosts(userID: String): Resource<Array<FeedPost>> {
        TODO("Not yet implemented")
    }
}
