package com.arda.mainapp.auth

import android.util.Log
import com.arda.mainapp.App
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

object GoogleRepository {
    private val token = "371251571405-96253fid7q9hd88l9sjlsk91ar23a490.apps.googleusercontent.com"

    private lateinit var oneTapClient: GoogleSignInClient
    fun initGoogleLoginAuth(){//context: Context
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(token)
            .requestId()
            .build()
        oneTapClient = GoogleSignIn.getClient(App.context, gso)
        Log.v("REPO","Google client INITILASED")
    }
    fun getOneTapClient(): GoogleSignInClient {
        return oneTapClient
    }
}