package poa.simple.timeline.runner

import com.charleskorn.kaml.Yaml
import poa.simple.timeline.*
import poa.simple.timeline.config.TimeLineConfig
import poa.simple.timeline.output.ConsoleOutput
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

    for (timeRanges in timeLineConfig.timeRanges) {

        val borderLine = supportLineForTimeRanges(timeLine, timeRanges)

        val dateLine = lineForTimeRanges(timeLine, timeRanges, { it.date() }, { it.shortDate() })
        val durationLine = lineForTimeRanges(timeLine, timeRanges, { it.duration() }, { it.shortDuration() })
        val nameLine = lineForTimeRanges(timeLine, timeRanges, { it.name() }, { it.shortName() })

        output.addAndMergeUpToBaseLine(borderLine)
        output.add(dateLine)
        output.add(durationLine)
        output.add(nameLine)
    }

    var events = timeLineConfig.events

    while (events.isNotEmpty()) {
        val (lines, unhandledEvents) = lineForEvents(timeLine, events)
        output.addAndMergeUpToBaseLine(supportLineForEvents(timeLine, events), UP)
        output.add(lines, UP)
        events = unhandledEvents
    }


    output.print()
}
