package HelperUtils

enum class Direction(val x: Int, val y: Int) {
    NORTH(-1, 0),
    NORTH_EAST(-1, 1),
    EAST(0, 1),
    SOUTH_EAST(1, 1),
    SOUTH(1, 0),
    SOUTH_WEST(1, -1),
    WEST(0, -1),
    NORTH_WEST(-1, -1);

    companion object {
        fun getDirNSEW() =
            Direction.entries.filterIndexed { i, _ -> i % 2 == 0 }

        fun getFullCoord() =
            Direction.entries

        fun fromIntArray(array: IntArray): Direction? {
            return entries.find { it.x == array[0] && it.y == array[1] }
        }

        fun fromIntArray(x: Int, y: Int): Direction? {
            return entries.find { it.x == x && it.y == y }
        }
    }


    infix operator fun plus(currPos: IntArray): IntArray {
        return intArrayOf(this.x + currPos[0], this.y + currPos[1])
    }

    fun copy(): Direction {
        return getFullCoord()[this.ordinal]
    }
}
