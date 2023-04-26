package com.arda.campuslink.domain.model

import android.graphics.Bitmap
import android.net.Uri

data class User(
    val UID : String,
    val userName: String,
    val jobTitle: String,
    val avatar: Uri?,
)
