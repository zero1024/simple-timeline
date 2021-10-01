package poa.simple.timeline.config

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class TimeLineConfig(
    val base: Base,
    val timeRanges: List<TimeRangeList> = emptyList(),
    val eventGroups: List<EventGroup> = emptyList(),
    private val events: List<Event> = emptyList(),
) {


    val sortedEvents = events.sortedBy { it.from }

    @Serializable
    data class Base(
        val from: @Serializable(with = LocalDateSerializer::class) LocalDate,
        val till: @Serializable(with = LocalDateSerializer::class) LocalDate,
        val birthday: @Serializable(with = NullableLocalDateSerializer::class) LocalDate?,
    )

}