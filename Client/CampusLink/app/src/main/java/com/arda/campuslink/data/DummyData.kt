package com.arda.campuslink.data

import com.arda.campuslink.R
import com.arda.campuslink.data.model.LinkedinPost
import com.arda.campuslink.data.model.User

val dummyUserData: List<User> = listOf(
    User(
        name = "Geovani Amaral",
        jobTitle = "Desenvolvedor Android",
        avatar = R.drawable.logo,
        banner = R.drawable.logo,
    ),
    User(
        name = "Android Developers",
        jobTitle = "54.714 seguidores",
        avatar = R.drawable.logo,
        banner = R.drawable.logo,
    )
)

val dummyFeedData: List<LinkedinPost> = listOf(
    LinkedinPost(
        user = dummyUserData[1],
        description = "\uD83C\uDF89 #AndroidDevSummit is back, and this year we're coming..",
        image = R.drawable.logo,
        likes = 350,
        comments = 3,
        sharings = 36,
        timestamp = 1665078680
    ),
    LinkedinPost(
        user = dummyUserData[1],
        description = "Guia para modularização de Apps Android.",
        image = null,
        likes = 5,
        comments = 2,
        sharings = 1,
        timestamp = 1665078680
    )
)