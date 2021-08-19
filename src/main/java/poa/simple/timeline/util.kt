package poa.simple.timeline

import poa.simple.timeline.config.Color
import poa.simple.timeline.output.ColoredChar
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

fun monthRange(from: LocalDate, till: LocalDate): String {
    return if (from.month == till.month) {
        " ${from.format(MMM)} "
    } else {
        " ${from.format(MMM)}-${till.format(MMM)} "
    }
}

fun Array<ColoredChar>.addText(text: String, offset: Int, color: Color) {
    for (i in text.indices) {
        this[i + offset] = ColoredChar(text[i], color)
    }
}

fun Array<ColoredChar>.addText(text: String, offset: Int, length: Int, color: Color) {

    val indent = (length - text.length) / 2

    val chars = Array(length) { ColoredChar('=', color) }
    chars[0] = ColoredChar('|', color)
    chars[chars.size - 1] = ColoredChar('|', color)

    chars.addText(text, indent, color)

    for ((idx, char) in chars.withIndex()) {
        this[idx + offset] = char
    }
}

fun lengthIsValid(length: Int, vararg texts: String): Boolean {
    for (t in texts) {
        if (length < t.length + 2)
            return false
    }
    return true
}

fun Array<ColoredChar>.adjust(fillChar: Char): Array<ColoredChar> {
    var prev: ColoredChar? = null
    for (idx in indices) {
        if (this[idx] == prev && this[idx].char == '|') {
            this[idx] = ColoredChar(fillChar, prev.color)
        }
        prev = this[idx]
    }
    return this
}
