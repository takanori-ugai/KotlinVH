package com.fujitsu.labs.virtualhome

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GraphTest {
    @Test
    fun `serialize Graph with Nodes and Edges to JSON`() {
        val graph =
            Graph(
                nodes =
                    mutableListOf(
                        Node(id = 1, category = "Category1", className = "ClassName1", prefabName = "PrefabName1"),
                        Node(id = 2, category = "Category2", className = "ClassName2", prefabName = "PrefabName2"),
                    ),
                edges =
                    mutableListOf(
                        Edge(fromId = 1, toId = 2, relationType = "Relation1"),
                        Edge(fromId = 2, toId = 1, relationType = "Relation2"),
                    ),
            )
        val json = Json.encodeToString(graph)
        assertEquals(
            """{"nodes":[{"id":1,"category":"Category1","class_name":"ClassName1","prefab_name":"PrefabName1"},
                {"id":2,"category":"Category2","class_name":"ClassName2","prefab_name":"PrefabName2"}],
                "edges":[{"from_id":1,"to_id":2,"relation_type":"Relation1"},
                {"from_id":2,"to_id":1,"relation_type":"Relation2"}]}
            """.replace(" ", "").replace("\n", ""),
            json,
        )
    }

    @Test
    fun `deserialize JSON to Graph with Nodes and Edges`() {
        val json =
            """{"nodes":[{"id":1,"category":"Category1","class_name":"ClassName1","prefab_name":"PrefabName1"},
            {"id":2,"category":"Category2","class_name":"ClassName2","prefab_name":"PrefabName2"}],
            "edges":[{"from_id":1,"to_id":2,"relation_type":"Relation1"},{"from_id":2,"to_id":1,"relation_type":"Relation2"}]}
            """
        val graph = Json.decodeFromString<Graph>(json)
        assertEquals(2, graph.nodes.size)
        assertEquals(2, graph.edges.size)
        assertEquals("Category1", graph.nodes[0].category)
        assertEquals("Relation1", graph.edges[0].relationType)
    }
}
