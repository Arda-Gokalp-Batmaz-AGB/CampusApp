package com.arda.campuslink.di

import com.arda.campuslink.data.repository.*
import com.arda.campuslink.domain.repository.*
import com.arda.campuslink.domain.usecase.AuthenticationUseCase
import com.arda.campuslink.domain.usecase.LoggedUserUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.functions.FirebaseFunctions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun AuthRepository(
        firebaseFirestore: FirebaseFirestore,
        auth: FirebaseAuth,
    ): AuthRepository = AuthRepositoryImpl(firebaseFirestore,auth)
    @Singleton
    @Provides
    fun UserRepository(
        firebaseFirestore: FirebaseFirestore,
        auth: FirebaseAuth,
    ): UserRepository = UserRepositoryImpl(firebaseFirestore,auth)
    @Singleton
    @Provides
    fun PostRepository(
        firebaseFirestore: FirebaseFirestore,
        firebaseFunctions: FirebaseFunctions,
        auth: FirebaseAuth,
        ): PostRepository = PostRepositoryImpl(firebaseFirestore,firebaseFunctions,auth)
    @Singleton
    @Provides
    fun CommentRepository(
        firebaseFirestore: FirebaseFirestore,
        firebaseFunctions: FirebaseFunctions,
        auth: FirebaseAuth,
        ): CommentRepository = CommentRepositoryImpl(firebaseFirestore,firebaseFunctions,auth)

    @Singleton
    @Provides
    fun SearchRepository(
        firebaseFirestore: FirebaseFirestore,
        firebaseFunctions: FirebaseFunctions,
        auth: FirebaseAuth,
        userRepositoryImpl: UserRepository,
        postRepositoryImpl: UserRepository,
    ): SearchRepository = SearchRepositoryImpl(firebaseFirestore,auth,userRepositoryImpl,postRepositoryImpl)
}