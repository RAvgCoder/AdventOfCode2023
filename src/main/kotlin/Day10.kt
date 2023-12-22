import HelperUtils.Direction
import HelperUtils.Utils.runPart
import HelperUtils.Utils.validate

/**
 * https://adventofcode.com/2023/day/10
 */
fun main() {
    // (partFunc, partNum, dayNum)
    runPart(::part1, 1, 10)
    runPart(::part2, 2, 10)
}


private val direction = Direction.getDirNSEW()

//private val visited: HashSet<String> = HashSet()

private fun part1(map: List<CharArray>) {
    val startingPoint = findS(map)
    var steps = 1


    var currentPos = startingPoint
    var prevPos = startingPoint

    var next = canMoveTo(currentPos, prevPos, map)!!
    currentPos = next + currentPos
    while (!startingPoint.contentEquals(currentPos)) {
        next = canMoveTo(currentPos, prevPos, map)!!
        prevPos = currentPos
        currentPos = next + currentPos
        steps++
    }

    steps /= 2

    validate("Num of steps along it takes to get from the starting to the farthest point is", steps, 6754)
}

fun minMax(currentPos: IntArray) {
    if (currentPos[0] > maxX) maxX = currentPos[0]
    if (currentPos[0] < minX) minX = currentPos[0]
    if (currentPos[1] > maxY) maxY = currentPos[1]
    if (currentPos[1] < minY) minY = currentPos[1]
}

private var minX = Int.MAX_VALUE
private var minY = Int.MAX_VALUE
private var maxX = Int.MIN_VALUE
private var maxY = Int.MIN_VALUE

private fun part2(map: List<CharArray>) {

    val newM = Array(map.size) { CharArray(map[0].size) { '_' } }

    val startingPoint = findS(map)
    var steps = 1


    var currentPos = startingPoint
    var prevPos = startingPoint
    newM[currentPos[0]][currentPos[1]] = 'X'

    var next = canMoveTo(currentPos, prevPos, map)!!
    currentPos = next + currentPos
    while (!startingPoint.contentEquals(currentPos)) {
//        println("Curr: ${currentPos.contentToString()} Next: $next")
        newM[currentPos[0]][currentPos[1]] = 'X'
        minMax(currentPos)
        next = canMoveTo(currentPos, prevPos, map)!!
        prevPos = currentPos
        currentPos = next + currentPos
        steps++
    }

    map.forEach { println(it.contentToString()) }
    println()
    newM.forEach { println(it.contentToString()) }

    println(
        """
        
        Bounds
        ---------
        Min: [$minX, $minY]
        Max: [$maxX, $maxY]
        
    """.trimIndent()
    )

    steps /= 2

    validate("Num of steps along it takes to get from the starting to the farthest point is", steps, 0)
}

private fun canMoveTo(currentPos: IntArray, prevPos: IntArray, map: List<CharArray>): Direction? {
    if (currentPos[0] !in map.indices || currentPos[1] !in map[0].indices)
        return null

    val south = Direction.SOUTH + currentPos
    val north = Direction.NORTH + currentPos
    val west = Direction.WEST + currentPos
    val east = Direction.EAST + currentPos
    return when (map[currentPos[0]][currentPos[1]]) {
        '|' -> {
            if (south.contentEquals(prevPos)) Direction.NORTH
            else if (north.contentEquals(prevPos)) Direction.SOUTH
            else null
        }

        '-' -> {
            if (west.contentEquals(prevPos)) Direction.EAST
            else if (east.contentEquals(prevPos)) Direction.WEST
            else null
        }

        'L' -> {
            if (north.contentEquals(prevPos)) Direction.EAST
            else if (east.contentEquals(prevPos)) Direction.NORTH
            else null
        }

        '7' -> {
            if (west.contentEquals(prevPos)) Direction.SOUTH
            else if (south.contentEquals(prevPos)) Direction.WEST
            else null
        }

        'J' -> {
            if (west.contentEquals(prevPos)) Direction.NORTH
            else if (north.contentEquals(prevPos)) Direction.WEST
            else null
        }

        'F' -> {
            if (south.contentEquals(prevPos)) Direction.EAST
            else if (east.contentEquals(prevPos)) Direction.SOUTH
            else null
        }

        '.' -> null

        'S' -> {
            for (dir in direction) {
                val next = canMoveTo(dir + currentPos, prevPos, map)
                next?.let { return dir }
            }
            throw IllegalStateException("Could not find any direction to move to from S: ${currentPos.contentToString()}")
        }

        else -> throw IllegalStateException("Direction from ${currentPos.contentToString()} to ${prevPos.contentToString()} can not be resolved")
    }
}

private fun findS(map: List<CharArray>): IntArray {
    for (i in map.indices) {
        for (j in map[0].indices) {
            if (map[i][j] == 'S') {
                return intArrayOf(i, j)
            }
        }
    }
    throw IllegalStateException("Could not find a starting point `S` in the map")
}
