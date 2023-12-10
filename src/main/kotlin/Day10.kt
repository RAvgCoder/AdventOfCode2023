import HelperUtils.Direction
import HelperUtils.Utils.runPart
import HelperUtils.Utils.validate

/**
 * https://adventofcode.com/2023/day/10
 */
fun main() {
    // (partFunc, partNum, dayNum)
    runPart(::part1, 1,10)
    runPart(::part2, 2, 10)
}


private val direction = Direction.getDirNSEW()

//private val visited: HashSet<String> = HashSet()

private fun part1(readFile: List<StringBuilder>) {
    val map: Array<CharArray> = setUP(readFile)

    val startingPoint = findS(map)
    var steps = 1


    var currentPos = startingPoint
    var prevPos = startingPoint

    var next = canMoveTo(currentPos, prevPos,map)!!
    currentPos = next + currentPos
    while (!startingPoint.contentEquals(currentPos)) {
//        println("Curr: ${currentPos.contentToString()} Next: $next")
        next = canMoveTo(currentPos, prevPos,map)!!
        prevPos = currentPos
        currentPos = next + currentPos
        steps++
    }

    steps /= 2

    validate(steps, 6754)
    println(
        "The num of steps along the loop it takes to get from the starting position to the point farthest from the starting position is $steps"
    )
}

private fun part2(readFile: List<StringBuilder>) {
    validate(0, 0)
}

fun canMoveTo(currentPos: IntArray, prevPos: IntArray, map: Array<CharArray>): Direction? {

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
                val next = canMoveTo(dir + currentPos, prevPos,map)
                next?.let { return dir }
            }
            throw IllegalStateException("Could not find any direction to move to from S: ${currentPos.contentToString()}")
        }

        else -> throw IllegalStateException("Direction from ${currentPos.contentToString()} to ${prevPos.contentToString()} can not be resolved")
    }
}

fun findS(map: Array<CharArray>): IntArray {
    for (i in map.indices) {
        for (j in map.indices) {
            if (map[i][j] == 'S') {
                return intArrayOf(i, j)
            }
        }
    }
    throw IllegalStateException("Could not find a starting point `S` in the map")
}

fun setUP(map: List<StringBuilder>): Array<CharArray> {
    return map.map {
        it.toString().toCharArray()
    }.toTypedArray()
}
