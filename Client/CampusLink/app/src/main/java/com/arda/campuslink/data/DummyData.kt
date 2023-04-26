package com.arda.campuslink.data

import com.arda.campuslink.R
import com.arda.campuslink.domain.model.FeedPost
import com.arda.campuslink.domain.model.User
import com.arda.campuslink.util.ImageProcessUtils

val dummyUserData: List<User> = listOf(
    User(
        "qwe",
        userName = "Batmaz",
        jobTitle = "Öğrenci",
        avatar = ImageProcessUtils.convertBitmapToUri(ImageProcessUtils.getBitmapFromImage(R.drawable.logo)),
    ),
    User(
        "hahaha",
        userName = "Android Developers",
        jobTitle = "54.714 seguidores",
        avatar =  ImageProcessUtils.convertBitmapToUri(ImageProcessUtils.getBitmapFromImage(R.drawable.logo)),
    )
)

val DUMMY_FEED_DATA: List<FeedPost> = listOf(
    FeedPost(
        postId = "asasdsaddasd",
        user = dummyUserData[1],
        description = "\uD83C\uDF89 #AndroidDevSummit is back, and this year we're coming..",
        image = R.drawable.logo,
        likes = 350,
        comments = 3,
        shares = 36,
        views = 10,
        timestamp = 1665078680,
        hashTags = arrayOf()
    ),
    FeedPost(
        postId = "asdasd",
        user = dummyUserData[1],
        description = "Guia para modularização de Apps Android.",
        image = null,
        likes = 5,
        comments = 2,
        shares = 1,
        views = 10,
        timestamp = 1665078680,
        hashTags = arrayOf("ardasd","he")

    ),
    FeedPost(
        postId = "aasdssdasd",
        user = dummyUserData[1],
        description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
        image = null,
        likes = 5,
        comments = 2,
        shares = 1,
        views = 10,
        timestamp = 1665078680,
        hashTags = arrayOf("ardasd","he")
    )
)