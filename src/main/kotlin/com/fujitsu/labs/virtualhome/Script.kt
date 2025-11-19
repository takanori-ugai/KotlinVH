package com.fujitsu.labs.virtualhome

import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

private const val OBJECTS_GROUP_IDX = 4
private const val NAME_GROUP_IDX = 1
private const val ID_GROUP_IDX = 2
private const val CHAR_GROUP_IDX = 2
private const val ACTION_GROUP_IDX = 3

/**
 * Data class representing an object.
 *
 * @property name The name of the object.
 * @property id The id of the object.
 * @property status The status of the object, default is an empty list.
 */
data class Obj(
    val name: String,
    val id: Int,
    var status: List<String> = listOf(),
)

/**
 * Data class representing a script line.
 *
 * @property character The character performing the action, default is null.
 * @property action The action being performed.
 * @property objects The list of objects involved in the action.
 */
data class ScriptLine(
    var character: String? = null,
    val action: String,
    val objects: List<Obj>,
)

/**
 * Class representing a script.
 *
 * @property regex Regular expression used to parse the script.
 * @property regex2 Another regular expression used to parse the script.
 * @property objectPool A set of objects used in the script.
 * @property lines The list of script lines parsed from the script.
 */
class Script(
    scriptList: List<String>,
) {
    // Regular expressions for parsing the script
    val regex = Regex("""\s*(<(\w+)>)?\s*\[(\w+)\]((\s*<\w+>\s*\(\d+\))*)""")
    val regex2 = Regex("""<(\w+)>\s*\((\d+)\)""")

    // Pool of objects used in the script
    val objectPool: MutableSet<Obj> = mutableSetOf()

    // List of lines in the script
    var lines: List<ScriptLine> = parseScript(scriptList)
//    <char0> [WALK] <cat> (366)

    /**
     * Finds an object in the object pool or creates a new one if it doesn't exist.
     *
     * @param name The name of the object.
     * @param id The id of the object.
     * @return The found or created object.
     */
    fun findObj(
        name: String,
        id: Int,
    ): Obj =
        objectPool.firstOrNull { it.name == name && it.id == id }
            ?: Obj(name, id).also { objectPool.add(it) }

    /**
     * Parses the script into a list of script lines.
     *
     * @param script The script to parse.
     * @return The list of script lines.
     */
    private fun parseScript(script: List<String>): List<ScriptLine> =
        script.map { line ->
            val matchResult = regex.find(line)
            val groups = matchResult?.groups
            val value = groups?.get(OBJECTS_GROUP_IDX)?.value.toString()
            val objects =
                regex2
                    .findAll(value)
                    .map {
                        findObj(
                            it.groups[NAME_GROUP_IDX]?.value.toString(),
                            it.groups[ID_GROUP_IDX]?.value!!.toInt(),
                        )
                    }.toList()

            val scriptLine =
                ScriptLine(
                    groups?.get(CHAR_GROUP_IDX)?.value,
                    groups?.get(ACTION_GROUP_IDX)?.value.toString(),
                    objects,
                )

            require(checkLine(scriptLine)) { "Error in Script: $scriptLine" }
            scriptLine
        }

    /**
     * Checks if a script line is valid.
     *
     * @param line The script line to check.
     * @return True if the line is valid, false otherwise.
     */
    fun checkLine(line: ScriptLine): Boolean {
        val actions = Commons.actionList
        val action = actions[line.action]
        logger.info { "Action Properties : $action.properties" }
        if (action == null) {
            return false
        }

        val properties = Commons.propertiesData()
        line.objects.forEachIndexed { index, obj ->
            val objProperties = properties[obj.name]
            logger.info { "Object Properties: $objProperties" }
            if (objProperties == null) {
                return false
            }
            action.properties[index].forEach {
                if (!objProperties.contains(it)) {
                    return false
                }
            }
        }
        return true
    }
}
