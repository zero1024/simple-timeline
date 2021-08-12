package poa.simple.timeline.config

import org.valiktor.functions.isLessThan
import org.valiktor.validate
import poa.simple.timeline.MMM
import poa.simple.timeline.MMM_YYYY
import poa.simple.timeline.duration
import java.time.LocalDate

data class TimeRange(val code: String, val from: LocalDate, val till: LocalDate) {

    init {
        validate(this) {
            validate(TimeRange::from).isLessThan(till)
        }
    }

    private fun name() = code.uppercase()

    private fun date() = " ${this.from.format(MMM_YYYY)} - ${this.till.format(MMM_YYYY)} "

    private fun shortName() = name().take(5)

    private fun shortDate() = " ${this.from.format(MMM)}-${this.till.format(MMM)} "

    fun name(condition: (String) -> Boolean) = if (condition(name())) name() else shortName()

    fun date(condition: (String) -> Boolean) = if (condition(date())) date() else shortDate()

    fun duration(): String = duration(from, till)

}


