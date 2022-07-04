package com.fujitsu.labs.virtualhome

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VirtualHomeResponse(
    val id: Int,
    val success: Boolean,
    val message: String?,
    val value: Int,
    @SerialName("message_list")
    val messageList: List<String>?
)
