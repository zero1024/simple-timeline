package poa.simple.timeline.output

class ConsoleOutput(private val baseLine: CharArray) {

    private val linesBefore = ArrayList<CharArray>()
    private val linesAfter = ArrayList<CharArray>()

    fun add(line: CharArray, direction: Direction = Direction.DOWN) {
        lines(direction).add(line)
    }

    fun addAndMergeUpToBaseLine(newLine: CharArray, direction: Direction = Direction.DOWN) {
        for ((idx, c) in newLine.withIndex()) {
            if (c != ' ' && c != '-') {
                for (line in lines(direction)) {
                    if (line[idx] == ' ' || line[idx] == '-') {
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

    private fun print(chars: CharArray) {
        for (char in chars) {
            print(char)
        }
        println()
    }

    enum class Direction {
        UP, DOWN
    }

    private fun lines(direction: Direction): ArrayList<CharArray> {
        return when (direction) {
            Direction.UP -> linesBefore
            Direction.DOWN -> linesAfter
        }
    }

}