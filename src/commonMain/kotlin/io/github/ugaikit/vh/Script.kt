package io.github.ugaikit.vh

private const val OBJECTS_GROUP_IDX = 4
private const val NAME_GROUP_IDX = 1
private const val ID_GROUP_IDX = 2
private const val CHAR_GROUP_IDX = 2
private const val ACTION_GROUP_IDX = 3
private val SCRIPT_LINE_REGEX = Regex("""\s*(<(\w+)>)?\s*\[(\w+)\]((\s*<\w+>\s*\(\d+\))*)\s*""")
private val SCRIPT_OBJECT_REGEX = Regex("""<(\w+)>\s*\((\d+)\)""")

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
 * @property objectPool A pool of objects used in the script.
 * @property lines The list of script lines parsed from the script.
 */
class Script(
    scriptList: List<String>,
) {
    private val objectMap = mutableMapOf<Pair<String, Int>, Obj>()

    // Pool of objects used in the script.
    val objectPool: MutableSet<Obj> = ScriptObjectPool(objectMap)

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
    ): Obj = objectMap.getOrPut(name to id) { Obj(name, id) }

    /**
     * Parses the script into a list of script lines.
     *
     * @param script The script to parse.
     * @return The list of script lines.
     */
    private fun parseScript(script: List<String>): List<ScriptLine> =
        script.map { line ->
            val matchResult =
                SCRIPT_LINE_REGEX.matchEntire(line)
                    ?: throw IllegalArgumentException("Malformed script line: $line")
            val groups = matchResult.groups
            val value = groups[OBJECTS_GROUP_IDX]?.value.orEmpty()
            val objects =
                SCRIPT_OBJECT_REGEX
                    .findAll(value)
                    .map {
                        findObj(
                            it.groups[NAME_GROUP_IDX]?.value.toString(),
                            it.groups[ID_GROUP_IDX]?.value!!.toInt(),
                        )
                    }.toList()

            val scriptLine =
                ScriptLine(
                    groups[CHAR_GROUP_IDX]?.value,
                    groups[ACTION_GROUP_IDX]?.value.toString(),
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
        if (action == null) {
            return false
        }
        if (line.objects.size > action.properties.size) {
            return false
        }

        val properties = Commons.propertiesData()
        line.objects.forEachIndexed { index, obj ->
            val objProperties = properties[obj.name]
            if (objProperties == null) {
                return false
            }
            val requiredProperties = action.properties.getOrNull(index) ?: return false
            requiredProperties.forEach {
                if (!objProperties.contains(it)) {
                    return false
                }
            }
        }
        return true
    }
}

private class ScriptObjectPool(
    private val objectMap: MutableMap<Pair<String, Int>, Obj>,
) : AbstractMutableSet<Obj>() {
    override val size: Int
        get() = objectMap.size

    override fun add(element: Obj): Boolean {
        val key = element.name to element.id
        val existed = objectMap.containsKey(key)
        objectMap[key] = element
        return !existed
    }

    override fun iterator(): MutableIterator<Obj> = objectMap.values.iterator()

    override fun contains(element: Obj): Boolean = objectMap[element.name to element.id] == element

    override fun remove(element: Obj): Boolean = objectMap.remove(element.name to element.id) != null

    override fun clear() {
        objectMap.clear()
    }
}
