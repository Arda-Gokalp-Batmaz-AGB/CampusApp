package com.arda.campuslink.domain.model

import android.net.Uri

data class ExtendedUser(
    val UID : String,
    val userName: String,
    val jobTitle: String,
    val skills: ArrayList<String> = arrayListOf<String>(),
    val experiences : ArrayList<String> = arrayListOf<String>(),
    val education : String = "",
    var avatar: Uri?,
    val connections: ArrayList<User>,
    var profilePublic : Boolean = true,
)