import HelperUtils.Utils.runPart
import HelperUtils.Utils.validate

/**
 * https://adventofcode.com/2023/day/8
 */
fun main() {
    // (partFunc, partNum, dayNum)
    runPart(::part1, 1, 8)
    runPart(::part2, 2)
}

private fun part1(readFile: List<StringBuilder>) {
    val (directions, nodes) = setUp(readFile)
    var curr = "AAA"
    var dirIdx = 0
    var steps = 0
    while (curr != "ZZZ") {
        steps++
        curr = nodes[curr]!![directions[dirIdx].digitToInt()]
        dirIdx = (dirIdx + 1) % directions.length
    }

    validate("Steps are required to reach is", steps, 19637)
}

const val DEAD = "ಠಿ_ಠ"
private fun part2(readFile: List<StringBuilder>) {
    val (directions, nodes) = setUp(readFile)

    val startingPoints =
        nodes.filter { it.key.endsWith("A") }.map { Triple(it.key, 0, 0) }.toList().toMutableList()

    var steps = 0




    validate("Steps are required to reach is", steps, 6)
}


private fun setUp(readFile: List<StringBuilder>): Pair<String, MutableMap<String, Array<String>>> {
    return Pair(
        readFile[0].map { if (it == 'R') 1 else 0 }.joinToString(separator = ""),
        createNodes(readFile)
    )
}

private fun createNodes(readFile: List<StringBuilder>): MutableMap<String, Array<String>> =
    readFile.drop(2).fold(mutableMapOf<String, Array<String>>()) { acc, line ->
        line.split("=").let { dir ->
            dir.last().split(",").let { destination ->
                acc.put(
                    dir.first().trim(), arrayOf(
                        destination[0].trim().removeRange(0, 1),
                        destination[1].trim().removeRange(destination[1].lastIndex - 1, destination[1].lastIndex)
                    )
                )
            }
        }
        acc
    }


private fun steps1(
    steps: Int,
    startingPoints: MutableList<Triple<String, Int, Int>>,
    nodes: MutableMap<String, Array<String>>,
    directions: String
) {
    val hasSeenStopState: MutableList<Boolean> = startingPoints.map { false }.toMutableList()
    var steps = 0
    while (!hasSeenStopState.reduce(Boolean::and)) {
        steps++

        startingPoints.mapIndexed { i, spot ->
            var (curr, walkIdx, count) = spot

            while (!curr.endsWith("Z")) {
                print("Curr=$curr => ")
                if (curr == DEAD)
                    break

                curr = if (isDead(nodes, curr)) DEAD
                else nodes[curr]!![directions[walkIdx].digitToInt()]

                println(curr)

                walkIdx = (walkIdx + 1) % directions.length

                hasSeenStopState[i] = hasSeenStopState[i] or curr.endsWith("Z")
                count++
            }

            startingPoints[i] = Triple(
                curr,
                walkIdx,
                count
            )
            count
        }

        val a = "d"

//        startingPoints.forEachIndexed { index, (curr, walk, stepCount) ->
//            val next = nodes[curr]!![directions[walk].digitToInt()]
//            startingPoints[index] = Triple(next, (walk + 1) % directions.length, stepCount+1)
//            hasSeenStopState[index] = hasSeenStopState[index] or curr.endsWith("Z")
//        }
    }
}

private fun isDead(nodes: MutableMap<String, Array<String>>, curr: String): Boolean =
    nodes[curr]!!.let { (left, right) ->
        left == curr && right == curr
    }

private fun VAR(
    startingPoints: MutableList<Pair<String, Int>>, nodes: MutableMap<String, Array<String>>, directions: String
): Int {
    var sum = 0
    var catchUp = 0
    while (!startingPoints.all { it.first.endsWith("Z") } || catchUp > 0) {
        sum += startingPoints.mapIndexed { i, (start, startIdx) ->
            var idx = startIdx
            var curr = start
            var canMove = true
            var count = 0
            while (catchUp-- > 0) {
                curr = nodes[curr]!![directions[idx].digitToInt()]
                idx = (idx + 1) % directions.length
            }
            catchUp = 0
            while (!curr.endsWith("Z") || canMove) {
                catchUp++
                count++
                curr = nodes[curr]!![directions[idx].digitToInt()]
                idx = (idx + 1) % directions.length
                canMove = false
            }
            startingPoints[i] = curr to idx
            count
        }.sum()
    }
    return sum
}
