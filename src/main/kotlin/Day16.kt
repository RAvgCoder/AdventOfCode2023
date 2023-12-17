import HelperUtils.Coordinate2D
import HelperUtils.Direction
import HelperUtils.Direction.*
import HelperUtils.Utils.runPart
import HelperUtils.Utils.validate

/**
 * https://adventofcode.com/2023/day/16
 */
fun main() {
    // (partFunc, partNum, dayNum)
    runPart(::part1, 1, 16)
    runPart(::part2, 2, 16)
}

private fun part1(map: List<CharArray>) {
    val rays = mutableListOf(
        Ray(
            EAST,
            true,
            Coordinate2D(0, 0),
            mutableSetOf(),
        )
    )

    val energizedPaths = getNumOfEnergizedPaths(map, rays)

    println("The number of tiles ended up being energized is $energizedPaths")

    validate(energizedPaths, 8034)
}

private fun getNumOfEnergizedPaths(map: List<CharArray>, ray: MutableList<Ray>): Int {
    var rays = ray
    val allPaths = ray.first().path

    do {
        rays = rays.let { list ->
            list.mapNotNull { ray -> moveRay(ray, map) }
                .forEach { rays.add(it) }

            rays.filter { it.isAlive }.toMutableList()
        }
    } while (rays.isNotEmpty())

    val energizedPaths = allPaths.map {
        it.split("\\s+".toRegex())[1]
    }.toSet().size
    return energizedPaths
}

private fun part2(map: List<CharArray>) {
    val rays = mutableListOf<Ray>()

    // Rays for the left and right areas of the map
    map.forEachIndexed { index, _ ->
        rays.add(Ray(EAST, true, Coordinate2D(index, 0), mutableSetOf<String>()))
        rays.add(Ray(WEST, true, Coordinate2D(index, map[0].lastIndex), mutableSetOf<String>()))
    }

    // Rays for top and bottom areas of the map
    map[0].forEachIndexed { index, _ ->
        rays.add(Ray(SOUTH, true, Coordinate2D(0, index), mutableSetOf<String>()))
        rays.add(Ray(NORTH, true, Coordinate2D(map.lastIndex, index), mutableSetOf<String>()))
    }


    val energizedPaths = rays.maxOf { ray -> getNumOfEnergizedPaths(map, mutableListOf(ray)) }

    println("The number of tiles ended up being energized is $energizedPaths")


    validate(0, 0)
}

private fun moveRay(ray: Ray, map: List<CharArray>): Ray? {
    val coordinate = ray.coordinate.copy()
    val direction = ray.dir.copy()
    var extraRay: Ray? = null

    if (!coordinate.isValid(endX = map[0].size, endY = map.size)) {
        ray.isAlive = false
        return null
    }

    when (map[coordinate.x][coordinate.y]) {
        '.' -> ray.move()
        '>', '^', 'v', '<' -> ray.move()
        '-' -> {
            if (direction == EAST || direction == WEST) ray.move()
            else {
                extraRay = ray.clone()
                extraRay.apply { dir = EAST }.move()
                ray.apply { dir = WEST }.move()
            }
        }

        '|' -> {
            if (direction == NORTH || direction == SOUTH) ray.move()
            else {
                extraRay = ray.clone()
                extraRay.apply { dir = NORTH }.move()
                ray.apply { dir = SOUTH }.move()
            }
        }

        '\\' -> {
            ray.dir = when (direction) {
                WEST -> NORTH
                EAST -> SOUTH
                NORTH -> WEST
                SOUTH -> EAST
                else -> throw IllegalArgumentException("Ray cannot move diagonally ray=$ray")
            }
            ray.move()
        }

        '/' -> {
            ray.dir = when (direction) {
                WEST -> SOUTH
                EAST -> NORTH
                NORTH -> EAST
                SOUTH -> WEST
                else -> throw IllegalArgumentException("Ray cannot move diagonally ray=$ray")
            }
            ray.move()
        }

        else -> throw IllegalStateException("Illegal state ${map[coordinate.x][coordinate.y]}")
    }

//    placeSpot(direction, coordinate, map)

    return extraRay
}

private data class Ray(
    var dir: Direction,
    var isAlive: Boolean,
    val coordinate: Coordinate2D,
    val path: MutableSet<String>
) {
    fun move() {
        val hash = "$coordinate Direction=$dir"
        if (path.contains(hash)) isAlive = false
        else {
            path.add(hash)
            coordinate.move(dir)
        }
    }

    fun clone(): Ray {
        return Ray(
            dir.copy(),
            true,
            coordinate.copy(),
            path
        )
    }
}