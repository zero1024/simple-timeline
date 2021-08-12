package poa.simple.timeline.config

import org.valiktor.functions.isLessThan
import org.valiktor.validate
import poa.simple.timeline.MM
import poa.simple.timeline.MM_YYYY
import poa.simple.timeline.duration
import java.time.LocalDate

data class TimeRange(val code: String, val from: LocalDate, val till: LocalDate) {

    init {
        validate(this) {
            validate(TimeRange::from).isLessThan(till)
        }
    }

    private fun name() = code.uppercase()

    private fun date() = " ${this.from.format(MM_YYYY)}-${this.till.format(MM_YYYY)} "

    private fun shortName() = name().take(5)

    private fun shortDate() = " ${this.from.format(MM)}-${this.till.format(MM)} "

    fun name(condition: (String) -> Boolean) = if (condition(name())) name() else shortName()

    fun date(condition: (String) -> Boolean) = if (condition(date())) date() else shortDate()

    fun duration(): String = duration(from, till)

}


