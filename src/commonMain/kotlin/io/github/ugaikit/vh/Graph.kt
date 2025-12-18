package io.github.ugaikit.vh

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

/**
 * Data class representing an edge in a graph.
 * @property fromId The ID of the node where the edge starts.
 * @property toId The ID of the node where the edge ends.
 * @property relationType The type of relation between the nodes.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
data class Edge(
    @SerialName("from_id")
    val fromId: Int,
    @SerialName("to_id")
    val toId: Int,
    @SerialName("relation_type")
    val relationType: String,
)

/**
 * Data class representing a node in a graph.
 * @property id The ID of the node.
 * @property category The category of the node.
 * @property className The class name of the node.
 * @property prefabName The prefab name of the node.
 * @property objTransform The transformation of the object represented by the node.
 * @property boundingBox The bounding box of the object represented by the node.
 * @property properties The properties of the node.
 * @property states The states of the node.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
data class Node(
    val id: Int? = null,
    val category: String? = null,
    @SerialName("class_name")
    val className: String? = null,
    @SerialName("prefab_name")
    val prefabName: String? = null,
    @SerialName("obj_transform")
    val objTransform: Map<String, List<Float>>? = null,
    @SerialName("bounding_box")
    val boundingBox: Map<String, List<Float>>? = null,
    val properties: List<String>? = null,
    val states: List<String>? = null,
)

/**
 * Data class representing a graph.
 * @property nodes The nodes of the graph.
 * @property edges The edges of the graph.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
data class Graph(
    val nodes: MutableList<Node> = mutableListOf(),
    val edges: MutableList<Edge> = mutableListOf(),
)
