package com.fujitsu.labs.virtualhome

import kotlinx.serialization.Serializable

@Serializable
data class VirtualHomeResponse(
    val id: Int,
    val success: Boolean,
    val message: String?,
    val value: Int,
    val message_list: List<String>?
)
