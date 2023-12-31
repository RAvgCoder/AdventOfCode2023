import HelperUtils.Utils.runPart
import HelperUtils.Utils.validate

/**
 * https://adventofcode.com/2023/day/2
 */
fun main() {
    runPart(::part1, 1, 2)
    runPart(::part2, 2, 2)
}

private fun part1(lines: List<StringBuilder>) {
    // Calculate the sum based on specific conditions within a collection of lines
    val sumOfIds = lines.sumOf { round ->
        // Split each line into segments using delimiters ";", or ":"
        val segment = round.split("[;:]".toRegex())

        // Check if all sublines within each segment meet certain criteria
        val isValid = segment.drop(1).all { subLine ->
            // Find all occurrences that match a pattern: a number followed by a color name
            Regex("\\d+\\s*[a-zA-Z]+").findAll(subLine).all { item ->
                // Extract the numeric value and color name from the matched pattern
                val colorNum = item.value.split("\\s+".toRegex())[0].toInt()
                val colorName = item.value.split("\\s+".toRegex())[1]

                // Verify conditions based on color names
                when (colorName) {
                    "red" -> colorNum <= 12
                    "green" -> colorNum <= 13
                    "blue" -> colorNum <= 14
                    // Unexpected color name encountered; throw an exception
                    else -> throw IllegalStateException(
                        "Error parsing item $item in subLine $subLine existing in segment $segment"
                    )
                }
            }
        }

        // If all conditions are met, extract a specific value from the first segment; otherwise, use 0
        if (isValid) segment[0].split(" ")[1].toInt()
        else 0
    }

    validate("Sum of the IDs of those games is", sumOfIds, 2551)
}

private fun part2(lines: List<StringBuilder>) {
    // Calculate the sum of cube powers for each round
    val sumOfPowerSets = lines.sumOf { round ->
        // Split the round into segments using delimiters ";" or ":"
        val segments = round.split("[;:]".toRegex())

        // Initialize counts for red, green, and blue cubes
        var red = 1
        var green = 1
        var blue = 1

        // Iterate through segments excluding the first one
        segments.drop(1).forEach { subLine ->
            // Find all occurrences of the pattern: a number followed by a color name
            Regex("\\d+\\s*[a-zA-Z]+").findAll(subLine).forEach { item ->
                // Extract the numeric value and color name from the matched pattern
                val colorNum = item.value.split("\\s+".toRegex())[0].toInt()
                val colorName = item.value.split("\\s+".toRegex())[1]

                // Update the count for each color to the maximum of the existing count and the current number
                when (colorName) {
                    "red" -> red = red.coerceAtLeast(colorNum)
                    "green" -> green = green.coerceAtLeast(colorNum)
                    "blue" -> blue = blue.coerceAtLeast(colorNum)
                    // Unexpected color name encountered; throw an exception
                    else -> throw IllegalStateException(
                        "Error parsing item $item in subLine $subLine existing in segment $segments"
                    )
                }
            }
        }

        // Calculate and return the product of red, green, and blue cubes
        red * green * blue
    }
    validate("sum of the power of these sets is", sumOfPowerSets, 62811)
}