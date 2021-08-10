package poa.simple.timeline.output

data class ColoredChar(val char: Char, val color: String) {
    companion object {
        const val BLACK = "\u001B[30m"
        val EMPTY_CHAR = ColoredChar(' ', BLACK)
    }
}