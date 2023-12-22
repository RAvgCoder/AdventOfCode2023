import HelperUtils.Coordinate2D
import HelperUtils.Direction
import HelperUtils.Utils.runPart
import HelperUtils.Utils.validate
import java.util.*
import kotlin.math.abs

/**
 * https://adventofcode.com/2023/day/21
 */
fun main() {
    // (partFunc, partNum, dayNum)
    runPart(::part1, 1, 21)
    runPart(::part2, 2, 21)
}

private val directions = Direction.getDirNSEW()
private fun part1(map: List<CharArray>) {
    val start = findS(map)

    var coordinateList = listOf(start)

    val STEPS = 64
    repeat(STEPS) {
        val newElem = mutableListOf<Coordinate2D>()
        coordinateList.forEach { coord ->
            newElem.addAll(nextSteps(coord, map))
        }
        coordinateList = newElem.distinct()
    }

    validate("The number of garden plots could the Elf reach in exactly $STEPS steps is ", coordinateList.size, 3699)
}

private fun part2(map: List<CharArray>) {
    val coordinateStepMap = createStepMap(map)



    var start = findS(map)
    val STEPS = 26501365

//    validate("The number of garden plots could the Elf reach in exactly $STEPS steps is ", coordinateQueue.size, 16)
}

fun createStepMap(map: List<CharArray>): Map<String, List<Coordinate2D>> {
    val stepMap = mutableMapOf<String, List<Coordinate2D>>()
    for (i in map.indices) {
        for (j in map[0].indices) {
            val start = Coordinate2D(i, j)
            // If it's placed on a '#'
            if (!canMove(start, map)) continue

            val coordinateQueue = LinkedList(listOf(start))

            val step: Long = 55
            for (s in 1..step) {
                val newElem = mutableListOf<Coordinate2D>()
                coordinateQueue.forEach { coord ->
                    newElem.addAll(nextSteps(coord, map))
                }

                coordinateQueue.clear()
                coordinateQueue.addAll(newElem.distinct())

                // If any coord leaves the map after one frame of simulation, then stop
                if (newElem.isEmpty() ||
                    newElem.any { !it.isValid(endY = map.size, endX = map[0].size) }
                ) {
                    break
                }
            }

            stepMap[start.toString()] = coordinateQueue.toList()
        }
    }
    return stepMap
}

fun nextSteps(coord: Coordinate2D, map: List<CharArray>): Collection<Coordinate2D> {
    val newCoords = mutableListOf<Coordinate2D>()
    directions.forEach { dir ->
        val newCoord = coord.clone()
        newCoord.offsetBy(dir)
        if (canMove(newCoord, map)) {
            newCoords.add(newCoord)
        }
    }
    return newCoords
}

private fun canMove(coord: Coordinate2D, map: List<CharArray>): Boolean {
    return map[resolve(coord.get_X(), map.size)][resolve(coord.get_Y(), map.size)] != '#'
}

fun resolve(xORyCord: Int, size: Int): Int {
    return if (xORyCord < 0) {
        (size - 1) - ((abs(xORyCord) - 1) % size)
    } else {
        xORyCord % size
    }
}


private fun drawMap(map: List<CharArray>, coordinateSet: Queue<Coordinate2D>) {
    val new = mutableListOf<CharArray>()
    map.forEach { line ->
        val x = CharArray(line.size)
        System.arraycopy(line, 0, x, 0, line.size)
        new.add(x)
    }

    coordinateSet.forEach { (x, y) ->
        new[x.toInt()][y.toInt()] = 'O'
    }

    println("-".repeat(20))
    new.forEach { println(it) }
    println("-".repeat(20))
    println()
}

private fun findS(map: List<CharArray>): Coordinate2D {
    for (i in map.indices) {
        for (j in map[0].indices) {
            if (map[i][j] == 'S') {
                return Coordinate2D(i, j)
            }
        }
    }
    throw IllegalStateException("Could not find a starting point `S` in the map")
}
