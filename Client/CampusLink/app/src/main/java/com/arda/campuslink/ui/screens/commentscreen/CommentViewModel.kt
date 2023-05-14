package com.arda.campuslink.ui.screens.commentscreen

import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arda.campuslink.domain.model.Comment
import com.arda.campuslink.domain.model.FeedPost
import com.arda.campuslink.domain.usecase.LoggedUserUseCase
import com.arda.campuslink.domain.usecase.UserCommentUseCase
import com.arda.campuslink.domain.usecase.UserPostFeedUseCase
import com.arda.campuslink.ui.screens.homescreen.HomeUiState
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
class CommentViewModel @Inject constructor(
    private val loggedUserUseCase: LoggedUserUseCase,
    private val userCommentUseCase: UserCommentUseCase,
) : ViewModel(), LifecycleObserver {
    private val _uiState = MutableStateFlow(CommentUiState())
    val uiState: StateFlow<CommentUiState> = _uiState.asStateFlow()

    init {
    }

    fun fetchNewComments(post: FeedPost) = viewModelScope.launch {
        _uiState.update {
            it.copy(feedFlow = Resource.Loading)
        }
        val result =
            userCommentUseCase.fetchNewComments(post)
        _uiState.update {
            it.copy(feedFlow = result)
        }
    }
    fun updateCurrentFeed(newFeed : ArrayList<Comment>)
    {
        _uiState.update {
            val temp = arrayListOf<Comment>()
            temp.addAll(it.currentFeed)
            temp.addAll(newFeed)
            temp.sortBy { x -> x.timestamp }
            it.copy(currentFeed = temp)
        }
        Log.v(DebugTags.UITag.tag,"Current Comment feed = ${_uiState.value.currentFeed}")
    }
}