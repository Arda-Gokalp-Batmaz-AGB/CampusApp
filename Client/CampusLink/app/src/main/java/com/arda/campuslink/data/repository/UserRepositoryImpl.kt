package com.arda.campuslink.data.repository

import com.arda.campuslink.R
import com.arda.campuslink.data.DUMMYDATA.dummyUserData
import com.arda.campuslink.domain.model.User
import com.arda.campuslink.domain.model.ExtendedUser
import com.arda.campuslink.domain.repository.UserRepository
import com.arda.campuslink.util.ImageProcessUtils
import com.arda.mainapp.auth.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
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
                    UID = "qwe",
                    userName = "Batmaz Arda Gokalp1",
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
                            UID = "ASD",
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