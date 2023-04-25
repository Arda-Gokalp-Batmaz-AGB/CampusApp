package com.arda.campuslink.data

import com.arda.campuslink.R
import com.arda.campuslink.domain.model.LinkedinPost
import com.arda.campuslink.domain.model.User

val dummyUserData: List<User> = listOf(
    User(
        name = "Arda Gökalp Batmaz",
        jobTitle = "Öğrenci",
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
    ),
    LinkedinPost(
        user = dummyUserData[1],
        description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
        image = null,
        likes = 5,
        comments = 2,
        sharings = 1,
        timestamp = 1665078680
    )
)