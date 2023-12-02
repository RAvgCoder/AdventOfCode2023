fun main() {
    part1()
    part2()
}

private fun part1() {
    println(
        MyArray.readFile(1, 1)
            .sumOf {
                str -> "${str.find { it.isDigit() }}${str.findLast { it.isDigit() }}".toInt()
            }
    )
}

private val numMap = mapOf(
    "one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5, "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9
)

private fun part2() {
    println(
        MyArray.readFile(1, 2)
            .map(::count)
            .sum()
    )
}


private fun count(str: String): Int {
    // Initialize count to a high value
    var count = Int.MAX_VALUE

    var firstNum = 0 // Initialize left-hand side number
    var lastNum = 0 // Initialize right-hand side number

    var idx: Int // Initialize index variable

    // Check for the first num in the string
    numMap.forEach { (numStr, numVal) ->
        // Find the index of numStr in the string
        idx = str.indexOf(numStr)

        // If the index is valid and within the current count limit
        if (idx >= 0 && idx in 0..count.coerceAtMost(idx)) {
            firstNum = numMap[numStr]!! // Update left-hand side number
        }

        // Update the count with the minimum of current count and index
        count = if (count.coerceAtMost(idx) == -1) count else count.coerceAtMost(idx)

        // Find the index of numVal in the string
        idx = str.indexOf("$numVal")

        // If the index is valid and within the current count limit
        if (idx >= 0 && idx in 0..count.coerceAtMost(idx)) {
            firstNum = numVal // Update left-hand side number
        }

        // Update the count with the minimum of current count and index
        count = if (count.coerceAtMost(idx) == -1) count else count.coerceAtMost(idx)
    }

    // Reset count to a low value
    count = Int.MIN_VALUE

    // Check for the last num in the string
    numMap.forEach { (numStr, numVal) ->
        // Find the last index of numStr in the string
        idx = str.lastIndexOf(numStr)

        // If the count is less than the index and the index is valid
        if (count < count.coerceAtLeast(idx) && idx >= 0) {
            lastNum = numMap[numStr]!! // Update right-hand side number
        }

        // Update the count with the maximum of current count and index
        count = count.coerceAtLeast(idx)

        // Find the last index of numVal in the string
        idx = count.coerceAtLeast(str.lastIndexOf("$numVal"))

        // If the count is less than the index and the index is valid
        if (count < count.coerceAtLeast(idx) && idx >= 0) {
            lastNum = numVal // Update right-hand side number
        }

        // Update the count with the maximum of current count and index
        count = count.coerceAtLeast(idx)
    }

    // Return the concatenated integers as a new integer
    return "$firstNum$lastNum".toInt()
}
