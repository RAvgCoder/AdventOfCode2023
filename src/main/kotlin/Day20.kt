import HelperUtils.Utils.runPart
import HelperUtils.Utils.validate

/**
 * https://adventofcode.com/2023/day/20
 */
fun main() {
    // (partFunc, partNum, dayNum)
    runPart(::part1, 1, )
    runPart(::part2, 2, 20)
}

private fun part1(readFile: List<StringBuilder>) {

    validate("", 0, 0)
}

private fun part2(readFile: List<StringBuilder>) {
    validate("", 0, 0)
}

sealed class Modules {
    class FLipFlop(val name: String) : Modules() {
        var pulse: Int = 0
        private var isJammed: Boolean = false
        val connection: MutableList<Module> = mutableListOf()

        fun sendPulse(pulse: Int): Int {
            if (pulse == 1) {
                isJammed = false
                this.pulse = this.pulse xor 1
            } else isJammed = true

            return if (isJammed) -1 else this.pulse
        }
    }

    class Conjunction(val name: String) : Modules()
    data class BroadcastModule(val list: List<Module>) : Modules()
}