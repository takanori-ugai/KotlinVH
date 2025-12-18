package io.github.ugaikit.vh

/**
 * Enum representing the mode for adding a character.
 *
 * @property value The string representation of the mode.
 */
enum class AddCharacterMode(
    val value: String,
) {
    /**
     * Represents a fixed position mode.
     */
    FixPosition("fix_position"),

    /**
     * Represents a random mode.
     */
    Random("random"),
    ;

    /**
     * Returns the string representation of the mode.
     *
     * @return The string representation of the mode.
     */
    override fun toString(): String = value
}
