package com.arda.campuslink.domain.model

data class SearchResult(
    val users : List<User>,
    val posts : List<FeedPost>,
)
