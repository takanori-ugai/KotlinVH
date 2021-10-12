package com.fujitsu.labs.virtualhome

import kotlinx.serialization.*
import kotlinx.serialization.builtins.*
import kotlinx.serialization.json.*

@Serializable
data class VirtualHomeRequest(
    val id: Int = System.currentTimeMillis().toInt(),
    val action: String,
    val intParams: List<Int>? = null,
    val stringParams: List<String>? = null
)
