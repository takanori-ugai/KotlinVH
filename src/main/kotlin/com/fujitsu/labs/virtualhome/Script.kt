package com.fujitsu.labs.virtualhome

data class Obj(val name: String, val id: Int)

data class ScriptLine(
    var character: String? = null,
    val action: String,
    val objects: List<Obj>
)

class Script(scriptList: List<String>) {

    //    val regex = Regex("""<(.+)>\w*<[.+]>\w*<(.+)>\w*\((.+)\)""")
    val regex = Regex("""\s*(<(\w+)>)?\s*\[(\w+)\]((\s*<\w+>\s*\(\d+\))*)""")
    val regex2 = Regex("""<(\w+)>\s*\((\d+)\)""")
    var lines: List<ScriptLine> = parseScript(scriptList)
//    <char0> [WALK] <cat> (366)

    private fun parseScript(script: List<String>): List<ScriptLine> {
        val linesTmp: MutableList<ScriptLine> = mutableListOf()
        script.forEach {
            val match = regex.find(it)
            val match2 = regex2.findAll(match?.groups?.get(4)?.value.toString())
            val objects: MutableList<Obj> = mutableListOf()
            match2.forEach {
                objects.add(Obj(it.groups.get(1)?.value.toString(), it.groups[2]?.value?.toInt()!!))
            }
            linesTmp.add(ScriptLine(match?.groups?.get(2)?.value, match?.groups?.get(3)?.value.toString(), objects))
        }
        return linesTmp
    }
}
