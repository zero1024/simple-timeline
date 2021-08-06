package poa.simple.timeline.config

import kotlinx.serialization.*
import java.time.LocalDate

@Serializable
data class TimeLineConfig(
    val base: Base,
    val timeRanges: List<List<@Serializable(with = TimeRangeSerializer::class) TimeRange>>,
    val events: List<@Serializable(with = EventSerializer::class) Event>,
) {

    @Serializable
    data class Base(
        val type: String,
        val from: @Serializable(with = LocalDateSerializer::class) LocalDate,
        val till: @Serializable(with = LocalDateSerializer::class) LocalDate,
    )

}