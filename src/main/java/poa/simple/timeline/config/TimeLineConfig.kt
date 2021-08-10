package poa.simple.timeline.config

import kotlinx.serialization.*
import java.time.LocalDate

@Serializable
data class TimeLineConfig(
    val base: Base,
    val timeRanges: List<TimeRangeList>,
    val events: List<@Serializable(with = EventSerializer::class) Event>,
) {

    @Serializable
    data class Base(
        val from: @Serializable(with = LocalDateSerializer::class) LocalDate,
        val till: @Serializable(with = LocalDateSerializer::class) LocalDate,
        val birthday: @Serializable(with = NullableLocalDateSerializer::class) LocalDate?,
    )

}