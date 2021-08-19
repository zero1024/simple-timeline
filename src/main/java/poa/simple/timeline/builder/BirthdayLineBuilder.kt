package poa.simple.timeline.builder

import poa.simple.timeline.YearTimeLine
import poa.simple.timeline.addText
import poa.simple.timeline.config.Color
import poa.simple.timeline.output.ColoredChar
import java.time.LocalDate

class BirthdayLineBuilder(
    private val defaultChar: Char,
    private val color: Color,
) {

    fun buildLine(
        timeLine: YearTimeLine,
        birthday: LocalDate,
    ): Array<ColoredChar> {
        val line = Array(timeLine.length()) { ColoredChar(defaultChar, color) }
        for (year in timeLine.years()) {
            val date = LocalDate.of(year, birthday.monthValue, birthday.dayOfMonth)
            val pos = timeLine.getCoord(date) - 1
            val age = year - birthday.year
            line.addText("${age}y", pos, Color.BLACK)
        }
        return line
    }

}