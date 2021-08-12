package poa.simple.timeline.config

import kotlinx.serialization.Serializable
import org.valiktor.functions.isLessThan
import org.valiktor.validate
import poa.simple.timeline.DD_MMM
import poa.simple.timeline.MMM
import poa.simple.timeline.duration
import poa.simple.timeline.now
import java.time.LocalDate

@Serializable
data class Event(
    val code: String,
    val from: @Serializable(with = LocalDateSerializer::class) LocalDate,
    val till: @Serializable(with = NullableLocalDateSerializer::class) LocalDate? = null,
    private val color: String? = null,
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
            " ${this.from.format(MMM)}-${this.till.format(MMM)} "
        } else {
            " ${this.from.format(DD_MMM)} "
        }
    }

    fun color(default: String) = color ?: default

    fun getMaxTextLength() = maxOf(name().length, duration().length, date().length)

}
