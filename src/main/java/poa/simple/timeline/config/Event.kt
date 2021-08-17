package poa.simple.timeline.config

import kotlinx.serialization.Serializable
import org.valiktor.functions.isLessThan
import org.valiktor.validate
import poa.simple.timeline.*
import java.time.LocalDate

@Serializable
data class Event(
    val code: String,
    val from: @Serializable(with = LocalDateSerializer::class) LocalDate,
    val till: @Serializable(with = NullableLocalDateSerializer::class) LocalDate? = null,
    private val color: Color? = null,
) {

    init {
        validate(this) {
            if (till != null)
                validate(Event::from).isLessThan(till)
        }
    }

    fun name() = " ${code.uppercase()} "

    fun duration(): String {
        return if (till != null) {
            duration(from, till)
        } else {
            duration(from, now)
        }
    }

    fun date(): String {
        return if (till != null) {
            monthRange(from, till)
        } else {
            " ${this.from.format(DD_MMM)} "
        }
    }

    fun color(default: Color) = color ?: default

    fun getMaxTextLength() = maxOf(name().length, duration().length, date().length)

}
