package poa.simple.timeline

import poa.simple.timeline.config.TimeRange


fun buildLine(
    timeLine: YearTimeLine,
    rangeList: List<TimeRange>,
    lineText: (TimeRange) -> String,
): CharArray {
    val line = CharArray(timeLine.length()) { ' ' }
    for (timeRange in rangeList) {

        val text = lineText(timeRange)

        val (fromIdx, tillIdx) = timeLine.getCoord(timeRange.from.year, timeRange.till.year)

        val l = tillIdx + 1 - fromIdx
        assert(l >= text.length)

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

    val minYear = rangeList.minOf { it.from.year }
    val maxYear = rangeList.maxOf { it.till.year }
    val (fromIdx, tillIdx) = timeLine.getCoord(minYear, maxYear)

    line[fromIdx] = '|'
    line[tillIdx] = '|'

    return line
}
