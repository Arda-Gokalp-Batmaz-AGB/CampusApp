package com.arda.campuslink.data.repository

import android.net.Uri
import android.util.Log
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import com.arda.campuslink.domain.model.FeedPost
import com.arda.campuslink.domain.model.Notification
import com.arda.campuslink.domain.model.SearchResult
import com.arda.campuslink.domain.model.User
import com.arda.campuslink.domain.repository.AuthRepository
import com.arda.campuslink.domain.repository.SearchRepository
import com.arda.campuslink.domain.repository.UserRepository
import com.arda.campuslink.util.DebugTags
import com.arda.mainapp.auth.Resource
import com.arda.mainapp.auth.utils.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val userRepositoryImpl: UserRepository,
    private val postRepositoryImpl: UserRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : SearchRepository {
    override val currentUser: FirebaseUser?
        get() = auth.currentUser
    private val userRef = firebaseFirestore.collection("/User")
    private val postRef = firebaseFirestore.collection("/Post")
    override suspend fun runSearch(parameter: String): Resource<SearchResult> =
        withContext(
            dispatcher
        ) {

            return@withContext try {
                val users = getUsers(parameter)
                val posts = getPosts(parameter)
                val searchResult = SearchResult(
                    posts = posts,
                    users = users
                )
                Resource.Sucess(searchResult!!)
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Failure<Exception>(e)
            }
        }

    override suspend fun getUsers(userName: String): ArrayList<User> =
        withContext(
            dispatcher
        )
        {
            return@withContext try {
                val usersDocs = userRef.whereEqualTo("userName", userName).get().await().documents
                val users = arrayListOf<User>()

                users.forEach { x ->
                    val temp = User(
                        UID = x.UID,
                        userName = x.userName,
                        jobTitle = x.jobTitle,
                        avatar = x.avatar
                    )
                    users.add(temp)
                }
                users
            } catch (e: Exception) {
                e.printStackTrace()
                arrayListOf<User>()
            }
        }

    override suspend fun getPosts(hashtag: String): ArrayList<FeedPost> =
        withContext(
            dispatcher
        )
        {
            return@withContext try {
                val postDocs =
                    postRef.whereArrayContains("hashTags", hashtag).get().await().documents
                val posts = arrayListOf<FeedPost>()

                postDocs.forEach { x ->
                    val userTemp = userRef.document("${x.get("userID")}").get().await()
                    val user =
                        User(
                            UID = userTemp.id,
                            userName = userTemp.get("userName").toString(),
                            jobTitle = userTemp.get("jobTitle").toString(),
                            avatar = Uri.parse(userTemp.get("avatar").toString())
                        )
                    val likesArray = arrayListOf<String>()
                    val disLikesArray = arrayListOf<String>()
                    val hashTagsArray = arrayListOf<String>()
                    likesArray.addAll(x.get("likes") as Collection<String>)
                    disLikesArray.addAll(x.get("dislikes") as Collection<String>)
                    hashTagsArray.addAll(x.get("hashTags") as Collection<String>)
                    val temp = FeedPost(
                        postId = x.id,
                        user = user,
                        description = x.get("description").toString(),
                        image = null,
                        likedUsers = disLikesArray,
                        disLikedUsers = likesArray,
                        comments = 0,
                        shares = 0,
                        views = 0,
                        timestamp = x.get("timestamp").toString().toLong(),
                        hashTags = hashTagsArray,
                        priority = 0
                    )
                    posts.add(temp)
                }
                posts
            } catch (e: Exception) {
                e.printStackTrace()
                arrayListOf<FeedPost>()
            }
        }

}