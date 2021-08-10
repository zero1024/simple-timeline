package poa.simple.timeline

import java.time.LocalDate

class YearTimeLine(private val startYear: Int, private val endYear: Int) {

    private val scale = 12
    private val map: Map<Int, YearPoint>

    val line: CharArray

    init {
        var idx = 2
        map = yearsPlusOne()
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

    fun yearsPlusOne() = generateSequence(startYear) { it + 1 }.takeWhile { it <= endYear + 1 }
    fun years() = generateSequence(startYear) { it + 1 }.takeWhile { it <= endYear }

    fun getCoord(from: LocalDate, till: LocalDate) = getCoord(from) to getCoord(till)

    fun getCoord(date: LocalDate): Int {
        val position = map[date.year]!!.position
        return position + date.monthValue - 1
    }

    fun length() = line.size

}