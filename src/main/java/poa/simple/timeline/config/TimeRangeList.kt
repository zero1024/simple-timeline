package poa.simple.timeline.config

import kotlinx.serialization.Serializable
import poa.simple.timeline.output.ColoredChar.Companion.BLACK

@Serializable
data class TimeRangeList(
    val color: String = BLACK,
    private val list: List<@Serializable(with = TimeRangeSerializer::class) TimeRange>,
) {
    val sortedList = list.sortedBy { it.from }
}