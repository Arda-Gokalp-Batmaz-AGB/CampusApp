package com.arda.campuslink.domain.repository

import com.arda.campuslink.domain.model.FeedPost
import com.arda.campuslink.domain.model.NewPost
import com.arda.mainapp.auth.Resource
import com.google.firebase.auth.FirebaseUser

interface PostRepository {
    suspend fun createPost(newPost: NewPost) : Resource<FeedPost>
    suspend fun interactPost(post: FeedPost) : Resource<FeedPost>
    suspend fun removePost(post: FeedPost) : Resource<FeedPost>
    suspend fun getUserPosts(userID : String) : Resource<Array<FeedPost>>
    suspend fun getRecommendedPosts(userID : String) : Resource<Array<FeedPost>>

}