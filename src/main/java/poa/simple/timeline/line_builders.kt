package poa.simple.timeline

import poa.simple.timeline.config.TimeRange
import java.lang.IllegalStateException


fun buildLine(
    timeLine: YearTimeLine,
    rangeList: List<TimeRange>,
    lineText: (TimeRange) -> String,
    lineShortText: (TimeRange) -> String? = { null },
): CharArray {

    val line = CharArray(timeLine.length()) { ' ' }

    for (timeRange in rangeList) {

        val (fromIdx, tillIdx) = timeLine.getCoord(timeRange.from, timeRange.till)
        val l = tillIdx + 1 - fromIdx

        val text =
            if (l >= lineText(timeRange).length + 2) lineText(timeRange)
            else {
                val shortText = lineShortText(timeRange)
                if (shortText != null && l >= shortText.length + 2) shortText
                else throw IllegalStateException()
            }

        val ident = (l - text.length) / 2

        val timeRangeChars = Array(l) { '=' }
        timeRangeChars[0] = '|'
        timeRangeChars[timeRangeChars.size - 1] = '|'

        for (i in text.indices) {
            timeRangeChars[i + ident] = text[i]
        }

        for ((idx, char) in timeRangeChars.withIndex()) {
            line[idx + fromIdx] = char
        }

    }
    return line
}

fun buildBorderLine(
    timeLine: YearTimeLine,
    rangeList: List<TimeRange>,
): CharArray {
    val line = CharArray(timeLine.length()) { ' ' }

    val minFrom = rangeList.minOf { it.from }
    val maxTill = rangeList.maxOf { it.till }
    val (fromIdx, tillIdx) = timeLine.getCoord(minFrom, maxTill)

    line[fromIdx] = '|'
    line[tillIdx] = '|'

    return line
}
