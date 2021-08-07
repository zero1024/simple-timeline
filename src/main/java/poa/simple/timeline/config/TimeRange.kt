package poa.simple.timeline.config

import org.valiktor.functions.isLessThan
import org.valiktor.validate
import poa.simple.timeline.MM
import poa.simple.timeline.MM_YYYY
import poa.simple.timeline.decimalYear
import java.time.LocalDate

data class TimeRange(val code: String, val from: LocalDate, val till: LocalDate) {

    init {
        validate(this) {
            validate(TimeRange::from).isLessThan(till)
        }
    }

    fun name() = code.uppercase()

    fun duration() = " ${till.decimalYear().subtract(from.decimalYear())} years "

    fun date() = " ${this.from.format(MM_YYYY)}-${this.till.format(MM_YYYY)} "

    fun shortName() = name().take(5)

    fun shortDuration() = " ${till.decimalYear().subtract(from.decimalYear())}y "

    fun shortDate() = " ${this.from.format(MM)}-${this.till.format(MM)} "


}


