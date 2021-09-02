package poa.simple.timeline.output

import poa.simple.timeline.config.BackgroundColor
import poa.simple.timeline.config.Color

data class ColoredChar(
    val char: Char,
    val color: Color = Color.BLACK,
    val backColor: BackgroundColor? = null,
    val isBold: Boolean = Character.isLetterOrDigit(char),
) {
    companion object {
        val EMPTY_CHAR = ColoredChar(' ')
    }
}