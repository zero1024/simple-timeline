package poa.simple.timeline.builder

import poa.simple.timeline.*
import poa.simple.timeline.config.Event
import poa.simple.timeline.output.ColoredChar

class EventLineBuilder(
    private val defaultChar: Char,
    private val defaultColor: String,
    private val maxEventText: Int = 20,
) {

    fun lineForEvents(
        timeLine: YearTimeLine,
        events: List<Event>,
    ): Pair<List<Array<ColoredChar>>, List<Event>> {

        val unhandledEvents = ArrayList<Event>()

        val dateLine = Array(timeLine.length()) { ColoredChar(defaultChar, defaultColor) }
        val durationLine = Array(timeLine.length()) { ColoredChar(defaultChar, defaultColor) }
        val nameLine = Array(timeLine.length()) { ColoredChar(defaultChar, defaultColor) }

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

            if (fromIdx - prevTillIdx < 1) {
                unhandledEvents.add(event)
            } else {
                prevTillIdx = tillIdx

                val dateText = event.date()
                val durationText = event.duration()
                val nameText = event.name()

                if (!lengthIsValid(l, dateText, durationText, nameText)) {
                    throw IllegalStateException()
                }

                dateLine.addText(dateText, fromIdx, l, event.color(defaultColor))
                durationLine.addText(durationText, fromIdx, l, event.color(defaultColor))
                nameLine.addText(nameText, fromIdx, l, event.color(defaultColor))
            }
        }
        return listOf(dateLine, durationLine, nameLine).map { it.adjust('=') } to unhandledEvents
    }

    fun supportLineForEvents(
        timeLine: YearTimeLine,
        events: List<Event>,
    ): Array<ColoredChar> {
        val line = Array(timeLine.length()) { ColoredChar.EMPTY_CHAR }

        for (event in events) {
            val from = event.from
            val till = event.till ?: event.from
            val (fromIdx, tillIdx) = timeLine.getCoord(from, till)

            line[fromIdx] = ColoredChar(':', event.color(defaultColor))
            line[tillIdx] = ColoredChar(':', event.color(defaultColor))
        }

        return line
    }

}