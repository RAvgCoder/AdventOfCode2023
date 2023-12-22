import HelperUtils.Coordinate2D
import HelperUtils.Direction
import HelperUtils.Utils.runPart
import HelperUtils.Utils.validate
import java.util.*

/**
 * https://adventofcode.com/2023/day/17
 */
fun main() {
    // (partFunc, partNum, dayNum)
    runPart(::part1, 1)
    runPart(::part2, 2, 17)
}

private fun part1(map: List<IntArray>) {
    map.onEach { println(it.contentToString()) }

    val heatMap = createHeatMap(map)

//    heatMap.onEach { println(it.contentToString()) }

    validate("The least heat loss it can incur is", 0, 102)
}

private fun createHeatMap(map: List<IntArray>): Array<Array<HeatNodeMap>> {
    val heatMap = Array<Array<HeatNodeMap>>(map.size) { arrayOf() }

    map.forEachIndexed { i, arr ->
        val muList = mutableListOf<HeatNodeMap>()
        arr.forEachIndexed { j, heatNum ->
            muList.add(HeatNodeMap(heatNum, Coordinate2D(i, j)))
        }
        heatMap[i] = muList.toTypedArray()
    }

    getShortestPath(heatMap)

    return heatMap
}

private fun getShortestPath(heatMap: Array<Array<HeatNodeMap>>): StringBuilder {
    val path = StringBuilder()
    val pathList = PriorityQueue(compareBy<HeatNodeMap> { it.cost })
    val foundList = mutableListOf<HeatNodeMap>()
    var curr = heatMap[0].first().apply { cost = 0 }
    val des = heatMap[heatMap.lastIndex].last()
//    var direction = EAST

    pathList.add(curr)
    while (pathList.isNotEmpty()) {
        curr = pathList.remove()
        val neighbours = getNeighbours(curr, heatMap)
        neighbours.onEach { node ->
            if (node.cost > curr.cost + node.heatCost) {
                pathList.remove(node)
                node.cost = curr.cost + node.heatCost
                node.prev = curr
                pathList.add(node)
            }
        }
        foundList.add(curr)
    }

    foundList.onEach { println(it) }

    return path
}

private fun part2(readFile: List<StringBuilder>) {
    validate("", 0, 0)
}

private class HeatNodeMap(val heatCost: Int, val coordinate2D: Coordinate2D) {
    var cost: Int = Int.MAX_VALUE
    var prev: HeatNodeMap? = null
}

private fun getNeighbours(
    curr: HeatNodeMap,
//    direction: Direction,
    heatMap: Array<Array<HeatNodeMap>>
): Array<HeatNodeMap> {
    return Direction
        .getDirNSEW()
        .toMutableList()
        .apply {
//            removeAt((indexOf(direction) + 2) % 4)
        }.mapNotNull { dir ->
            val coord = curr.coordinate2D + dir
            if (coord.isValid(endX = heatMap.lastIndex.toLong(), endY = heatMap[0].lastIndex.toLong()))
                heatMap[coord.get_Y()][coord.get_X()]
            else null
        }.toTypedArray()
}
