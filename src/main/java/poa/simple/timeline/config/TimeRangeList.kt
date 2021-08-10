package poa.simple.timeline.config

import kotlinx.serialization.Serializable
import poa.simple.timeline.output.ColoredChar.Companion.BLACK

@Serializable
data class TimeRangeList(
    val color: String = BLACK,
    val list: List<@Serializable(with = TimeRangeSerializer::class) TimeRange>,
)