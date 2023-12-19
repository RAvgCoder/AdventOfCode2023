package HelperUtils

/**
 * Represents a 2D coordinate (x, y).
 * @property x The x-coordinate.
 * @property y The y-coordinate.
 */
class Coordinate2D(var x: Long, var y: Long) {

    /**
     * Gets the integerForm of a coordinate x pos
     */

    fun get_X(): Int {
        return x.toInt()
    }

    /**
     * Gets the integerForm of a coordinate y pos
     */
    fun get_Y(): Int {
        return y.toInt()
    }

    /**
     * Checks if the current coordinate is within the specified bounds.
     *
     * @param startX The starting x-coordinate of the bound.
     * @param startY The starting y-coordinate of the bound.
     * @param endX   The ending x-coordinate of the bound (exclusive).
     * @param endY   The ending y-coordinate of the bound (exclusive).
     * @return True if the coordinate is within the bounds, otherwise false.
     */
    fun isValid(startX: Long = 0, startY: Long = 0, endX: Long, endY: Long): Boolean {
        return (x in startX..<endX) && (y in startY..<endY)
    }

    /**
     * Moves the coordinate by the specified direction.
     *
     * @param direction The direction by which the coordinate should be moved.
     */
    fun offsetBy(direction: Direction) {
        x += direction.x
        y += direction.y
    }

    /**
     * Clones the current coordinate.
     *
     * @return A new instance of Coordinate2D with the same x and y values as the original.
     */
    fun clone(): Coordinate2D {
        return Coordinate2D(x, y)
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Coordinate2D)
            this.x == other.x && this.y == other.y
        else false
    }

    infix operator fun plus(dir: Direction): Coordinate2D {
        return Coordinate2D(this.x + dir.x, this.y + dir.y)
    }

    override fun toString(): String {
        return "Coordinate: [$x,$y]"
    }

    override fun hashCode(): Int {
        return toString().hashCode()
    }
}