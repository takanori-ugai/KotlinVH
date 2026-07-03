package io.github.ugaikit.vh

/**
 * Data class representing an action script.
 *
 * @param character the character performing the action
 * @param action the action being performed
 * @param objects the objects involved in the action
 */
data class ActionScript(
    val character: String?,
    val action: String,
    val objects: List<Pair<String, Int>>,
)

private val ACTION_SCRIPT_REGEX = Regex("""\s*(?:<([^>]+)>)?\s*\[([^\]]+)\]\s*(.*)""")
private val OBJECT_TIME_REGEX = Regex("""<([^>]+)>\s*\((\d+)\)""")

/**
 * Parses a list of action script lines into a list of ActionScript objects.
 *
 * @param script the list of action script lines
 * @return the list of ActionScript objects
 */
fun parseActionScript(script: List<String>): List<ActionScript> =
    script.mapNotNull { line ->
        ACTION_SCRIPT_REGEX.matchEntire(line)?.destructured?.let { (character, action, rest) ->
            val objects =
                OBJECT_TIME_REGEX
                    .findAll(rest)
                    .map { match ->
                        val (obj, time) = match.destructured
                        obj to time.toInt()
                    }.toList()
            ActionScript(character.ifEmpty { null }, action, objects)
        }
    }

// Example usage
// fun main() {
//    val script =
//        listOf(
//            "<char0> [WALK] <wine> (93) <book> (89)",
//            "<char0> [GRAB] <wine> (93)",
//            "  <char0> [DRINK] <wine> (93)",
//            "[DRINK] <wine> (93)",
//        )
//
//    val parsedScripts = parseActionScript(script)
//    parsedScripts.forEach { println(it) }
// }
