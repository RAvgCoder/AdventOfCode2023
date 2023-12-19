package HelperUtils

/**
 * Coordinate(x,y) | (i,j)
 */
class Coordinate2D(var x: Int, var y: Int) {

    fun isValid(startX: Int = 0, startY: Int = 0, endX: Int, endY: Int): Boolean {
        return (x in startX..<endX) && (y in startY..<endY)
    }

    fun offsetBy(direction: Direction) {
        x += direction.x
        y += direction.y
    }

    // ------------------------------------
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
        var result = x
        result = 31 * result + y
        return result
    }
}