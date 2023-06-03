package com.arda.campuslink.ui.screens.publishscreen

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arda.campuslink.domain.model.FeedPost
import com.arda.campuslink.domain.usecase.CreatePostUseCase
import com.arda.campuslink.domain.usecase.LoggedUserUseCase
import com.arda.campuslink.domain.usecase.UserPostFeedUseCase
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
class PublishViewModel @Inject constructor(
    private val createPostUseCase: CreatePostUseCase,
    private val loggedUserUseCase: LoggedUserUseCase,
    private val userPostFeedUseCase: UserPostFeedUseCase
) : ViewModel(), LifecycleObserver {
    private val _uiState = MutableStateFlow(PublishUiState())
    val uiState: StateFlow<PublishUiState> = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(currentMinimizedUser = loggedUserUseCase.getMinProfileOfCurrentUser())
        }
    }

    fun createPost() = viewModelScope.launch {
        _uiState.update {
            it.copy(postCreateFlow = Resource.Loading)
        }
        val result =
            createPostUseCase.createNewPost(_uiState.value.description, _uiState.value.image)
        _uiState.update {
            it.copy(postCreateFlow = result)
        }
    }

    fun addPostToFeed(post: FeedPost)
    {
        userPostFeedUseCase.addPostToFeed(post)
    }
    fun updateDescription(textValue: String) {
        _uiState.update {
            it.copy(description = textValue)
        }
        Log.v(DebugTags.UITag.tag, "Current Description = ${_uiState.value.description}")
    }

    fun updateImage(image: Bitmap) {
        _uiState.update {
            it.copy(image = image)
        }
    }
}
