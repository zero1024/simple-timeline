package poa.simple.timeline.output

import poa.simple.timeline.output.ColoredChar.Companion.BLACK

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

    fun addAndMergeUpToBaseLine(newLine: Array<ColoredChar>, direction: Direction) {
        for ((idx, c) in newLine.withIndex()) {
            if (c.char != ' ' && c.char != '-') {
                for (line in lines(direction)) {
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
        var color = BLACK
        for (char in chars) {
            if (char.color != color) {
                color = char.color
                print(char.color)
            }
            print(char.char)
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