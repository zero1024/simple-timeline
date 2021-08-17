package poa.simple.timeline

import poa.simple.timeline.config.Color
import poa.simple.timeline.config.TimeRange
import poa.simple.timeline.config.TimeRangeList
import poa.simple.timeline.output.ColoredChar
import poa.simple.timeline.output.ColoredChar.Companion.EMPTY_CHAR
import java.time.LocalDate


fun birthdayLine(
    timeLine: YearTimeLine,
    birthday: LocalDate,
): Array<ColoredChar> {
    val line = Array(timeLine.length()) { ColoredChar('-', Color.BLACK) }
    for (year in timeLine.years()) {
        val date = LocalDate.of(year, birthday.monthValue, birthday.dayOfMonth)
        val pos = timeLine.getCoord(date) - 1
        val age = year - birthday.year
        line.addText("${age}y", pos, Color.BLACK)
    }
    return line
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
        val durationText = timeRange.duration()
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

fun Array<ColoredChar>.addText(text: String, offset: Int, color: Color) {
    for (i in text.indices) {
        this[i + offset] = ColoredChar(text[i], color)
    }
}

fun Array<ColoredChar>.addText(text: String, offset: Int, length: Int, color: Color) {

    val indent = (length - text.length) / 2

    val chars = Array(length) { ColoredChar('=', color) }
    chars[0] = ColoredChar('|', color)
    chars[chars.size - 1] = ColoredChar('|', color)

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

    line[fromIdx] = ColoredChar(':', rangeList.color)
    line[tillIdx] = ColoredChar(':', rangeList.color)

    return line
}

fun Array<ColoredChar>.adjust(fillChar: Char): Array<ColoredChar> {
    var prev: ColoredChar? = null
    for (idx in indices) {
        if (this[idx] == prev && this[idx].char == '|') {
            this[idx] = ColoredChar(fillChar, prev.color)
        }
        prev = this[idx]
    }
    return this
}
