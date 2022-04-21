package com.fujitsu.labs.virtualhome

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Edge(
    @SerialName("from_id")
    val fromId: Int,
    @SerialName("to_id")
    val toId: Int,
    @SerialName("relation_type")
    val relationType: String
)

@Serializable
data class Node(
    val id: Int? = null,
    val category: String? = null,
    val class_name: String? = null,
    val prefab_name: String? = null,
    val obj_transform: Map<String, List<Float>>? = null,
    val bounding_box: Map<String, List<Float>>? = null,
    val properties: List<String>? = null,
    val states: List<String>? = null
)

@Serializable
data class Graph(
    val nodes: MutableList<Node> = mutableListOf(),
    val edges: MutableList<Edge> = mutableListOf()
)
