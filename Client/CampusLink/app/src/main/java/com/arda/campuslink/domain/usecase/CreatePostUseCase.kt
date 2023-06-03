package com.arda.campuslink.domain.usecase

import android.graphics.Bitmap
import com.arda.campuslink.domain.model.FeedPost
import com.arda.campuslink.domain.model.NewPost
import com.arda.campuslink.domain.repository.PostRepository
import com.arda.mainapp.auth.Resource
import java.util.regex.Matcher
import java.util.regex.Pattern
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
            hashTags = findTags(description),
            timestamp = System.currentTimeMillis()
        )

        return postRepository.createPost(post)
    }

    private fun findTags(description : String): Array<String> {
        val MY_PATTERN: Pattern = Pattern.compile("#(\\S+)")
        val mat: Matcher = MY_PATTERN.matcher(description)
        val strs = arrayListOf<String>()
        while (mat.find()) {
            strs.add(mat.group(1))
        }
        return strs.toTypedArray()
    }
}