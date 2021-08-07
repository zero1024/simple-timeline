package poa.simple.timeline.config

import org.valiktor.functions.isLessThan
import org.valiktor.validate
import poa.simple.timeline.MM
import poa.simple.timeline.MM_YYYY
import poa.simple.timeline.decimalYear
import java.time.LocalDate

data class Event(val code: String, val from: LocalDate, val till: LocalDate?) {

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
            " 1d "
        }
    }

    fun date(): String {
        return if (till != null) {
            " ${this.from.format(MM)}-${this.till.format(MM)} "
        } else {
            " ${this.from.format(MM_YYYY)} "
        }
    }

    fun getMaxTextLength() = maxOf(name().length, duration().length, date().length)

}
