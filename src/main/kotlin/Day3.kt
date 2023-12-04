import kotlin.time.measureTimedValue

fun main() {
    val (_, time1) = measureTimedValue {
        part1(MyArray.readFile(3))
    }
    println("Time taken for part1 is $time1")

    println()

    val (_, time2) = measureTimedValue {
        part2(MyArray.readFile(3))
    }
    println("Time taken for part2 is $time2")
}

private fun part1(readFile: List<String>) {
    var sum = 0 // Initialize the sum of part numbers
    var currNumber = 0 // Initialize the current number being processed
    val startingCoordinates = arrayOf(0, 0) // Array to store starting coordinates

    // Loop through each line in the 'readFile'
    for (i in readFile.indices) {
        for (j in readFile[i].indices) {
            val curr = readFile[i][j] // Get the current character from the file

            // If a number is being built and the row changes
            if (currNumber != 0 && startingCoordinates[0] != i) {
                // Check if the part number is valid and add it to the sum if valid
                if (isValidPart(readFile, startingCoordinates, currNumber)) sum += currNumber

                // Reset the current number and update coordinates if the current character is a digit
                currNumber = 0
                if (curr.isDigit()) {
                    currNumber += curr.digitToInt()
                    startingCoordinates[0] = i
                    startingCoordinates[1] = j
                }
            } else if (!curr.isDigit() && currNumber != 0) {
                // If encountering a non-digit and a number is being built, check and add to the sum if valid
                if (isValidPart(readFile, startingCoordinates, currNumber)) sum += currNumber

                // Reset the current number
                currNumber = 0
            } else if (curr.isDigit()) {
                // If encountering a digit, continue building the number
                if (currNumber == 0) {
                    startingCoordinates[0] = i
                    startingCoordinates[1] = j
                }
                currNumber *= 10
                currNumber += curr.digitToInt()
            }
        }
    }

    println("Sum of all of the part numbers in the engine schematic is $sum")
}

private fun isValidPart(readFile: List<String>, pos: Array<Int>, currNumber: Int): Boolean {
    // Calculate the number of digits in the current number
    val numDigits = currNumber.toString().length

    // Calculate new starting position for checking adjacent characters
    val newPos = arrayOf(pos[0] - 1, pos[1] - 1)
    val xBound = newPos[1] + (numDigits + 1) // Calculate the x-bound for checking
    val yBound = newPos[0] + 2 // Calculate the y-bound for checking

    // Loop through the adjacent characters around the current position
    for (i in newPos[0]..yBound) {
        for (j in newPos[1]..xBound) {
            // Continue if the indices are out of bounds of the readFile
            if (i !in readFile.indices || j !in readFile[0].indices) continue

            // Check if the character is not a digit and not a dot
            if (!readFile[i][j].isDigit() && readFile[i][j] != '.') {
                return true // If a non-digit, non-dot character is found, consider the part valid
            }
        }
    }

    return false // If no non-digit, non-dot character is found, consider the part invalid
}

private fun part2(input: List<String>) {
    // Convert the input to a mutable list of strings
    val readFile = input.toMutableList()

    var sum = 0 // Initialize a sum variable to accumulate results

    // Iterate through each string in the list
    for (i in readFile.indices) {
        // Find the index of the first '*' character in the current string
        var starPos = readFile[i].indexOfFirst { it == '*' }

        // Continue processing '*' characters in the current string until none are left
        while (starPos != -1) {
            sum += findGears(i, starPos, readFile)

            // Replace the '*' character with a '.' character in the current string
            readFile[i] = StringBuilder(readFile[i]).apply { set(starPos, '.') }.toString()

            // Find the index of the next '*' character in the current string
            starPos = readFile[i].indexOfFirst { it == '*' }
        }
    }

    println("Gear ratios produced $sum")
}

val directions = arrayOf(
    intArrayOf(-1, -1),
    intArrayOf(-1, 0),
    intArrayOf(-1, 1),
    intArrayOf(0, 1),
    intArrayOf(1, 1),
    intArrayOf(1, 0),
    intArrayOf(1, -1),
    intArrayOf(0, -1),
)

fun findGears(yCoord: Int, xCoord: Int, readFile: List<String>): Int {
    val gears: MutableList<Triple<Int, IntRange, Int>> = mutableListOf()

    for (dir in directions) {
        // Calculate the new coordinates based on the direction
        val yOffset = yCoord + dir[0]
        val xOffset = xCoord + dir[1]

        if (yOffset !in readFile.indices || xOffset !in readFile[0].indices) continue

        // Check if the character at the new coordinates is a digit (indicating a gear)
        if (readFile[yOffset][xOffset].isDigit()) {
            // If it's a digit, check if it forms a range with other gears already found
            if (isRange(gears, xOffset, yOffset)) continue

            // Extract the integer value and its range from the string
            val pair = extractInt(xOffset, readFile[yOffset])

            // Store information about the gear (value, range, and y-coordinate)
            gears.add(Triple(pair.first, pair.second, yOffset))
        }
    }

    // If two gears are found (indicating a pair), return their product; otherwise, return 0
    return if (gears.size == 2) gears[0].first * gears[1].first else 0
}


fun isRange(gears: MutableList<Triple<Int, IntRange, Int>>, xOffset: Int, yOffset: Int): Boolean {
    for ((_, range, y) in gears) {
        if (y != yOffset) continue
        if (xOffset in range) return true
    }
    return false
}

fun extractInt(xOffset: Int, str: String): Pair<Int, IntRange> {
    // Initialize left and right pointers and characters with the starting offset
    var l = xOffset
    var r = xOffset
    var lC = str[l]
    var rC = str[r]

    // Continue while either left or right characters are digits
    while (lC.isDigit() || rC.isDigit()) {
        // Expand the range to the right if the next character is a digit and within bounds
        if (str.length > r + 1 && str[r + 1].isDigit()) {
            r++
        }
        // Expand the range to the left if the previous character is a digit and within bounds
        else if (l - 1 >= 0 && str[l - 1].isDigit()) {
            l--
        }

        lC = if (l - 1 >= 0) str[l - 1] else '.'  // Get the character left to the current range
        rC = if (str.length > r + 1) str[r + 1] else '.'  // Get the character right to the current range
    }

    // Create an IntRange from left to right to represent the range of digits
    val range = IntRange(l, r)

    // Extract the substring within the determined range and convert it to an integer
    val num = str.substring(range).toInt()

    return Pair(num, range)
}
