package poa.simple.timeline.config

import kotlinx.serialization.*
import java.time.LocalDate

@Serializable
data class TimeLineConfig(
    val base: Base,
    val timeRanges: List<TimeRangeList>,
    val eventGroups: List<EventGroup>,
    private val events: List<Event>,
) {

    val sortedEvents = events.sortedBy { it.from }

    @Serializable
    data class Base(
        val from: @Serializable(with = LocalDateSerializer::class) LocalDate,
        val till: @Serializable(with = LocalDateSerializer::class) LocalDate,
        val birthday: @Serializable(with = NullableLocalDateSerializer::class) LocalDate?,
    )

}