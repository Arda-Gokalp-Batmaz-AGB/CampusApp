package com.arda.campuslink.domain.model

data class Notification(
    val notificationId : String,
    val sender: User,
    val title: String,
    val description: String,
    val projectOrClub: Group? = null,// bu bir connection requesti diyebilriim
)
