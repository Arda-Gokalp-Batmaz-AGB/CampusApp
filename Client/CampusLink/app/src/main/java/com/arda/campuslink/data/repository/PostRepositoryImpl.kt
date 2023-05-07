package com.arda.campuslink.data.repository

import com.arda.campuslink.R
import com.arda.campuslink.data.DUMMY_FEED_DATA
import com.arda.campuslink.data.dummyUserData
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
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class PostRepositoryImpl @Inject constructor(
    private val firebaseFunctions: FirebaseFunctions,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : PostRepository {

    val userPostFeed : ArrayList<FeedPost> = arrayListOf()
    override suspend fun createPost(newPost: NewPost): Resource<FeedPost> =
        withContext(
            dispatcher
        )
        {
//            val postID: UUID = UUID.randomUUID()
            return@withContext try {
                //val result = auth.signInWithEmailAndPassword(email, password).await()
                    //Return edilen postu FeedPost Yap öyle al
                        // Postu databaseye ekle sonra databaseden dönen değeri feedpost yap geri dönder
                            //hashtag bulma arkaplan
                val feedPost = FeedPost(
                    postId = "qweqwewq134314",
                    user = newPost.user,
                    description = newPost.description,
                    image = null,
                    likes = 0,
                    comments = 0,
                    shares = 0,
                    views = 0,
                    timestamp = newPost.timestamp,
                    hashTags = arrayOf()
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
        ){
        TODO("Not yet implemented")
    }

    override suspend fun getRecommendedPosts(userID: String): Resource<ArrayList<FeedPost>> =
    withContext(
    dispatcher
    ) {
        val posts = arrayListOf<FeedPost>()
        posts.add(DUMMY_FEED_DATA[0])
        posts.add(DUMMY_FEED_DATA[1])
        posts.addAll(userPostFeed)
        userPostFeed.clear()
        return@withContext try {
            Resource.Sucess(posts!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure<Exception>(e)
        }
    }
}
