package com.arda.campuslink.domain.model

data class ConnectionRequest(
    val requestID : String,
    val senderID : String,
    val receiverID : String,
)
