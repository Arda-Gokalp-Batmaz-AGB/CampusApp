package com.arda.campuslink.ui.screens.leftbarpopup

import com.arda.campuslink.domain.model.FeedPost
import com.arda.campuslink.domain.model.SearchResult
import com.arda.campuslink.domain.model.User
import com.arda.mainapp.auth.Resource
import com.google.firebase.auth.FirebaseUser

data class LeftBarPopUpUiState(
    val currentMinimizedUser: User? = null,
    val currentMinimizedFirebaseUser: FirebaseUser?= null,
    val enteredTextParameter : String = "",
    val users : ArrayList<User> = arrayListOf(),
    val posts : ArrayList<FeedPost> = arrayListOf(),
    val searchFlow: Resource<ArrayList<SearchResult>>? = null,

)
