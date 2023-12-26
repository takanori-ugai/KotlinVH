package com.fujitsu.labs.virtualhome

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a response from the VirtualHome server.
 *
 * @property id The ID of the response.
 * @property success Indicates whether the request was successful.
 * @property message The message accompanying the response, if any.
 * @property value The value associated with the response.
 * @property messageList A list of messages associated with the response, if any.
 */
@Serializable
data class VirtualHomeResponse(
    val id: Int,
    val success: Boolean,
    val message: String?,
    val value: Int,
    @SerialName("message_list")
    val messageList: List<String>?,
)
