package com.arda.campuslink.domain.usecase

import android.graphics.Bitmap
import android.net.Uri
import com.arda.campuslink.domain.model.NewPost
import com.arda.campuslink.domain.model.User
import com.arda.campuslink.domain.repository.AuthRepository
import javax.inject.Inject

class CreatePostUseCase @Inject constructor(
   private val loggedUserUseCase: LoggedUserUseCase
)
{
    fun createNewPost(description : String,image : Bitmap)
    {
        val post = NewPost(
            user = loggedUserUseCase.getMinProfileOfCurrentUser(),
            description = description,
            image = image,
            hashTags = findTags()
        )

        return
    }

    private fun findTags() : Array<String>
    {
        return arrayOf()
    }
}