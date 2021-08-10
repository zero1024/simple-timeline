package poa.simple.timeline.config

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import poa.simple.timeline.now
import java.time.LocalDate


/**
 * example: someCode.2006.9.1-2011.6.21
 */
class TimeRangeSerializer : KSerializer<TimeRange> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("TIME_RANGE", PrimitiveKind.STRING)
    private val pattern =
        ("(?<code>[a-zA-Z0-9_]*)\\." +
                "(?<from>[0-9.]*)" +
                "-" +
                "(?<till>[0-9.]*)")
            .toRegex()

    override fun deserialize(decoder: Decoder): TimeRange {
        val str = decoder.decodeString()
        val groups = pattern.find(str)!!.groups

        return TimeRange(
            code = groups.getStr("code"),
            from = parseLocalDate(groups.getStr("from"))!!,
            till = parseLocalDate(groups.getStr("till"))!!
        )
    }

    override fun serialize(encoder: Encoder, value: TimeRange) {
    }
}

class LocalDateSerializer : KSerializer<LocalDate> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LOCAL_DATE", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalDate {
        return parseLocalDate(decoder.decodeString())!!
    }

    override fun serialize(encoder: Encoder, value: LocalDate) {
    }
}

class NullableLocalDateSerializer : KSerializer<LocalDate?> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LOCAL_DATE", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalDate? {
        return parseLocalDate(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: LocalDate?) {
    }
}

private fun MatchGroupCollection.getStr(name: String) = this[name]!!.value

private fun parseLocalDate(str: String): LocalDate? {
    if (str.isEmpty()) {
        return null
    }
    val strArray = str.split(".")
    val year = strArray[0].toInt()
    if (year == 9999) {
        return now
    }
    var month = 1
    var day = 1
    if (strArray.size > 1) {
        month = strArray[1].toInt()
    }
    if (strArray.size > 2) {
        day = strArray[2].toInt()
    }
    return LocalDate.of(year, month, day)
}
