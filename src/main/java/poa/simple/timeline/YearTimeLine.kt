package poa.simple.timeline

import java.time.LocalDate

class YearTimeLine(startYear: Int, endYear: Int) {

    private val scale = 12
    private val map: Map<Int, YearPoint>

    val line: CharArray

    init {
        var idx = 2
        map = generateSequence(startYear) { it + 1 }
            .takeWhile { it <= endYear + 1 }
            .map { year ->
                val yearPoint = YearPoint(
                    idx,
                    year.toString().asSequence() + generateSequence { ' ' }.take(scale - 4), year
                )
                idx += scale
                year to yearPoint
            }.toMap()
        line = map.flatMap { it.value.chars }.toCharArray()
    }

    fun getCoord(from: LocalDate, till: LocalDate): Pair<Int, Int> {
        val fromYearPosition = map[from.year]!!.position
        val tillYearPosition = map[till.year]!!.position
        return fromYearPosition + from.monthValue - 1 to tillYearPosition + till.monthValue - 1
    }

    fun length() = line.size

}