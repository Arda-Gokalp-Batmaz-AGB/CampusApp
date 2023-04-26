package com.arda.campuslink.data.repository

import com.arda.campuslink.domain.model.FeedPost
import com.arda.campuslink.domain.model.NewPost
import com.arda.campuslink.domain.repository.AuthRepository
import com.arda.campuslink.domain.repository.PostRepository
import com.arda.mainapp.auth.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepositoryImpl @Inject constructor(
    private val firebaseFunctions: FirebaseFunctions,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : PostRepository {
    override suspend fun createPost(newPost: NewPost): Resource<NewPost> {
        TODO("Not yet implemented")
    }

    override suspend fun interactPost(post: FeedPost): Resource<FeedPost> {
        TODO("Not yet implemented")
    }

    override suspend fun removePost(post: FeedPost): Resource<FeedPost> {
        TODO("Not yet implemented")
    }
}
