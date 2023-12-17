package HelperUtils

class Coordinate2D(var x: Int, var y: Int) {
    fun move(direction: Direction) {
        x += direction.x
        y += direction.y
    }

    fun isValid(startX: Int = 0, startY: Int = 0, endX: Int, endY: Int): Boolean {
        return (x in startX..<endX) && (y in startY..<endY)
    }

    infix operator fun plus(currPos: Direction): Coordinate2D {
        return Coordinate2D(this.x + currPos.x, this.y + currPos.x)
    }

    override fun toString(): String {
        return "Coordinate: [$x,$y]"
    }

    fun copy(): Coordinate2D {
        return Coordinate2D(x,y)
    }
}