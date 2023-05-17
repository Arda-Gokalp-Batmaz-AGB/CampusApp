package com.arda.campuslink.ui.screens.commentscreen

import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arda.campuslink.domain.model.Comment
import com.arda.campuslink.domain.model.FeedPost
import com.arda.campuslink.domain.usecase.*
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
    private val createCommentUseCase: CreateCommentUseCase
) : ViewModel(), LifecycleObserver {
    private val _uiState = MutableStateFlow(CommentUiState())
    val uiState: StateFlow<CommentUiState> = _uiState.asStateFlow()

    init {
    }

    fun createNewComment() = viewModelScope.launch {
        _uiState.update {
            it.copy(commentCreateFlow = Resource.Loading)
        }
        val result =
            createCommentUseCase.createNewComment(
                _uiState.value.description,
                _uiState.value.focusedComponentId,
                _uiState.value.currentPost
            )
        _uiState.update {
            it.copy(commentCreateFlow = result)
        }
    }

    fun fetchNewComments() = viewModelScope.launch {
        _uiState.update {
            it.copy(feedFlow = Resource.Loading)
        }
        val result =
            userCommentUseCase.fetchNewComments(_uiState.value.currentPost!!)
        _uiState.update {
            it.copy(feedFlow = result)
        }
    }

    fun updateCurrentFeed(newFeed: ArrayList<Comment>) {
        _uiState.update {
            val temp = arrayListOf<Comment>()
            temp.addAll(it.currentFeed.keys)
            it.currentFeed.values.forEach { x ->
                temp.addAll(x)
            }
            temp.addAll(newFeed)
            temp.sortBy { x -> x.timestamp }

            val combinedComments = combineCommentAndChildren(temp)
            it.copy(currentFeed = combinedComments)
        }
        Log.v(DebugTags.UITag.tag, "Current Comment feed = ${_uiState.value.currentFeed}")
    }

    private fun combineCommentAndChildren(commentFeed: ArrayList<Comment>): HashMap<Comment, ArrayList<Comment>> {
        var uniqueCommentFeed = commentFeed.distinctBy { x -> x.commentId }
        val commentBlocks = hashMapOf<Comment, ArrayList<Comment>>()
        uniqueCommentFeed.forEach {
            if (it.parentCommentId == "") {
                if (!commentBlocks.containsKey(it)) {
                    commentBlocks.put(it, arrayListOf<Comment>())
                }
            }
        }

        uniqueCommentFeed.forEach {
            if (it.parentCommentId != "") {
                val parentComment =
                    commentBlocks.keys.first { x -> x.commentId == it.parentCommentId }
                val currentChilds = commentBlocks[parentComment]
                currentChilds!!.add(it)
                commentBlocks.put(parentComment, currentChilds)
            }
        }
        return commentBlocks
    }

    fun updateDescription(textValue: String) {
        _uiState.update {
            it.copy(description = textValue)
        }
        Log.v(DebugTags.UITag.tag, "Current Description = ${_uiState.value.description}")
    }

    fun updateFocusedComponent(componentID: String) {
        _uiState.update {
            it.copy(focusedComponentId = componentID)
        }
        Log.v(DebugTags.UITag.tag, "Current Component ID = ${_uiState.value.focusedComponentId}")
    }

    fun interactWithComment(comment: Comment, type: String) = viewModelScope.launch {
        if (type == "like") {

        } else if (type == "dislike") {

        }
    }

    fun setPost(feedPost: FeedPost) {
        _uiState.update {
            it.copy(currentPost = feedPost)
        }
    }



    fun refreshCurrentFeed()
    {
        resetFeed()
        fetchNewComments()
        _uiState.update {
            it.copy(isFeedRefreshing = false)
        }
        Log.v(DebugTags.UITag.tag,"Feed Refreshed")

    }
    private fun resetFeed()
    {
        _uiState.update {
            val temp = hashMapOf<Comment,ArrayList<Comment>>()
            it.copy(currentFeed = temp, isFeedRefreshing = true)
        }
    }
    fun getNewlyAddedCommentsByUser()
    {
        val posts = userCommentUseCase.getNewlyAddedCommentsByUser()
        if(!posts.isEmpty())
        {
            Log.v(DebugTags.UITag.tag,"Newly added posts added to the feed")

            updateCurrentFeed(posts)
        }
    }
}