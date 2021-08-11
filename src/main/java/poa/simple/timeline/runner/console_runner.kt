package poa.simple.timeline.runner

import com.charleskorn.kaml.Yaml
import poa.simple.timeline.*
import poa.simple.timeline.builder.EventLineBuilder
import poa.simple.timeline.config.Event
import poa.simple.timeline.config.TimeLineConfig
import poa.simple.timeline.config.TimeRangeList
import poa.simple.timeline.output.ColoredChar.Companion.BLACK
import poa.simple.timeline.output.ConsoleOutput
import poa.simple.timeline.output.ConsoleOutput.Direction.DOWN
import poa.simple.timeline.output.ConsoleOutput.Direction.UP
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

        eventLineBuilder.handleEvents(timeLine, unhandledTimeRanges.convertToEvents(), output, DOWN, false)
    }

    for (eventGroup in timeLineConfig.eventGroups) {
        EventLineBuilder('-', eventGroup.color)
            .handleEvents(timeLine, eventGroup.sortedList, output, UP, true)
    }

    eventLineBuilder.handleEvents(timeLine, timeLineConfig.sortedEvents, output, UP, true)

    output.print()
}

private fun TimeRangeList.convertToEvents() =
    this.sortedList.map { Event(it.code, it.from, it.till, color = this.color) }

private fun EventLineBuilder.handleEvents(
    timeLine: YearTimeLine,
    events: List<Event>,
    output: ConsoleOutput,
    direction: ConsoleOutput.Direction,
    upToTheBaseLine: Boolean,
) {
    var tmpEvents = events
    while (tmpEvents.isNotEmpty()) {
        val (lines, unhandledEvents) = lineForEvents(timeLine, tmpEvents)
        val supportLine = supportLineForEvents(timeLine, tmpEvents)
        if (upToTheBaseLine) {
            output.addAndMergeUpToBaseLine(supportLine, direction)
        } else {
            output.add(supportLine, direction)
        }
        output.add(lines, direction)
        tmpEvents = unhandledEvents
    }
}
