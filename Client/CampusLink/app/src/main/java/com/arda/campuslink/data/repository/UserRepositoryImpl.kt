package com.arda.campuslink.data.repository

import android.net.Uri
import android.util.Log
import com.arda.campuslink.data.DUMMYDATA.dummyUserData
import com.arda.campuslink.domain.model.User
import com.arda.campuslink.domain.model.ExtendedUser
import com.arda.campuslink.domain.model.ConnectionRequest
import com.arda.campuslink.domain.model.Notification
import com.arda.campuslink.domain.repository.UserRepository
import com.arda.campuslink.util.DebugTags
import com.arda.mainapp.auth.Resource
import com.arda.mainapp.auth.utils.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

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
    private val connectRef = firebaseFirestore.collection("/connect")
    private val requestRef = firebaseFirestore.collection("/Request")

    override suspend fun getDetailedUserInfo(userId: String): Resource<ExtendedUser> = withContext(
        dispatcher
    ) {

        return@withContext try {
            val user = userRef.document("${userId}").get().await()
            val connections1 = connectRef.whereEqualTo("senderId", userId).get().await().documents
            val connections2 = connectRef.whereEqualTo("receiverId", userId).get().await().documents
            val totalConnections = arrayListOf<User>()
            connections1.forEach {
                val tempId = it.get("receiverId")
                val tempDocumentUser = userRef.document("${tempId}").get().await()
                val tempUser = User(
                    UID = it.id,
                    userName = it.get("userName").toString(),
                    jobTitle = it.get("jobTitle").toString(),
                    avatar = Uri.parse(user.get("avatar").toString())
                )
                totalConnections.add(tempUser)
            }
            connections2.forEach {
                val tempId = it.get("senderId")
                val tempDocumentUser = userRef.document("${tempId}").get().await()
                val tempUser = User(
                    UID = it.id,
                    userName = it.get("userName").toString(),
                    jobTitle = it.get("jobTitle").toString(),
                    avatar = Uri.parse(user.get("avatar").toString())
                )
                totalConnections.add(tempUser)
            }
            val expArray = arrayListOf<String>()
            val skillsArray = arrayListOf<String>()
            expArray.addAll(user.get("experiences") as Collection<String>)
            skillsArray.addAll(user.get("skills") as Collection<String>)
            currentUser =
                ExtendedUser(
                    UID = user.id,
                    userName = user.get("userName").toString(),
                    jobTitle = user.get("jobTitle").toString(),
                    experiences = expArray,
                    skills = skillsArray,
                    education = user.get("education").toString(),
                    avatar = Uri.parse(user.get("avatar").toString()),
                    connections = totalConnections,//TODO DÜZELT
                    profilePublic = user.get("profilePublic").toString().toBoolean()
                )

            Resource.Sucess(currentUser!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure<Exception>(e)
        }
    }

    override suspend fun connectRequestUser(userId: String): Resource<User> = withContext(
        dispatcher
    ) {

        return@withContext try {
            val connectionSendedUser = userRef.document("${userId}").get().await()
            val dbRequest = hashMapOf(
                "senderId" to currentFirebaseUser!!.uid,
                "receiverId" to userId,
            )
            val temp = requestRef.whereEqualTo("senderId", currentFirebaseUser!!.uid)
                .whereEqualTo("senderId", userId).get()
                .await().documents
            val temp2 = requestRef.whereEqualTo("receiverId", currentFirebaseUser!!.uid)
                .whereEqualTo("receiverId", userId).get()
                .await().documents
            if (temp.size == 0 && temp2.size == 0) {
                val request = requestRef.add(dbRequest).await()
            }
            val conUser =
                User(
                    UID = connectionSendedUser.id,
                    userName = connectionSendedUser.get("userName").toString(),
                    jobTitle = connectionSendedUser.get("jobTitle").toString(),
                    avatar = Uri.parse(connectionSendedUser.get("avatar").toString())
                )

            Resource.Sucess(conUser!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure<Exception>(e)
        }
    }

    override suspend fun acceptConnectionRequest(notification: Notification): Resource<String> =
        withContext(
            dispatcher
        ) {
            return@withContext try {
                val request = requestRef.whereEqualTo("receiverId", currentFirebaseUser!!.uid)
                    .whereEqualTo("senderId", notification.sender.UID).get()
                    .await().documents.first()
                val dbConnect = hashMapOf(
                    "senderId" to notification.sender.UID,
                    "receiverId" to currentFirebaseUser!!.uid,
                )
                val connection = connectRef.add(dbConnect)
                requestRef.document(request.id).delete()
                Resource.Sucess(notification.sender.UID!!)
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Failure<Exception>(e)
            }
        }

    override suspend fun declineConnectionRequest(userId: String): Resource<String> = withContext(
        dispatcher
    ) {
        return@withContext try {
            var request = requestRef.whereEqualTo("receiverId", userId)
                .whereEqualTo("senderId", currentFirebaseUser!!.uid).get().await().documents
            if (request.size == 0) {
                request = requestRef.whereEqualTo("receiverId", currentFirebaseUser!!.uid)
                    .whereEqualTo("senderId", userId).get().await().documents
            }
            requestRef.document(request.first().id).delete()
            Resource.Sucess(userId)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure<Exception>(e)
        }
    }

    override suspend fun getUserConnections(userId: String): Resource<ArrayList<User>> {
        TODO("Not yet implemented")
    }

    override suspend fun getNotifications(userId: String): Resource<ArrayList<Notification>> =
        withContext(
            dispatcher
        ) {
            return@withContext try {
                val requests = requestRef.whereEqualTo("receiverId", userId).get().await().documents
                val connectionRequests = arrayListOf<Notification>()
// şuan sadece conectionrequestler var e
                requests.forEach { x ->
                    val sender = userRef.document("${x.get("senderId")}").get().await()
                    val senderUser =
                        User(
                            UID = sender.id,
                            userName = sender.get("userName").toString(),
                            jobTitle = sender.get("jobTitle").toString(),
                            avatar = Uri.parse(sender.get("avatar").toString())
                        )
                    var title = "Connection Request"
                    var description = "${senderUser.userName} sent you a connection request"
                    val temp = Notification(
                        notificationId = x.id,
                        sender = senderUser,
                        title = title,
                        description = description,
                    )
                    connectionRequests.add(temp)
                }
                Resource.Sucess(connectionRequests)
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Failure<Exception>(e)
            }
        }

    override suspend fun switchProfileVisibility(extendedUser: ExtendedUser): Resource<ExtendedUser> =
        withContext(
            dispatcher
        )
        {
            return@withContext try {
                currentUser = extendedUser
                currentUser!!.profilePublic = !currentUser!!.profilePublic
                userRef.document("${extendedUser.UID}")
                    .update("profilePublic", currentUser!!.profilePublic)
                Log.v(DebugTags.DataTag.tag, "Profile set to ${currentUser!!.profilePublic}")
                Resource.Sucess(currentUser!!)
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Failure<Exception>(e)
            }
        }

    override suspend fun updateProfile(updatedUserProfile: ExtendedUser): Resource<ExtendedUser> =
        withContext(
            dispatcher
        ) {
            var profileUpdates: UserProfileChangeRequest
            profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(updatedUserProfile.userName)
                .setPhotoUri(updatedUserProfile.avatar)
                .build()

            return@withContext try {
                val updateUser = hashMapOf(
                    "userName" to updatedUserProfile.userName,
                    "skills" to updatedUserProfile.skills,
                    "profilePublic" to updatedUserProfile.profilePublic,
                    "jobTitle" to updatedUserProfile.jobTitle,
                    "experiences" to updatedUserProfile.experiences,
                    "education" to updatedUserProfile.education,
                    "avatar" to updatedUserProfile.avatar,
                )
                val foundUser = userRef.document(updatedUserProfile.UID).update(
                    updateUser
                ).await()

                currentFirebaseUser!!.updateProfile(profileUpdates).await()
                Resource.Sucess(updatedUserProfile!!)
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Failure<Exception>(e)
            }
        }
}