import HelperUtils.Utils.runPart
import HelperUtils.Utils.validate

/**
 * https://adventofcode.com/2023/day/6
 */
fun main() {
    runPart(::part1, 1, 6)
    runPart(::part2, 2, 6)
}

private fun part1(readFile: List<StringBuilder>) {

    val waysToBeatRecord = readFile
        .map { line ->
            line.split("\\s+".toRegex())
                .drop(1)
                .map { it.toInt() }
        }
        .let { it[0].zip(it[1]) }
        .map { (waitTime, distance) ->
            (1..waitTime)
                .first { (waitTime - it) * it > distance }
                .let { firstWin -> (waitTime - firstWin) - firstWin + 1 }
        }.fold(1, Long::times)

    validate("Number of ways you can beat the record is", waysToBeatRecord, 219849)
}

private fun part2(readFile: List<StringBuilder>) {

    val waysToBeatRecord = readFile
        .map { line ->
            line.split("\\s+".toRegex())
                .drop(1)
                .joinToString(separator = "")
                .toLong()
        }
        .let { timeDistance ->
            val waitTime = timeDistance[0]
            val distance = timeDistance[1]
            (1..waitTime)
                .first { (waitTime - it) * it > distance }
                .let { firstWin -> (waitTime - firstWin) - firstWin + 1 }
        }

    validate("Number of ways you can beat the record is", waysToBeatRecord, 29432455)
}