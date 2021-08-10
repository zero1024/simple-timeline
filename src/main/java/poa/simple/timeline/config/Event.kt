package poa.simple.timeline.config

import kotlinx.serialization.Serializable
import org.valiktor.functions.isLessThan
import org.valiktor.validate
import poa.simple.timeline.*
import poa.simple.timeline.output.ColoredChar.Companion.BLACK
import java.time.LocalDate

@Serializable
data class Event(
    val code: String,
    val from: @Serializable(with = LocalDateSerializer::class) LocalDate,
    val till: @Serializable(with = NullableLocalDateSerializer::class) LocalDate? = null,
    val color: String = BLACK,
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
            " ${till.decimalYear().subtract(from.decimalYear())}y "
        } else {
            " ${now.decimalYear().subtract(from.decimalYear())} years "
        }
    }

    fun date(): String {
        return if (till != null) {
            " ${this.from.format(MM)}-${this.till.format(MM)} "
        } else {
            " ${this.from.format(DD_MMM)} "
        }
    }

    fun getMaxTextLength() = maxOf(name().length, duration().length, date().length)

}
