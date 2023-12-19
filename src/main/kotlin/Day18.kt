import HelperUtils.Coordinate2D
import HelperUtils.Direction
import HelperUtils.Direction.EAST
import HelperUtils.Direction.WEST
import HelperUtils.Utils.runPart
import HelperUtils.Utils.validate

/**
 * https://adventofcode.com/2023/day/18
 */
fun main() {
    // (partFunc, partNum, dayNum)
    runPart(::part1, 1, 18)
    runPart(::part2, 2, 18)
}

// Used for part1
private const val MAP_SIZE: Long = 900
private const val START: Long = MAP_SIZE / 6
private const val EMPTY_SPACE = '.'
private const val BORDER = '#'
private const val FILLER = 'X'

private fun part1(rawFileData: List<StringBuilder>) {
    // Create the map
    val map = Array<CharArray>(MAP_SIZE.toInt()) { CharArray(MAP_SIZE.toInt()) { EMPTY_SPACE } }

    // Step 1: Set up borders on the map based on the provided input data

    val instructions = rawFileData.map { inst ->
        // Split the instruction and convert it into a pair
        inst.split(" ").let { split ->
            getDirection(split[0][0]) to split[1].toLong()
        }
    }

    setUpBorders(instructions, map)

    // Step 2: Fills in the bounds of a map.
    fillBounds(map)

    // Step 3: Calculate the total cubic meters of BORDER and FILLER characters in the map
    val cubicMeters = map.sumOf { line ->
        line.count { c ->
            c == BORDER || c == FILLER
        }
    }

    validate("The cubic meters of lava the dig plan could hold is", cubicMeters, 35244)
}


private fun part2(rawFileData: List<StringBuilder>) {
    val instructions = rawFileData.map { line ->
        // Split the instruction and convert it into a Triple
        val hex = line.split(" ")[2]
            .removePrefix("(")
            .removeSuffix(")")

        val distance = hex.substring(1..5)
            .toLong(radix = 16)
        val direction = getDirection(hex.last())
        direction to distance
    }

    // Generate points traversed
    val coordinate = Coordinate2D(START, START)

    // Process each instruction to determine movements and steps
    var numOfBoundaryPoints: Long = 0
    val boundaryPoints =
        instructions.foldIndexed(mutableListOf<Coordinate2D>()) { i, acc, (direction, step) ->
            if (i == 0) acc.add(coordinate.clone())
            // Apply steps in the specified direction & Record the position after movement
            numOfBoundaryPoints += step
            acc.add(coordinate.apply {
                this.x += (direction.x * step)
                this.y += (direction.y * step)
            }.clone())
            acc
        }.reversed()

    // Calculate area using the Shoelace theorem
    val shoeLaceArea = boundaryPoints.zipWithNext { point1: Coordinate2D, point2: Coordinate2D ->
        (point1.x * point2.y) - (point1.y * point2.x)
    }.sum() / 2

    println("BoundaryPoints: $numOfBoundaryPoints")
    println("ShoeLace theorem: $shoeLaceArea")
    println("Pick's theorem: ${(shoeLaceArea + 1 - (numOfBoundaryPoints / 2))}")

    /*
     *  Apply Pick's Theorem
     *  A = Area
     *  i = Points inside the area
     *  b = Num of boundary points
     *
     *  A = i + (b/2) - 1 => i = A + 1 - (b/2)
     */
    val cubicMeters = (shoeLaceArea + 1 - (numOfBoundaryPoints / 2)) + numOfBoundaryPoints

    validate("The cubic meters of lava the dig plan could hold is", cubicMeters, 85070763635666)
}

/**
 * Fills in the bounds of a map.
 *
 * @param map The 2D array representing the map.
 */
private fun fillBounds(map: Array<CharArray>) {
    var direction = EAST // Assuming EAST is the starting direction in the array
    val startCoord = getTopLeftBorder(map) // Obtains the coordinate of the top-left border
    val currCoord = startCoord.clone() // Clones the start coordinate
    println("${startCoord.x + 1}:${startCoord.y + 1}") // Prints the coordinates (adding 1 for human-readable indexing)

    // Move to the next coordinate in the current direction
    currCoord.offsetBy(direction)

    // Loop until reaching the starting coordinate again
    while (currCoord != startCoord) {/*
         * Colors the column if traveling in the East / West Direction
         *
         * This is done in cases where it reaches the end of a border without finishing the coloring
         * as represented here with 'X'
         * .................#######....
         * .................#~~~~~#....
         * .................#~~~~~#....
         * ...........#######~~~~~#....
         * ...........#~~~~~X~~~~~#....
         * ...........#~~~~~X~~~~~#....
         * ...........#~~~~~X~~~~~#....
         * ........####~~~~~X~~~~~#....
         * ....#####~~X~~~~~X~~~~~#....
         * ....#~~~~~~X~~~~~#######....
         * ....#~~~~~~X~~~~~#..........
         * ....#####~~X~~~~~#..........
         * ........####~~~~~#..........
         * ...........#######..........
         */
        colorColumn(direction, currCoord, map)

        // Check if movement in the current direction is not possible
        if (!canMove(currCoord, direction, map)) {
            // Determine the next direction to follow based on the current direction and map conditions
            direction = nextDir(direction, map, currCoord)
        }

        // Color the column in the updated direction if applicable
        colorColumn(direction, currCoord, map)

        // Move to the next coordinate in the updated direction
        currCoord.offsetBy(direction)
    }
}

