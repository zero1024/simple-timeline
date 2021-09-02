package poa.simple.timeline.output

import poa.simple.timeline.config.BackgroundColor
import poa.simple.timeline.config.Color

class ConsoleOutput(baseLine: CharArray) {

    private val baseLine: Array<ColoredChar>

    init {
        this.baseLine = baseLine.map { ColoredChar(it, backColor = BackgroundColor.YELLOW) }.toTypedArray()
    }

    private val linesBefore = ArrayList<Array<ColoredChar>>()
    private val linesAfter = ArrayList<Array<ColoredChar>>()

    fun add(lines: List<Array<ColoredChar>>, direction: Direction) {
        if (direction == Direction.UP) {
            lines.reversed()
        } else {
            lines
        }.forEach { add(it, direction) }
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
                    if (isFillChar(line[idx].char)) {
                        line[idx] = c
                    }
                }
            }
        }
        add(newLine, direction)
    }

    fun addPointOfInterest(idx: Int) {
        for (line in allLines()) {
            if (isFillChar(line[idx].char)) {
                line[idx] = ColoredChar('|', Color.BLACK, line[idx].backColor, isBold = true)
            }
        }
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
            print(char.color.code)
            if (char.backColor != null) {
                print(char.backColor.code)
            }
            if (char.isBold) {
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

    private fun isFillChar(char: Char) = char == ' ' || char == '-'

    private fun lines(direction: Direction): ArrayList<Array<ColoredChar>> {
        return when (direction) {
            Direction.UP -> linesBefore
            Direction.DOWN -> linesAfter
        }
    }

    private fun allLines(): List<Array<ColoredChar>> {
        return linesBefore + listOf(baseLine) + linesAfter
    }

}