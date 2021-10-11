package com.fujitsu.labs.virtualhome

import kotlinx.serialization.*

@Serializable
data class Node(
    val id: Int? = null,
    val category: String? = null,
    val class_name: String? = null,
    val prefab_name: String? = null,
    val obj_transform: Map<String, List<Double>>? = null,
    val bounding_box: Map<String, List<Double>>? = null,
    val properties: List<String>? = null,
    val states: List<String>? = null
)
