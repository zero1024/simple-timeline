package poa.simple.timeline.config

import kotlinx.serialization.Serializable

@Serializable
data class TimeRangeList(
    val color: Color? = null,
    private val list: List<@Serializable(with = TimeRangeSerializer::class) TimeRange>,
) {

    val sortedList = list.sortedBy { it.from }

    fun color(default: Color) = color ?: default
}