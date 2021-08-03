package poa.simple.timeline.config

import org.valiktor.functions.isLessThan
import org.valiktor.validate
import poa.simple.timeline.decimalYear
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


