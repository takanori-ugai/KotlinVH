package com.fujitsu.labs.virtualhome

import kotlinx.serialization.*

@Serializable
data class Edge(
    val from_id: Int,
    val to_id: Int,
    val relation_type: String
)

@Serializable
class Graph {
    val nodes: MutableList<Node> = mutableListOf()
    val edges: MutableList<Edge> = mutableListOf()
}
