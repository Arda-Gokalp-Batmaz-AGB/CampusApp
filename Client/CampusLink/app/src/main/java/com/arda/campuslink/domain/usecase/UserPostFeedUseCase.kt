package com.arda.campuslink.domain.usecase

import com.arda.campuslink.domain.model.FeedPost
import com.arda.campuslink.domain.repository.PostRepository
import com.arda.mainapp.auth.Resource
import javax.inject.Inject

class UserPostFeedUseCase @Inject constructor(
    private val loggedUserUseCase: LoggedUserUseCase,
    private val postRepository: PostRepository,
) {

    fun addPostToFeed(post: FeedPost)
    {
        //TO-DO Add newly added post to post feed
    }
    suspend fun fetchNewPostsToFeed(userId : String): Resource<ArrayList<FeedPost>> {
        return postRepository.getRecommendedPosts(userId)
    }
    fun getNewlyAddedPostsByUser(): ArrayList<FeedPost> {
        return postRepository.getNewlyAddedPostsByUser()
    }
    fun getFeedPosts()
    {

    }
}