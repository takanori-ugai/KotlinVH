package com.fujitsu.labs.virtualhome

import kotlinx.serialization.Serializable
import kotlin.random.Random

/**
 * Represents a request to the VirtualHome server.
 *
 * @property id The ID of the request, which defaults to the current system time in milliseconds.
 * @property action The action to perform.
 * @property intParams Optional list of integer parameters.
 * @property stringParams Optional list of string parameters.
 */
@Serializable
data class VirtualHomeRequest(
    val id: Int = Random.nextInt(),
    val action: String,
    val intParams: List<Int>? = null,
    val stringParams: List<String>? = null,
)
