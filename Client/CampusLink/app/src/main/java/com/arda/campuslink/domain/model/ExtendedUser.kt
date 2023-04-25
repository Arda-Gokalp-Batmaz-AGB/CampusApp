package com.arda.campuslink.domain.model

data class ExtendedUser(
    val userName: String,
    val jobTitle: String,
    val avatar: Int,
    val banner: Int,
    val connections: Array<User>,
    val followers: Array<User>,
)