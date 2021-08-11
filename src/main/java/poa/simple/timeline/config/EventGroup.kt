package poa.simple.timeline.config

import kotlinx.serialization.Serializable
import poa.simple.timeline.output.ColoredChar.Companion.BLACK

@Serializable
data class EventGroup(
    val color: String = BLACK,
    private val list: List<Event>,
) {
    val sortedList = list.sortedBy { it.from }
}