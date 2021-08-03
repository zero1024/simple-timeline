package poa.simple.timeline

class YearTimeLine(startYear: Int, endYear: Int) {

    private val map: Map<Int, YearPoint>
    val line: CharArray

    init {
        var idx = 2
        map = generateSequence(startYear) { it + 1 }
            .takeWhile { it <= endYear }
            .map { year ->
                val yearPoint = YearPoint(idx, year.toString().asSequence() + generateSequence { ' ' }.take(7), year)
                idx += 11
                year to yearPoint
            }.toMap()
        line = map.flatMap { it.value.chars }.toCharArray()
    }

    fun getCoord(fromYear: Int, tillYear: Int): Pair<Int, Int> {
        return map[fromYear]!!.position to map[tillYear]!!.position
    }

    fun length() = line.size

}