package poa.simple.timeline.config

import org.valiktor.functions.isLessThan
import org.valiktor.validate
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

data class TimeRange(val code: String, val from: LocalDate, val till: LocalDate) {

    init {
        validate(this) {
            validate(TimeRange::from).isLessThan(till)
        }
    }

    fun name() = code.uppercase()
    fun yearsDuration() = till.decimalYear().subtract(from.decimalYear())

}

private fun LocalDate.decimalYear() =
    BigDecimal(this.year).add(BigDecimal(this.monthValue).divide(BigDecimal(12), 1, RoundingMode.HALF_UP))
