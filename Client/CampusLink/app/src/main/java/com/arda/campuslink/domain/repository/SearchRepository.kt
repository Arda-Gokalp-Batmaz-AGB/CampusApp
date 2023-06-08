package com.arda.campuslink.domain.repository

import com.arda.campuslink.domain.model.FeedPost
import com.arda.campuslink.domain.model.SearchResult
import com.arda.campuslink.domain.model.User
import com.arda.mainapp.auth.Resource
import com.google.firebase.auth.FirebaseUser

interface SearchRepository {
    val currentUser : FirebaseUser?
    suspend fun runSearch(parameter : String) : Resource<SearchResult>
    suspend fun getUsers(userName : String) : ArrayList<User>
    suspend fun getPosts(hashtag : String) :ArrayList<FeedPost>
}