package com.arda.campuslink.domain.usecase

import android.graphics.Bitmap
import com.arda.campuslink.domain.model.FeedPost
import com.arda.campuslink.domain.model.NewPost
import com.arda.campuslink.domain.repository.PostRepository
import com.arda.mainapp.auth.Resource
import com.google.type.DateTime
import javax.inject.Inject

class CreatePostUseCase @Inject constructor(
    private val loggedUserUseCase: LoggedUserUseCase,
    private val postRepository: PostRepository,

    ) {
    suspend fun createNewPost(description: String, image: Bitmap?): Resource<FeedPost> {
        val post = NewPost(
            user = loggedUserUseCase.getMinProfileOfCurrentUser(),
            description = description,
            image = image,
            hashTags = findTags(),
            timestamp = System.currentTimeMillis()
        )

        return postRepository.createPost(post)
    }

    private fun findTags(): Array<String> {
        return arrayOf()
    }
}