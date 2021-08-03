package poa.simple.timeline

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

fun LocalDate.decimalYear() =
    BigDecimal(this.year)
        .add(BigDecimal(this.dayOfYear).divide(BigDecimal(365), 1, RoundingMode.HALF_UP))

fun LocalDate.ceilYear(): Int {
    return this.decimalYear().setScale(0, RoundingMode.HALF_UP).intValueExact()
}