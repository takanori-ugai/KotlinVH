package com.fujitsu.labs.virtualhome

import mu.KotlinLogging
private val logger = KotlinLogging.logger {}

/**
 * Data class representing an object.
 *
 * @property name The name of the object.
 * @property id The id of the object.
 * @property status The status of the object, default is an empty list.
 */
data class Obj(val name: String, val id: Int, var status: List<String> = listOf())

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
    val objects: List<Obj>
)

/**
 * Class representing a script.
 *
 * @property regex Regular expression used to parse the script.
 * @property regex2 Another regular expression used to parse the script.
 * @property objectPool A set of objects used in the script.
 * @property lines The list of script lines parsed from the script.
 */
class Script(scriptList: List<String>) {

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
    fun findObj(name: String, id: Int): Obj {
        return objectPool.firstOrNull { it.name == name && it.id == id }
            ?: Obj(name, id).also { objectPool.add(it) }
    }

    /**
     * Parses the script into a list of script lines.
     *
     * @param script The script to parse.
     * @return The list of script lines.
     */
    private fun parseScript(script: List<String>): List<ScriptLine> {
        return script.map { line ->
            val objects = regex2.findAll(regex.find(line)?.groups?.get(4)?.value.toString())
                .map { findObj(it.groups[1]?.value.toString(), it.groups[2]?.value!!.toInt()) }
                .toList()

            val scriptLine = ScriptLine(
                regex.find(line)?.groups?.get(2)?.value,
                regex.find(line)?.groups?.get(3)?.value.toString(),
                objects
            )

            if (!checkLine(scriptLine)) throw Exception("Error in Script: $scriptLine")
            scriptLine
        }
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
        if (action == null) return false
        val properties = Commons.propertiesData()
        line.objects.forEachIndexed { index, obj ->
            val objProperties = properties[obj.name]
            logger.info { "Object Properties: $objProperties" }
            if (objProperties == null) return false
            action.properties[index].forEach {
                if (!objProperties.contains(it)) return false
            }
        }
        return true
    }
}
