package poa.simple.timeline

import poa.simple.timeline.config.Event
import poa.simple.timeline.config.TimeRange
import poa.simple.timeline.config.TimeRangeList
import poa.simple.timeline.output.ColoredChar
import poa.simple.timeline.output.ColoredChar.Companion.BLACK
import poa.simple.timeline.output.ColoredChar.Companion.EMPTY_CHAR
import java.time.LocalDate


private const val borderChar = '|'

private const val maxEventText = 20

fun birthdayLine(
    timeLine: YearTimeLine,
    birthday: LocalDate,
): Array<ColoredChar> {
    val line = Array(timeLine.length()) { ColoredChar('-', "\u001B[43m") }
    for (year in timeLine.years()) {
        val date = LocalDate.of(year, birthday.monthValue, birthday.dayOfMonth)
        val pos = timeLine.getCoord(date) - 1
        val age = year - birthday.year
        line.addText("${age}y", pos, "\u001B[43m")
    }
    return line
}

fun lineForEvents(
    timeLine: YearTimeLine,
    events: List<Event>,
): Pair<List<Array<ColoredChar>>, List<Event>> {

    val unhandledEvents = ArrayList<Event>()

    val dateLine = Array(timeLine.length()) { EMPTY_CHAR }
    val durationLine = Array(timeLine.length()) { EMPTY_CHAR }
    val nameLine = Array(timeLine.length()) { EMPTY_CHAR }

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

            val dateText = event.date()
            val durationText = event.duration()
            val nameText = event.name()

            if (!lengthIsValid(l, dateText, durationText, nameText)) {
                throw IllegalStateException()
            }

            dateLine.addText(dateText, fromIdx, l, event.color)
            durationLine.addText(durationText, fromIdx, l, event.color)
            nameLine.addText(nameText, fromIdx, l, event.color)
        }
    }
    return listOf(dateLine, durationLine, nameLine).map { it.adjust('=') } to unhandledEvents
}

fun lineForTimeRanges(
    timeLine: YearTimeLine,
    rangeList: TimeRangeList,
): Pair<List<Array<ColoredChar>>, TimeRangeList> {

    val unhandled = ArrayList<TimeRange>()

    val dateLine = Array(timeLine.length()) { ColoredChar('-', rangeList.color) }
    val durationLine = Array(timeLine.length()) { ColoredChar('-', rangeList.color) }
    val nameLine = Array(timeLine.length()) { ColoredChar('-', rangeList.color) }

    for (timeRange in rangeList.sortedList) {

        val (fromIdx, tillIdx) = timeLine.getCoord(timeRange.from, timeRange.till)
        val l = tillIdx + 1 - fromIdx

        val dateText = timeRange.date { l >= it.length + 2 }
        val durationText = timeRange.duration { l >= it.length + 2 }
        val nameText = timeRange.name { l >= it.length + 2 }

        if (lengthIsValid(l, dateText, durationText, nameText)) {
            dateLine.addText(dateText, fromIdx, l, rangeList.color)
            durationLine.addText(durationText, fromIdx, l, rangeList.color)
            nameLine.addText(nameText, fromIdx, l, rangeList.color)
        } else {
            unhandled.add(timeRange)
        }


    }
    return listOf(dateLine, durationLine, nameLine).map { it.adjust('=') } to TimeRangeList(rangeList.color, unhandled)
}

fun Array<ColoredChar>.addText(text: String, offset: Int, color: String) {
    for (i in text.indices) {
        this[i + offset] = ColoredChar(text[i], color)
    }
}

fun Array<ColoredChar>.addText(text: String, offset: Int, length: Int, color: String) {

    val indent = (length - text.length) / 2

    val chars = Array(length) { ColoredChar('=', color) }
    chars[0] = ColoredChar(borderChar, color)
    chars[chars.size - 1] = ColoredChar(borderChar, color)

    chars.addText(text, indent, color)

    for ((idx, char) in chars.withIndex()) {
        this[idx + offset] = char
    }
}

fun lengthIsValid(length: Int, vararg texts: String): Boolean {
    for (t in texts) {
        if (length < t.length + 2)
            return false
    }
    return true
}

fun supportLineForTimeRanges(
    timeLine: YearTimeLine,
    rangeList: TimeRangeList,
): Array<ColoredChar> {
    val line = Array(timeLine.length()) { EMPTY_CHAR }

    val minFrom = rangeList.sortedList.minOf { it.from }
    val maxTill = rangeList.sortedList.maxOf { it.till }
    val (fromIdx, tillIdx) = timeLine.getCoord(minFrom, maxTill)

    line[fromIdx] = ColoredChar(borderChar, rangeList.color)
    line[tillIdx] = ColoredChar(borderChar, rangeList.color)

    return line
}

fun supportLineForEvents(
    timeLine: YearTimeLine,
    events: List<Event>,
): Array<ColoredChar> {
    val line = Array(timeLine.length()) { EMPTY_CHAR }

    for (event in events) {
        val from = event.from
        val till = event.till ?: event.from
        val (fromIdx, tillIdx) = timeLine.getCoord(from, till)

        line[fromIdx] = ColoredChar(borderChar, event.color)
        line[tillIdx] = ColoredChar(borderChar, event.color)
    }

    return line
}

private fun Array<ColoredChar>.adjust(fillChar: Char): Array<ColoredChar> {
    var prev: ColoredChar? = null
    for (idx in indices) {
        if (this[idx] == prev && this[idx].char == borderChar) {
            this[idx] = ColoredChar(fillChar, prev.color)
        }
        prev = this[idx]
    }
    return this
}
