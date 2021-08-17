package poa.simple.timeline.config

import kotlinx.serialization.Serializable

@Serializable
data class TimeRangeList(
    val color: Color = Color.BLACK,
    private val list: List<@Serializable(with = TimeRangeSerializer::class) TimeRange>,
) {
    val sortedList = list.sortedBy { it.from }
}