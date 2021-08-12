package poa.simple.timeline.output

class ConsoleOutput(baseLine: CharArray) {

    private val baseLine: Array<ColoredChar>

    init {
        this.baseLine = baseLine.map { ColoredChar(it, "\u001B[43m") }.toTypedArray()
    }

    private val linesBefore = ArrayList<Array<ColoredChar>>()
    private val linesAfter = ArrayList<Array<ColoredChar>>()

    fun add(lines: List<Array<ColoredChar>>, direction: Direction) {
        lines.forEach { add(it, direction) }
    }

    fun add(line: Array<ColoredChar>, direction: Direction) {
        lines(direction).add(line)
    }

    fun currentLineOffset(direction: Direction) = lines(direction).size

    fun addAndMergeUpToBaseLine(newLine: Array<ColoredChar>, direction: Direction) {
        addAndMergeUpTo(newLine, direction, 0)
    }

    fun addAndMergeUpTo(newLine: Array<ColoredChar>, direction: Direction, mergeOffset: Int) {
        val lines = lines(direction).let {
            it.reversed().take(it.size - mergeOffset)
        }
        for ((idx, c) in newLine.withIndex()) {
            if (c.char != ' ' && c.char != '-') {
                for (line in lines) {
                    if (line[idx].char == ' ' || line[idx].char == '-') {
                        line[idx] = c
                    }
                }
            }
        }
        add(newLine, direction)
    }

    fun print() {
        for (line in linesBefore.reversed()) {
            print(line)
        }
        print(baseLine)
        for (line in linesAfter) {
            print(line)
        }
    }

    private fun print(chars: Array<ColoredChar>) {
        for (char in chars) {
            print(char.color)
            if (Character.isLetterOrDigit(char.char)) {
                print("\u001B[1m")
            }
            print(char.char)
            print("\u001B[0m")
        }
        println()
    }

    enum class Direction {
        UP, DOWN
    }

    private fun lines(direction: Direction): ArrayList<Array<ColoredChar>> {
        return when (direction) {
            Direction.UP -> linesBefore
            Direction.DOWN -> linesAfter
        }
    }

}