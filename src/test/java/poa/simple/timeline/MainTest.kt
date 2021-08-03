package poa.simple.timeline

import org.junit.jupiter.api.Test
import poa.simple.timeline.runner.main

class MainTest {

    @Test
    internal fun test() {
        main(arrayOf("src/test/resources/life.yml"))
    }


}