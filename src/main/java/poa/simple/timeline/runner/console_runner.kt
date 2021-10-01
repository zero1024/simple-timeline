package poa.simple.timeline.runner

import com.charleskorn.kaml.Yaml
import org.jline.reader.LineReaderBuilder
import org.jline.reader.impl.completer.FileNameCompleter
import org.jline.terminal.TerminalBuilder
import poa.simple.timeline.YearTimeLine
import poa.simple.timeline.builder.BirthdayLineBuilder
import poa.simple.timeline.builder.EventLineBuilder
import poa.simple.timeline.builder.TimeRangeLineBuilder
import poa.simple.timeline.config.Color
import poa.simple.timeline.config.Event
import poa.simple.timeline.config.TimeLineConfig
import poa.simple.timeline.config.TimeRangeList
import poa.simple.timeline.output.ConsoleOutput
import poa.simple.timeline.output.ConsoleOutput.Direction.DOWN
import poa.simple.timeline.output.ConsoleOutput.Direction.UP
import java.io.File

val birthdayLineBuilder = BirthdayLineBuilder('-', Color.BLACK)
val eventLineBuilder = EventLineBuilder(' ', Color.BLACK)
val timeRangeLineBuilder = TimeRangeLineBuilder('-', Color.BLACK)

fun main(args: Array<String>) {

    val configFile: File = toFile(if (args.isNotEmpty()) args[0] else System.getenv("PWD"))

    val timeLineConfig = configFile.inputStream().use {
        Yaml.default.decodeFromStream(TimeLineConfig.serializer(), it)
    }

    val timeLine = YearTimeLine(timeLineConfig.base.from.year, timeLineConfig.base.till.year)

    val output = ConsoleOutput(timeLine.line)


    if (timeLineConfig.base.birthday != null) {
        val line = birthdayLineBuilder.buildLine(timeLine, timeLineConfig.base.birthday)
        output.add(line, UP)
    }


    for (timeRanges in timeLineConfig.timeRanges) {

        val borderLine = timeRangeLineBuilder.buildSupportLine(timeLine, timeRanges)

        val (lines, unhandledTimeRanges) = timeRangeLineBuilder.buildLine(timeLine, timeRanges)

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

private fun toFile(path: String): File {
    val trimmedPath = path.trim()
    val file = File(trimmedPath)
    if (!file.exists()) {
        throw IllegalArgumentException("There is no file $trimmedPath")
    }
    return if (file.isDirectory) {
        if (file.files().isEmpty()) {
            throw IllegalArgumentException("$trimmedPath is empty")
        } else {
            println("Available files: ${file.files().joinToString { it }}")
            val reader = LineReaderBuilder.builder()
                .completer(FileNameCompleter())
                .terminal(TerminalBuilder.terminal())
                .build()
            toFile(reader.readLine("simple-timeline>"))
        }
    } else {
        file
    }
}

private fun File?.files(): List<String> = this?.list()?.filter { it.endsWith(".yml") } ?: emptyList()

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
        val (lines, unhandledEvents) = buildLine(timeLine, tmpEvents)
        val supportLine = buildSupportLine(timeLine, tmpEvents)
        output.addAndMergeUpTo(supportLine, direction, lineOffset)
        output.add(lines, direction)
        tmpEvents = unhandledEvents
    }
}
