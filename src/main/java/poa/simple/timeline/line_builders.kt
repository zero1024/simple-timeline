package poa.simple.timeline

import poa.simple.timeline.config.Event
import poa.simple.timeline.config.TimeRange
import java.lang.IllegalStateException
import java.time.LocalDate


private const val borderChar = '|'

private const val maxEventText = 20

fun birthdayLine(
    timeLine: YearTimeLine,
    birthday: LocalDate,
): CharArray {
    val line = CharArray(timeLine.length()) { '-' }
    for (year in timeLine.years()) {
        val date = LocalDate.of(year, birthday.monthValue, birthday.dayOfMonth)
        val pos = timeLine.getCoord(date) - 1
        val age = year - birthday.year
        line.addText("${age}y", pos)
    }
    return line
}

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

            val dateText = event.date()
            val durationText = event.duration()
            val nameText = event.name()

            if (!lengthIsValid(l, dateText, durationText, nameText)) {
                throw IllegalStateException()
            }

            dateLine.addText(dateText, fromIdx, l)
            durationLine.addText(durationText, fromIdx, l)
            nameLine.addText(nameText, fromIdx, l)
        }
    }
    return listOf(dateLine, durationLine, nameLine).map { it.adjust('=') } to unhandledEvents
}

fun lineForTimeRanges(
    timeLine: YearTimeLine,
    rangeList: List<TimeRange>,
): Pair<List<CharArray>, List<TimeRange>> {

    val unhandled = ArrayList<TimeRange>()

    val dateLine = CharArray(timeLine.length()) { '-' }
    val durationLine = CharArray(timeLine.length()) { '-' }
    val nameLine = CharArray(timeLine.length()) { '-' }

    for (timeRange in rangeList) {

        val (fromIdx, tillIdx) = timeLine.getCoord(timeRange.from, timeRange.till)
        val l = tillIdx + 1 - fromIdx

        val dateText = timeRange.date { l >= it.length + 2 }
        val durationText = timeRange.duration { l >= it.length + 2 }
        val nameText = timeRange.name { l >= it.length + 2 }

        if (lengthIsValid(l, dateText, durationText, nameText)) {
            dateLine.addText(dateText, fromIdx, l)
            durationLine.addText(durationText, fromIdx, l)
            nameLine.addText(nameText, fromIdx, l)
        } else {
            unhandled.add(timeRange)
        }


    }
    return listOf(dateLine, durationLine, nameLine).map { it.adjust('=') } to unhandled
}

fun CharArray.addText(text: String, offset: Int) {
    for (i in text.indices) {
        this[i + offset] = text[i]
    }
}

fun CharArray.addText(text: String, offset: Int, length: Int) {

    val indent = (length - text.length) / 2

    val chars = CharArray(length) { '=' }
    chars[0] = borderChar
    chars[chars.size - 1] = borderChar

    chars.addText(text, indent)

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
        val from = event.from
        val till = event.till ?: event.from
        val (fromIdx, tillIdx) = timeLine.getCoord(from, till)

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
