package com.arda.campuslink.domain.model

import android.net.Uri

data class ExtendedUser(
    val UID : String,
    val userName: String,
    val jobTitle: String,
    var avatar: Uri?,
    val connections: Array<User>,
    var profilePublic : Boolean = true,
)