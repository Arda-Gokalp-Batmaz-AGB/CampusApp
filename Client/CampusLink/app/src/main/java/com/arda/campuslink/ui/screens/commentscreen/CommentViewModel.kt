package com.arda.campuslink.ui.screens.commentscreen

import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arda.campuslink.domain.model.Comment
import com.arda.campuslink.domain.model.FeedPost
import com.arda.campuslink.domain.model.User
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
    private val createCommentUseCase: CreateCommentUseCase,
    private val userInteractionUseCase: UserInteractionUseCase
) : ViewModel(), LifecycleObserver {
    private val _uiState = MutableStateFlow(CommentUiState())
    val uiState: StateFlow<CommentUiState> = _uiState.asStateFlow()

    init {
    }

    fun getAuthenticatedUser(): User {
        return loggedUserUseCase.getMinProfileOfCurrentUser()
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
        var commentToUpdate: Comment? = null
        val currentFeed = hashMapOf<Comment,ArrayList<Comment>>()
        currentFeed.putAll(_uiState.value.currentFeed)
        currentFeed.keys.sortedBy { x -> x.timestamp }
        commentToUpdate = currentFeed.keys.find { it.commentId == comment.commentId }
        if (commentToUpdate == null) {
            currentFeed.values?.let {
                currentFeed.values.forEach { x ->
                    commentToUpdate = x.find { it.commentId == comment.commentId }
                }
            }
        }
        if (type == "like") {
            commentToUpdate?.let {
                if(it.likedUsers.contains(loggedUserUseCase.getMinProfileOfCurrentUser().UID))
                {
                    it.likedUsers.remove(loggedUserUseCase.getMinProfileOfCurrentUser().UID)
                    removeLikeDislike(it)
                }
                else
                {
                    it.disLikedUsers.remove(loggedUserUseCase.getMinProfileOfCurrentUser().UID)
                    it.likedUsers.add(loggedUserUseCase.getMinProfileOfCurrentUser().UID)
                    likeComment(it)
                }
            }

            Log.v(DebugTags.UITag.tag, "Like Count increased")

        } else if (type == "dislike") {
            commentToUpdate?.let {
                if(it.disLikedUsers.contains(loggedUserUseCase.getMinProfileOfCurrentUser().UID))
                {
                    it.disLikedUsers.remove(loggedUserUseCase.getMinProfileOfCurrentUser().UID)
                    removeLikeDislike(it)
                }
                else
                {
                    it.likedUsers.remove(loggedUserUseCase.getMinProfileOfCurrentUser().UID)
                    it.disLikedUsers.add(loggedUserUseCase.getMinProfileOfCurrentUser().UID)
                    disLikeComment(it)

                }
            }
            Log.v(DebugTags.UITag.tag, "DISLike Count increased")

        }
        _uiState.update {
            it.copy(currentFeed = currentFeed)
        }
    }

    fun updateCommentState() {

    }

    fun setPost(feedPost: FeedPost) {
        _uiState.update {
            it.copy(currentPost = feedPost)
        }
    }


    fun refreshCurrentFeed() {
        resetFeed()
        fetchNewComments()
        _uiState.update {
            it.copy(isFeedRefreshing = false)
        }
        Log.v(DebugTags.UITag.tag, "Feed Refreshed")

    }

    fun dismissStates()
    {
        Log.v(DebugTags.UITag.tag, "CommentViewModel States Dismissed")
        _uiState.update {
            CommentUiState()
        }
    }
    private fun resetFeed() {
        _uiState.update {
            val temp = hashMapOf<Comment, ArrayList<Comment>>()
            it.copy(currentFeed = temp, isFeedRefreshing = true)
        }
    }

    fun getNewlyAddedCommentsByUser() {
        val posts = userCommentUseCase.getNewlyAddedCommentsByUser()
        if (!posts.isEmpty()) {
            Log.v(DebugTags.UITag.tag, "Newly added posts added to the feed")

            updateCurrentFeed(posts)
        }
    }
    private fun likeComment(comment: Comment) = viewModelScope.launch {
        val result = userInteractionUseCase.likeComment(comment = comment)
    }
    private fun disLikeComment(comment: Comment) = viewModelScope.launch {
        val result = userInteractionUseCase.disLikeComment(comment = comment)

    }
    private fun removeLikeDislike(comment: Comment) = viewModelScope.launch {
        val result = userInteractionUseCase.resetCommentInteraction(comment = comment)
    }
}