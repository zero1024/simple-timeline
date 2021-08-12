package poa.simple.timeline

import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

val now = LocalDate.now()

val MMM_YYYY = DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH)
val MMM = DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH)
val DD_MMM = DateTimeFormatter.ofPattern("dd MMM", Locale.ENGLISH)

fun duration(from: LocalDate, till: LocalDate): String {
    val period = Period.between(from, till)
    return " ${period.toString().replace("P", "").lowercase()} "
}
