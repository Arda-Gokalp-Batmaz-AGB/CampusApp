package com.arda.campuslink.data.repository

import android.net.Uri
import android.util.Log
import com.arda.campuslink.R
import com.arda.campuslink.data.DUMMYDATA.dummyUserData
import com.arda.campuslink.domain.model.User
import com.arda.campuslink.domain.model.ExtendedUser
import com.arda.campuslink.domain.repository.UserRepository
import com.arda.campuslink.util.DebugTags
import com.arda.campuslink.util.ImageProcessUtils
import com.arda.mainapp.auth.Resource
import com.arda.mainapp.auth.utils.await
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
    private val userRef = firebaseFirestore.collection("/User")

    override suspend fun getDetailedUserInfo(userId: String): Resource<ExtendedUser> = withContext(
        dispatcher
    ) {

        return@withContext try {
            val user = userRef.document("${userId}").get().await()

            currentUser =
                ExtendedUser(
                    UID = user.id,
                    userName = user.get("userName").toString(),
                    jobTitle = user.get("jobTitle").toString(),
                    avatar = Uri.parse(user.get("avatar").toString()),
                    connections = arrayOf<User>(dummyUserData[0], dummyUserData[1]),//TODO DÜZELT
                    profilePublic = user.get("profilePublic").toString().toBoolean()
                )

            Resource.Sucess(currentUser!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure<Exception>(e)
        }
    }

    override suspend fun switchProfileVisibility(extendedUser: ExtendedUser): Resource<ExtendedUser> = withContext(
        dispatcher
    )
    {
        return@withContext try {
            currentUser = extendedUser
            currentUser!!.profilePublic = !currentUser!!.profilePublic
            userRef.document("${extendedUser.UID}").update("profilePublic",currentUser!!.profilePublic)
            Log.v(DebugTags.DataTag.tag, "Profile set to ${currentUser!!.profilePublic}")
            Resource.Sucess(currentUser!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure<Exception>(e)
        }
    }

}