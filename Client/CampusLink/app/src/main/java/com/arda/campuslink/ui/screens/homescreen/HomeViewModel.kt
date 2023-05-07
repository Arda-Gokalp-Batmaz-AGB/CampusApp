package com.arda.campuslink.ui.screens.homescreen

import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arda.campuslink.domain.model.FeedPost
import com.arda.campuslink.domain.usecase.LoggedUserUseCase
import com.arda.campuslink.domain.usecase.UserPostFeedUseCase
import com.arda.campuslink.ui.screens.publishscreen.PublishUiState
import com.arda.campuslink.util.DebugTags
import com.arda.mainapp.auth.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val loggedUserUseCase: LoggedUserUseCase,
    private val userPostFeedUseCase: UserPostFeedUseCase
) : ViewModel(), LifecycleObserver
{
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        //fetchNewPosts()
    }
    fun fetchNewPosts() = viewModelScope.launch {
        _uiState.update {
            it.copy(feedFlow = Resource.Loading)
        }
        val result = userPostFeedUseCase.fetchNewPostsToFeed(loggedUserUseCase.getMinProfileOfCurrentUser().UID)
        _uiState.update {
            it.copy(feedFlow = result)
        }
    }
    fun updateCurrentFeed(newFeed : ArrayList<FeedPost>)
    {
        _uiState.update {
            val temp = arrayListOf<FeedPost>()
            temp.addAll(it.currentFeed)
            temp.addAll(newFeed)
            temp.sortBy { x -> x.priority }
            it.copy(currentFeed = temp)
        }
        Log.v(DebugTags.UITag.tag,"Current updated feed = ${_uiState.value.currentFeed}")
    }
    fun refreshCurrentFeed()
    {
        resetFeed()
        fetchNewPosts()
        _uiState.update {
            it.copy(isFeedRefreshing = false)
        }
        Log.v(DebugTags.UITag.tag,"Feed Refreshed")

    }
    private fun resetFeed()
    {
        _uiState.update {
            val temp = arrayListOf<FeedPost>()
            it.copy(currentFeed = temp, isFeedRefreshing = true)
        }
    }
    fun getNewlyAddedPostsByUser()
    {
        val posts = userPostFeedUseCase.getNewlyAddedPostsByUser()
        if(!posts.isEmpty())
        {
            Log.v(DebugTags.UITag.tag,"Newly added posts added to the feed")

            updateCurrentFeed(posts)
        }
    }
}