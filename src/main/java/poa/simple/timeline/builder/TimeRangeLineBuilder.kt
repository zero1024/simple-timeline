package poa.simple.timeline.builder

import poa.simple.timeline.YearTimeLine
import poa.simple.timeline.addText
import poa.simple.timeline.adjust
import poa.simple.timeline.config.Color
import poa.simple.timeline.config.TimeRange
import poa.simple.timeline.config.TimeRangeList
import poa.simple.timeline.lengthIsValid
import poa.simple.timeline.output.ColoredChar

class TimeRangeLineBuilder(
    private val defaultChar: Char,
    private val defaultColor: Color,
) {

    fun buildLine(
        timeLine: YearTimeLine,
        rangeList: TimeRangeList,
    ): Pair<List<Array<ColoredChar>>, TimeRangeList> {

        val unhandled = ArrayList<TimeRange>()

        val color = rangeList.color(defaultColor)

        val dateLine = Array(timeLine.length()) { ColoredChar(defaultChar, color) }
        val durationLine = Array(timeLine.length()) { ColoredChar(defaultChar, color) }
        val nameLine = Array(timeLine.length()) { ColoredChar(defaultChar, color) }

        for (timeRange in rangeList.sortedList) {

            val (fromIdx, tillIdx) = timeLine.getCoord(timeRange.from, timeRange.till)
            val l = tillIdx + 1 - fromIdx

            val dateText = timeRange.date { l >= it.length + 2 }
            val durationText = timeRange.duration()
            val nameText = timeRange.name { l >= it.length + 2 }

            if (lengthIsValid(l, dateText, durationText, nameText)) {
                dateLine.addText(dateText, fromIdx, l, color)
                durationLine.addText(durationText, fromIdx, l, color)
                nameLine.addText(nameText, fromIdx, l, color)
            } else {
                unhandled.add(timeRange)
            }


        }
        return listOf(dateLine, durationLine, nameLine).map { it.adjust('=') } to TimeRangeList(color,
            unhandled)
    }

    fun buildSupportLine(
        timeLine: YearTimeLine,
        rangeList: TimeRangeList,
    ): Array<ColoredChar> {
        val line = Array(timeLine.length()) { ColoredChar.EMPTY_CHAR }

        val minFrom = rangeList.sortedList.minOf { it.from }
        val maxTill = rangeList.sortedList.maxOf { it.till }
        val (fromIdx, tillIdx) = timeLine.getCoord(minFrom, maxTill)

        val color = rangeList.color(defaultColor)

        line[fromIdx] = ColoredChar(':', color)
        line[tillIdx] = ColoredChar(':', color)

        return line
    }

}