package HelperUtils

/**
 *
 *  Provides a way to traverse a 2D space
 *
 *  This order must be preserved
 *
 *  NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST
 *  @author Egbor Osebhulimen
 */
enum class Direction(val x: Int, val y: Int) {

    NORTH(-1, 0),
    NORTH_EAST(-1, 1),
    EAST(0, 1),
    SOUTH_EAST(1, 1),
    SOUTH(1, 0),
    SOUTH_WEST(1, -1),
    WEST(0, -1),
    NORTH_WEST(-1, -1),
    STILL(0, 0);

    companion object {
        /**
         * Provides a list of the four cardinal directions
         *
         * {NORTH, EAST, SOUTH, WEST}
         */
        fun getDirNSEW() =
            Direction.entries
                .filterIndexed { i, _ -> i % 2 == 0 }
                .dropLast(1)
                .toTypedArray()

        fun getFullCoord() =
            Direction.entries
                .dropLast(1)
                .toTypedArray()
    }


    fun next(fullDir: Boolean = false): Direction {
        return (if (fullDir) getFullCoord() else getDirNSEW())
            .let { list ->
                list[(list.indexOf(this) + 1) % list.size]
            }
    }

    infix operator fun plus(currPos: IntArray): IntArray {
        return intArrayOf(this.x + currPos[0], this.y + currPos[1])
    }

    fun copy(): Direction {
        return getFullCoord()[this.ordinal]
    }
}
