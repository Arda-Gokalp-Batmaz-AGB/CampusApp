package com.arda.campuslink.data.repository

import android.util.Log
import com.arda.campuslink.domain.repository.AuthRepository
import com.arda.campuslink.util.GoogleOneTapClient
import com.arda.mainapp.auth.Resource
import com.arda.mainapp.auth.utils.await
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : AuthRepository {
    override val currentUser: FirebaseUser?
        get() = auth.currentUser
    private val userRef = firebaseFirestore.collection("/User")

    override suspend fun emailLogin(email: String, password: String): Resource<FirebaseUser> =
        withContext(
            dispatcher
        ) {
            return@withContext try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                Resource.Sucess(result.user!!)
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Failure<Exception>(e)
            }
        }

    override suspend fun emailRegister(email: String, password: String): Resource<FirebaseUser> =
        withContext(
            dispatcher
        ) {
            return@withContext try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                result?.user?.updateProfile(
                    UserProfileChangeRequest.Builder().setDisplayName(email).build()
                )?.await()
                createFireStoreUser(result.user!!)

                Resource.Sucess(result.user!!)
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Failure<Exception>(e)
            }
        }

    override suspend fun googleLogin(task: Task<GoogleSignInAccount>): Resource<FirebaseUser> = withContext(
        dispatcher){
        return@withContext try  {
            val account = task.await()
            Log.d("REPO", "firebaseAuthWithGoogle:" + account.id)
            val credential = GoogleAuthProvider.getCredential( account.idToken,null)
            val result = auth.signInWithCredential(credential).await()
            createFireStoreUser(result.user!!)

            Resource.Sucess(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure<Exception>(e)
        }
    }

    private suspend fun createFireStoreUser(firebaseUser: FirebaseUser)
    {
        val isEmpty = userRef.whereEqualTo("UID",firebaseUser.uid).get().await().isEmpty
        if(isEmpty)
        {
            val dbUser = hashMapOf(
//                "UID" to firebaseUser.uid,
                "userName" to firebaseUser.displayName,
                "avatar" to firebaseUser.photoUrl,
                "education" to "",
                "experiences" to listOf<String>(),
                "jobTitle" to "",
                "skills" to listOf<String>(),
                "profilePublic" to true,
            )
            userRef.document(firebaseUser.uid).set(dbUser).await()
        }
    }
    override fun logout() {
        GoogleOneTapClient.getOneTapClient().signOut()
        auth.signOut()
    }
}


