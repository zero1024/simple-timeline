package poa.simple.timeline.config

import kotlinx.serialization.Serializable

@Serializable
data class EventGroup(
    val color: Color = Color.BLACK,
    private val list: List<Event>,
) {
    val sortedList = list.sortedBy { it.from }
}