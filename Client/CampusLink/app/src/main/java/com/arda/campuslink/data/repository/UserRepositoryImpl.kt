package com.arda.campuslink.data.repository

import com.arda.campuslink.R
import com.arda.campuslink.data.dummyUserData
import com.arda.campuslink.domain.model.User
import com.arda.campuslink.domain.model.ExtendedUser
import com.arda.campuslink.domain.repository.UserRepository
import com.arda.campuslink.util.ImageProcessUtils
import com.arda.mainapp.auth.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : UserRepository {
    override val currentFirebaseUser: FirebaseUser?
        get() = auth.currentUser
    override var currentUser: ExtendedUser? = null
    override suspend fun getDetailedUserInfo(userId : String ): Resource<ExtendedUser> = withContext(
        dispatcher
    ) {

        return@withContext try {
            currentUser =
                ExtendedUser(
                    userName = "Batmaz Arda Gokalp",
                    jobTitle = "Student",
                    avatar = R.drawable.logo,
                    banner = R.drawable.logo,
                    connections = arrayOf<User>(dummyUserData[0],dummyUserData[1]),
                    followers =  arrayOf<User>(dummyUserData[0]),
                )

            Resource.Sucess(currentUser!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure<Exception>(e)
        }
    }


    override suspend fun getMinimizedUserInfo(userId : String ): Resource<User> = withContext(
        dispatcher
    ) {

        return@withContext try {
            val minUser =
                ImageProcessUtils.convertBitmapToUri(ImageProcessUtils.getBitmapFromImage(R.drawable.logo))
                    ?.let {
                        User(
                            userName = "Batmaz Arda Gokalp",
                            jobTitle = "Student",
                            avatar = it,
                        )
                    }

            Resource.Sucess(minUser!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure<Exception>(e)
        }
    }
}