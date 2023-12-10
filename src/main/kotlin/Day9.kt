import MyArray.runPart
import MyArray.validate

/**
 * https://adventofcode.com/2023/day/9
 */
fun main() {
    // (partFunc, partNum, dayNum)
    runPart(::part1, 1, 9)
    runPart(::part2, 2, 9)
}

private fun part1(readFile: List<StringBuilder>) {
    val levels = setUp(readFile)

    val extrapolatedSum =
        levels.sumOf { level -> sumGenSeq(level) }

    println("The sum of the extrapolated values is $extrapolatedSum")
    validate(extrapolatedSum, 1834108701)
}

private fun part2(readFile: List<StringBuilder>) {
    val levels = setUp(readFile)

    val extrapolatedSum =
        levels.sumOf { level -> sumGenSeq(level.reversed()) }

    println("The sum of the extrapolated values is $extrapolatedSum")
    validate(extrapolatedSum, 993)
}


private fun sumGenSeq(sequence: List<Int>) : Int =
    if (sequence.any { it != 0 })
        sequence.last() + sumGenSeq(sequence.zipWithNext { a: Int, b: Int -> b - a})
    else 0

private fun setUp(readFile: List<StringBuilder>): List<List<Int>> =
    readFile.map { line ->
        line.toString().split("\\s+".toRegex()).map { it.toInt() }
    }

