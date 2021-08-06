package poa.simple.timeline.config

import org.valiktor.functions.isLessThan
import org.valiktor.validate
import java.time.LocalDate

data class Event(val code: String, val from: LocalDate, val till: LocalDate?) {

    init {
        validate(this) {
            if (till != null)
                validate(Event::from).isLessThan(till)
        }
    }

    fun name() = code.uppercase()

}


