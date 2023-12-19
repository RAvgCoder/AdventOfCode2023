package HelperUtils

/**
 * Enum representing directional movements in a 2D space.
 * Each direction has corresponding x and y offsets for movement.
 *
 *  This order must be preserved
 *
 *  NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST
 *
 *
 * @property x The x-coordinate offset for the direction.
 * @property y The y-coordinate offset for the direction.
 * @author Egbor Osebhulimen
 */

enum class Direction(val x: Int, val y: Int) {
    // Enum values for different directions with associated coordinate offsets
    NORTH(-1, 0),
    NORTH_EAST(-1, 1),
    EAST(0, 1),
    SOUTH_EAST(1, 1),
    SOUTH(1, 0),
    SOUTH_WEST(1, -1),
    WEST(0, -1),
    NORTH_WEST(-1, -1),
    STILL(0, 0); // Represents a still or no movement

    companion object {
        /**
         * Provides a list of the four cardinal directions: {NORTH, EAST, SOUTH, WEST}.
         */
        fun getDirNSEW() =
            entries.toTypedArray()
                .filterIndexed { i, _ -> i % 2 == 0 }
                .dropLast(1)
                .toTypedArray()

        /**
         * Provides a list of all directions except STILL.
         */
        fun getFullCoord() =
            entries
                .dropLast(1)
                .toTypedArray()
    }

    /**
     * Gets the next direction in sequence, considering either all directions or only cardinal directions.
     *
     * @param usingAllDirections Boolean indicating whether to consider all directions or only cardinal directions.
     * @return The next direction in sequence.
     */
    fun next(usingAllDirections: Boolean = false): Direction {
        return (if (usingAllDirections) getFullCoord() else getDirNSEW())
            .let { list ->
                list[(list.indexOf(this) + 1) % list.size]
            }
    }

    /**
     * Overrides the plus operator for adding directional offsets to a current position.
     *
     * @param currPos The current position as an array [x, y].
     * @return The new position after moving in this direction.
     */
    infix operator fun plus(currPos: IntArray): IntArray {
        return intArrayOf(this.x + currPos[0], this.y + currPos[1])
    }

    /**
     * Copies the current direction.
     *
     * @return A copy of the current direction.
     */
    fun copy(): Direction {
        return getFullCoord()[this.ordinal]
    }
}
