package com.fujitsu.labs.virtualhome

enum class AddCharacterMode(val value: String) {
    FixPosition("fix_position"),
    Random("random") ;

    override fun toString(): String = value
}