/**
 * Colors a column in the map based on the specified direction.
 *
 * @param direction  The direction of the column (EAST or WEST).
 * @param currCoord  The current coordinate.
 * @param map        The 2D array representing the map.
 */
private fun colorColumn(
    direction: Direction, currCoord: Coordinate2D, map: Array<CharArray>
) {
    if (direction == EAST || direction == WEST) {
        val moving = direction.next() // Determines the next movement in the direction
        val tmpCoord = currCoord.clone() // Clones the current coordinate

        // Moves and colors the column until it cannot be colored anymore
        while (canColor(tmpCoord, moving, map)) {
            tmpCoord.offsetBy(moving) // Moves to the next coordinate in the direction
            writePosToMap(map, tmpCoord, FILLER) // Colors the coordinate on the map
        }
    }
}


/**
 * Determines the next direction to follow based on the current direction, map conditions, and current coordinate.
 *
 * @param currDir    The current direction.
 * @param map        The 2D array representing the map.
 * @param currCoord  The current coordinate.
 * @return The next direction to follow.
 */
fun nextDir(currDir: Direction, map: Array<CharArray>, currCoord: Coordinate2D): Direction {
    // Get the list of all directions (NORTH, SOUTH, EAST, WEST)
    return Direction.getDirNSEW().toMutableList().apply {
        // Remove the current direction and the opposite direction
        remove(currDir)
        remove(currDir.next().next())
    }.first { dir ->
        // Find the first direction in the remaining list where movement is possible
        canMove(currCoord, dir, map)
    }
}


/**
 * Finds the top-left border coordinate in the map.
 *
 * @param map The 2D array representing the map.
 * @return The top-left border coordinate.
 */
fun getTopLeftBorder(map: Array<CharArray>): Coordinate2D {
    // Find the y-coordinate of the first line that contains a border character
    val yCoord = map.indexOfFirst { line ->
        line.any { it == BORDER }
    }

    // Find the x-coordinate of the first border character in the line found above
    val xCoord = map[yCoord].indexOfFirst { it == BORDER }

    return Coordinate2D(yCoord.toLong(), xCoord.toLong())
}

/**
 * Sets up borders on a map based on provided raw file data.
 *
 * @param instructions The raw data obtained from a file.
 * @param map         The 2D array representing the map.
 */
private fun setUpBorders(
    instructions: List<Pair<Direction, Long>>, map: Array<CharArray>
) {
    // Starting coordinate at the center of the map
    val coordinate = Coordinate2D(START, START)

    // Process each instruction to determine movements and steps
    instructions.forEach { (direction, step) ->
        // Write the initial position to the map
        writePosToMap(map, coordinate)

        // Apply steps in the specified direction
        (0..<step).forEach { _ ->
            coordinate.offsetBy(direction)
            // Record the position after movement
            writePosToMap(map, coordinate)
        }
    }
}

private fun getDirection(char: Char): Direction {
    return when (char) {
        'U', '3' -> Direction.NORTH
        'D', '1' -> Direction.SOUTH
        'R', '0' -> EAST
        'L', '2' -> WEST
        else -> throw IllegalArgumentException("Cannot switch on $char")
    }
}


/**
 * Checks if a specific coordinate can be colored in a map based on the given direction.
 *
 * @param tmpCoord   The current coordinate.
 * @param direction  The direction in which to check.
 * @param map        The 2D array representing the map.
 * @return True if the coordinate can be colored, otherwise false.
 */
fun canColor(
    tmpCoord: Coordinate2D, direction: Direction, map: Array<CharArray>
): Boolean {
    // Calculate the next coordinate based on the direction
    val next = tmpCoord + direction

    // Check if it's not possible to move in the specified direction and the next cell is not a specific filler character
    return !canMove(tmpCoord, direction, map) && map[next.get_X()][next.get_Y()] != FILLER
}

/**
 * Checks if movement in a specific direction is possible from the current coordinate on the map.
 *
 * @param currCoord  The current coordinate.
 * @param direction  The direction in which to check for movement.
 * @param map        The 2D array representing the map.
 * @return True if movement is possible, otherwise false.
 */
fun canMove(
    currCoord: Coordinate2D, direction: Direction, map: Array<CharArray>
): Boolean {
    // Calculate the next coordinate based on the direction
    val next = currCoord + direction

    // Check if the cell in the next direction is a border
    return map[next.get_X()][next.get_Y()] == BORDER
}

/**
 * Writes the position to the map array based on the coordinate.
 *
 * @param map        The 2D array representing the map.
 * @param coordinate The coordinate to write on the map.
 * @param symbol     The symbol to write to the map
 */
private fun writePosToMap(map: Array<CharArray>, coordinate: Coordinate2D, symbol: Char = BORDER) {
    map[coordinate.get_X()][coordinate.get_Y()] = symbol
}
