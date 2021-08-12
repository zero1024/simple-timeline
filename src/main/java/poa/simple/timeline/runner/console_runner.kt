package poa.simple.timeline.runner

import com.charleskorn.kaml.Yaml
import poa.simple.timeline.YearTimeLine
import poa.simple.timeline.birthdayLine
import poa.simple.timeline.builder.EventLineBuilder
import poa.simple.timeline.config.Event
import poa.simple.timeline.config.TimeLineConfig
import poa.simple.timeline.config.TimeRangeList
import poa.simple.timeline.lineForTimeRanges
import poa.simple.timeline.output.ColoredChar.Companion.BLACK
import poa.simple.timeline.output.ConsoleOutput
import poa.simple.timeline.output.ConsoleOutput.Direction.DOWN
import poa.simple.timeline.output.ConsoleOutput.Direction.UP
import poa.simple.timeline.supportLineForTimeRanges
import java.io.File

fun main(args: Array<String>) {

    val configFile = args[0]

    val timeLineConfig = File(configFile).inputStream().use {
        Yaml.default.decodeFromStream(TimeLineConfig.serializer(), it)
    }

    val timeLine = YearTimeLine(timeLineConfig.base.from.year, timeLineConfig.base.till.year)

    val output = ConsoleOutput(timeLine.line)

    if (timeLineConfig.base.birthday != null) {
        val line = birthdayLine(timeLine, timeLineConfig.base.birthday)
        output.add(line, UP)
    }


    val eventLineBuilder = EventLineBuilder(' ', BLACK)

    for (timeRanges in timeLineConfig.timeRanges) {

        val borderLine = supportLineForTimeRanges(timeLine, timeRanges)

        val (lines, unhandledTimeRanges) = lineForTimeRanges(timeLine, timeRanges)

        output.addAndMergeUpToBaseLine(borderLine, DOWN)
        output.add(lines, DOWN)

        eventLineBuilder.handleEvents(timeLine,
            unhandledTimeRanges.convertToEvents(),
            output,
            DOWN,
            output.currentLineOffset(DOWN))
    }

    for (eventGroup in timeLineConfig.eventGroups) {
        EventLineBuilder('-', eventGroup.color)
            .handleEvents(timeLine, eventGroup.sortedList, output, UP, 1)
    }

    eventLineBuilder.handleEvents(timeLine, timeLineConfig.sortedEvents, output, UP, 1)

    output.print()
}

private fun TimeRangeList.convertToEvents() =
    this.sortedList.map { Event(it.code, it.from, it.till, color = this.color) }

private fun EventLineBuilder.handleEvents(
    timeLine: YearTimeLine,
    events: List<Event>,
    output: ConsoleOutput,
    direction: ConsoleOutput.Direction,
    lineOffset: Int,
) {
    var tmpEvents = events
    while (tmpEvents.isNotEmpty()) {
        val (lines, unhandledEvents) = lineForEvents(timeLine, tmpEvents)
        val supportLine = supportLineForEvents(timeLine, tmpEvents)
        output.addAndMergeUpTo(supportLine, direction, lineOffset)
        output.add(lines, direction)
        tmpEvents = unhandledEvents
    }
}
