import MyArray.runPart

/**
 * https://adventofcode.com/2023/day/1
 */
fun main() {
    runPart(::part1, 1, 1)
    runPart(::part2, 2, 1)
}

private fun part1(lines: List<StringBuilder>) {
    println("Sum of all of the calibration values is ${
        lines.sumOf { str ->
            "${str.find { it.isDigit() }}${str.findLast { it.isDigit() }}".toInt()
        }
    }")
}

private fun part2(lines: List<StringBuilder>) {
    println("Sum of all of the calibration values is ${lines.sumOf { count(it) }}")
}

private val numMap = listOf(
    "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
)

private fun count(str: StringBuilder): Int {
    // Initialize count to a high value
    var count = Int.MAX_VALUE

    var firstNum = 0 // Initialize left-hand side number
    var lastNum = 0 // Initialize right-hand side number

    var idx: Int // Initialize index variable

    // Check for the first num in the string
    numMap.forEachIndexed { numVal, numStr ->
        // Find the index of numStr in the string
        idx = str.indexOf(numStr)

        // If the index is valid and within the current count limit
        if (idx >= 0 && idx in 0..count.coerceAtMost(idx)) {
            firstNum = (numVal + 1) // Update left-hand side number
        }

        // Update the count with the minimum of current count and index
        count = if (count.coerceAtMost(idx) == -1) count else count.coerceAtMost(idx)

        // Find the index of numVal in the string
        idx = str.indexOf("${numVal + 1}")

        // If the index is valid and within the current count limit
        if (idx >= 0 && idx in 0..count.coerceAtMost(idx)) {
            firstNum = (numVal + 1) // Update left-hand side number
        }

        // Update the count with the minimum of current count and index
        count = if (count.coerceAtMost(idx) == -1) count else count.coerceAtMost(idx)
    }

    // Reset count to a low value
    count = Int.MIN_VALUE

    // Check for the last num in the string
    numMap.forEachIndexed { numVal, numStr ->
        // Find the last index of numStr in the string
        idx = str.lastIndexOf(numStr)

        // If the count is less than the index and the index is valid
        if (count < count.coerceAtLeast(idx) && idx >= 0) {
            lastNum = (numVal + 1) // Update right-hand side number
        }

        // Update the count with the maximum of current count and index
        count = count.coerceAtLeast(idx)

        // Find the last index of numVal in the string
        idx = count.coerceAtLeast(str.lastIndexOf("${numVal + 1}"))

        // If the count is less than the index and the index are valid
        if (count < count.coerceAtLeast(idx) && idx >= 0) {
            lastNum = (numVal + 1) // Update right-hand side number
        }

        // Update the count with the maximum of current count and index
        count = count.coerceAtLeast(idx)
    }

    // Return the concatenated integers as a new integer
    return "$firstNum$lastNum".toInt()
}
