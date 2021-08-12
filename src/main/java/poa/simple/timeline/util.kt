package poa.simple.timeline

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

val now = LocalDate.now()

val MM_YYYY = DateTimeFormatter.ofPattern("MM.yyyy")
val MM = DateTimeFormatter.ofPattern("MM")
val DD_MMM = DateTimeFormatter.ofPattern("dd MMM")

fun duration(from: LocalDate, till: LocalDate): String {
    val period = Period.between(from, till)
    return " ${period.toString().replace("P", "").lowercase()} "
}
