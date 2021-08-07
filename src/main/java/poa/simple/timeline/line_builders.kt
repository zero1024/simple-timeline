package poa.simple.timeline

import poa.simple.timeline.config.Event
import poa.simple.timeline.config.TimeRange


private const val borderChar = '|'

private const val maxEventText = 20

fun lineForEvents(
    timeLine: YearTimeLine,
    events: List<Event>,
): Pair<List<CharArray>, List<Event>> {

    val unhandledEvents = ArrayList<Event>()

    val dateLine = CharArray(timeLine.length()) { ' ' }
    val durationLine = CharArray(timeLine.length()) { ' ' }
    val nameLine = CharArray(timeLine.length()) { ' ' }

    var prevTillIdx = 0
    for (event in events) {

        val maxLength = minOf(event.getMaxTextLength() + 2, maxEventText)

        var (fromIdx, tillIdx) = timeLine.getCoord(event.from, event.till ?: event.from)

        var l = tillIdx + 1 - fromIdx

        if (l < maxLength) {
            val ident = maxLength - l
            val leftIdent = ident / 2
            val rightIdent = (ident / 2) + ident % 2
            fromIdx -= leftIdent
            tillIdx += rightIdent
            l = tillIdx + 1 - fromIdx
        }

        if (fromIdx - prevTillIdx < 2) {
            unhandledEvents.add(event)
        } else {
            prevTillIdx = tillIdx

            fun applyToLine(text: String, line: CharArray) {

                if (l < text.length + 2)
                    throw IllegalStateException()

                val ident = (l - text.length) / 2

                val chars = Array(l) { '=' }
                chars[0] = borderChar
                chars[chars.size - 1] = borderChar

                for (i in text.indices) {
                    chars[i + ident] = text[i]
                }

                for ((idx, char) in chars.withIndex()) {
                    line[idx + fromIdx] = char
                }
            }

            applyToLine(event.date(), dateLine)
            applyToLine(event.duration(), durationLine)
            applyToLine(event.name(), nameLine)
        }
    }
    return listOf(dateLine, durationLine, nameLine).map { it.adjust('=') } to unhandledEvents
}

fun lineForTimeRanges(
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

fun supportLineForTimeRanges(
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

fun supportLineForEvents(
    timeLine: YearTimeLine,
    events: List<Event>,
): CharArray {
    val line = CharArray(timeLine.length()) { ' ' }

    for (event in events) {
        val minFrom = event.from
        val maxTill = event.till ?: event.from
        val (fromIdx, tillIdx) = timeLine.getCoord(minFrom, maxTill)

        line[fromIdx] = borderChar
        line[tillIdx] = borderChar
    }

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
