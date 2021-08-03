package poa.simple.timeline.output

import java.util.*

class ConsoleOutput(private val baseLine: CharArray) {

    private val linesAfter = LinkedList<CharArray>()

    fun add(line: CharArray) {
        linesAfter.add(line)
    }

    fun addAndMergeUpToBaseLine(newLine: CharArray) {
        for ((idx, c) in newLine.withIndex()) {
            if (c != ' ' && c != '-') {
                for (line in linesAfter) {
                    if (line[idx] == ' ' || line[idx] == '-') {
                        line[idx] = c
                    }
                }
            }
        }
        add(newLine)
    }

    fun print() {
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

}