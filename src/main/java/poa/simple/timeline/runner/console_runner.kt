package poa.simple.timeline.runner

import com.charleskorn.kaml.Yaml
import poa.simple.timeline.YearTimeLine
import poa.simple.timeline.buildBorderLine
import poa.simple.timeline.buildLine
import poa.simple.timeline.config.TimeLineConfig
import poa.simple.timeline.config.TimeRange
import poa.simple.timeline.output.ConsoleOutput
import java.io.File
import java.time.format.DateTimeFormatter

fun main(args: Array<String>) {

    val configFile = args[0]

    val timeLineConfig = File(configFile).inputStream().use {
        Yaml.default.decodeFromStream(TimeLineConfig.serializer(), it)
    }

    val timeLine = YearTimeLine(timeLineConfig.base.from.year, timeLineConfig.base.till.year)

    val output = ConsoleOutput(timeLine.line)

    for (timeRanges in timeLineConfig.timeRanges) {

        val borderLine = buildBorderLine(timeLine, timeRanges)

        val dateLine = buildLine(timeLine, timeRanges) { it.formattedDates() }
        val durationLine = buildLine(timeLine, timeRanges) { " ${it.yearsDuration()} years " }
        val nameLine = buildLine(timeLine, timeRanges) { " ${it.name()} " }

        output.addAndMergeUpToBaseLine(borderLine)
        output.add(dateLine)
        output.add(durationLine)
        output.add(nameLine)

    }
    output.print()
}

private val formatter = DateTimeFormatter.ofPattern("MM.yyyy")
fun TimeRange.formattedDates() = " ${this.from.format(formatter)} - ${this.till.format(formatter)} "