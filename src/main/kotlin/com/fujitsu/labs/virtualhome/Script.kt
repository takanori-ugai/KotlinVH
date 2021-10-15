package com.fujitsu.labs.virtualhome

data class Obj(val name: String, val id: Int, var status: List<String> = listOf())

data class ScriptLine(
    var character: String? = null,
    val action: String,
    val objects: List<Obj>
)

class Script(scriptList: List<String>) {

    //    val regex = Regex("""<(.+)>\w*<[.+]>\w*<(.+)>\w*\((.+)\)""")
    val regex = Regex("""\s*(<(\w+)>)?\s*\[(\w+)\]((\s*<\w+>\s*\(\d+\))*)""")
    val regex2 = Regex("""<(\w+)>\s*\((\d+)\)""")
    val objectPool: MutableSet<Obj> = mutableSetOf()
    var lines: List<ScriptLine> = parseScript(scriptList)
//    <char0> [WALK] <cat> (366)

    fun findObj(name: String, id: Int): Obj {
        val find = objectPool.find { it.name == name && it.id == id }
        if (find == null) {
            val newObj = Obj(name, id)
            objectPool.add(newObj)
            return newObj
        } else {
            return find
        }
    }

    private fun parseScript(script: List<String>): List<ScriptLine> {
        val linesTmp: MutableList<ScriptLine> = mutableListOf()
        script.forEach {
            val objects: MutableList<Obj> = mutableListOf()
            val match = regex.find(it)
            val match2 = regex2.findAll(match?.groups?.get(4)?.value.toString())
            match2.forEach {
                objects.add(findObj(it.groups.get(1)?.value.toString(), it.groups[2]?.value?.toInt()!!))
            }
            val scriptLine = ScriptLine(match?.groups?.get(2)?.value, match?.groups?.get(3)?.value.toString(), objects)
            if (!checkLine(scriptLine)) throw Exception("Error in Script: $scriptLine")
            linesTmp.add(scriptLine)
        }
        return linesTmp
    }

    fun checkLine(line: ScriptLine): Boolean {
        val actions = Commons.actionList
        val action = actions[line.action]
        println("Action Properties : $action.properties")
        if (action == null) return false
        val properties = Commons.propertiesData()
        line.objects.forEachIndexed { index, obj ->
            val objProperties = properties[obj.name]
            println("Object Properties: $objProperties")
            if (objProperties == null) return false
            action.properties[index].forEach {
                if (!objProperties.contains(it)) return false
            }
        }
        return true
    }
}
