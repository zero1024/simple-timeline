package poa.simple.timeline

import poa.simple.timeline.config.TimeRange
import java.lang.IllegalStateException


private const val borderChar = '|'

fun buildLine(
    timeLine: YearTimeLine,
    rangeList: List<TimeRange>,
    lineText: (TimeRange) -> String,
    lineShortText: (TimeRange) -> String,
): CharArray {

    val line = CharArray(timeLine.length()) { '-' }

    for (timeRange in rangeList) {

        val (fromIdx, tillIdx) = timeLine.getCoord(timeRange.from, timeRange.till)
        val l = tillIdx + 1 - fromIdx

        val text =
            if (l >= lineText(timeRange).length + 2) lineText(timeRange)
            else {
                val shortText = lineShortText(timeRange)
                if (l >= shortText.length + 2) shortText
                else throw IllegalStateException()
            }

        val ident = (l - text.length) / 2

        val timeRangeChars = Array(l) { '=' }
        timeRangeChars[0] = borderChar
        timeRangeChars[timeRangeChars.size - 1] = borderChar

        for (i in text.indices) {
            timeRangeChars[i + ident] = text[i]
        }

        for ((idx, char) in timeRangeChars.withIndex()) {
            line[idx + fromIdx] = char
        }

    }
    return line.adjust('=')
}

fun buildBorderLine(
    timeLine: YearTimeLine,
    rangeList: List<TimeRange>,
): CharArray {
    val line = CharArray(timeLine.length()) { ' ' }

    val minFrom = rangeList.minOf { it.from }
    val maxTill = rangeList.maxOf { it.till }
    val (fromIdx, tillIdx) = timeLine.getCoord(minFrom, maxTill)

    line[fromIdx] = borderChar
    line[tillIdx] = borderChar

    return line
}

private fun CharArray.adjust(fillChar: Char): CharArray {
    var prev: Char? = null
    for (idx in indices) {
        if (this[idx] == prev && this[idx] == borderChar) {
            this[idx] = fillChar
        }
        prev = this[idx]
    }
    return this
}
