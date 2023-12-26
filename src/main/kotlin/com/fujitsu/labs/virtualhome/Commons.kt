package com.fujitsu.labs.virtualhome

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * Data class representing an action.
 *
 * @property name The name of the action.
 * @property num The number associated with the action.
 * @property properties A list of properties related to the action.
 */
data class Action(
    val name: String,
    val num: Int,
    val properties: List<List<String>> = listOf(listOf()),
)

/**
 * Singleton object that provides common functionality.
 */
object Commons {
    private var objectStatesCache: Map<String, List<String>>? = null
    private var propertiesDataCache: Map<String, List<String>>? = null

    /**
     * Returns a map of object states. If the cache is null, it fetches the data from a JSON file.
     *
     * @return A map of object states.
     */
    fun objectStates(): Map<String, List<String>> {
        if (objectStatesCache == null) {
            objectStatesCache = getJsonFromResource("object_states.json")
        }
        return objectStatesCache!!
    }

    /**
     * Returns a map of properties data. If the cache is null, it fetches the data from a JSON file.
     *
     * @return A map of properties data.
     */
    fun propertiesData(): Map<String, List<String>> {
        if (propertiesDataCache == null) {
            propertiesDataCache = getJsonFromResource("properties_data_all.json")
        }
        return propertiesDataCache!!
    }

    /**
     * Fetches a JSON file from resources and decodes it into a map.
     *
     * @param jsonFileName The name of the JSON file.
     * @return A map decoded from the JSON file, or null if the resource could not be found.
     */
    private fun getJsonFromResource(jsonFileName: String): Map<String, List<String>>? {
        val resource =
            this.javaClass
                .classLoader
                .getResourceAsStream(jsonFileName)
                ?.bufferedReader()
                ?.use { it.readText() }
        if (resource == null) {
            return null
        }
        return Json.decodeFromString<Map<String, List<String>>>(resource)
    }

    /**
     * A map of actions.
     */
    val actionList: Map<String, Action> =
        mapOf(
            "CLOSE" to Action("Close", 1, listOf(listOf("CAN_OPEN"))),
            "DRINK" to Action("Drink", 1, listOf(listOf("DRINKABLE", "RECIPIENT"))),
            "FIND" to Action("Find", 1, listOf(listOf())),
            "WALK" to Action("Walk", 1, listOf(listOf())),
            "GRAB" to Action("Grab", 1, listOf(listOf("GRABBABLE"))),
            "LOOKAT" to Action("Look at", 1, listOf(listOf())),
            "LOOKAT_SHORT" to Action("Look at short", 1, listOf(listOf())),
            "LOOKAT_MEDIUM" to Action("Look at", 1, listOf(listOf())),
            "LOOKAT_LONG" to Action("Look at long", 1, listOf(listOf())),
            "OPEN" to Action("Open", 1, listOf(listOf("CAN_OPEN"))),
            "POINTAT" to Action("Point at", 1, listOf(listOf())),
            "PUTBACK" to Action("Put", 2, listOf(listOf("GRABBABLE"), listOf())),
            "PUTIN" to Action("Put in", 2, listOf(listOf("GRABBABLE"), listOf("CAN_OPEN"))),
            "PUTOBJBACK" to Action("Put back", 1, listOf(listOf())),
            "RUN" to Action("Run", 1, listOf(listOf())),
            "SIT" to Action("Sit", 1, listOf(listOf("SITTABLE"))),
            "STANDUP" to Action("Stand up", 0),
            "SWITCHOFF" to Action("Switch off", 1, listOf(listOf("HAS_SWITCH"))),
            "SWITCHON" to Action("Switch on", 1, listOf(listOf("HAS_SWITCH"))),
            "TOUCH" to Action("Touch", 1, listOf(listOf())),
            "TURNTO" to Action("Turn to", 1, listOf(listOf())),
            "WATCH" to Action("Watch", 1, listOf(listOf())),
            "WIPE" to Action("Wipe", 1, listOf(listOf())),
            "PUTON" to Action("PutOn", 1, listOf(listOf("CLOTHES"))),
            "PUTOFF" to Action("PutOff", 1, listOf(listOf("CLOTHES"))),
            "GREET" to Action("Greet", 1, listOf(listOf("PERSON"))),
            "DROP" to Action("Drop", 1, listOf(listOf())),
            "READ" to Action("Read", 1, listOf(listOf("READABLE"))),
            "LIE" to Action("Lie", 1, listOf(listOf("LIEABLE"))),
            "POUR" to Action("Pour", 2, listOf(listOf("POURABLE", "DRINKABLE"), listOf("RECIPIENT"))),
            "TYPE" to Action("Type", 1, listOf(listOf("HAS_SWITCH"))),
            "PUSH" to Action("Push", 1, listOf(listOf("MOVABLE"))),
            "PULL" to Action("Pull", 1, listOf(listOf("MOVABLE"))),
            "MOVE" to Action("Move", 1, listOf(listOf("MOVABLE"))),
            "WASH" to Action("Wash", 1, listOf(listOf())),
            "RINSE" to Action("Rinse", 1, listOf(listOf())),
            "SCRUB" to Action("Scrub", 1, listOf(listOf())),
            "SQUEEZE" to Action("Squeeze", 1, listOf(listOf("CLOTHES"))),
            "PLUGIN" to Action("PlugIn", 1, listOf(listOf("HAS_PLUG"))),
            "PLUGOUT" to Action("PlugOut", 1, listOf(listOf("HAS_PLUG"))),
            "CUT" to Action("Cut", 1, listOf(listOf("EATABLE", "CUTABLE"))),
            "EAT" to Action("Eat", 1, listOf(listOf("EATABLE"))),
            "SLEEP" to Action("Sleep", 0),
            "WAKEUP" to Action("WakeUp", 0),
            "RELEASE" to Action("Release", 1, listOf(listOf())),
        )
}
