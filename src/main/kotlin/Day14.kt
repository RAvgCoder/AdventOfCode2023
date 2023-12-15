import HelperUtils.Utils.runPart
import HelperUtils.Utils.validate

/**
 * https://adventofcode.com/2023/day/14
 */
fun main() {
    // (partFunc, partNum, dayNum)
    runPart(::part1, 1, 14)
    runPart(::part2, 2, 14)
}

private fun part1(oldPlatform: List<CharArray>) {
    val platform = oldPlatform.toMutableList()
    flip(platform)

    rollNorth(platform)
    val supportLoad = calculateSupportLoad(platform)

    validate(supportLoad, 108857)
    println("The total load on the north support beams is $supportLoad")
}

private fun part2(oldPlatform: List<CharArray>) {
    val platform = oldPlatform.toMutableList()
    flip(platform)

    repeat(1000000000) {
        runCycle(platform)
    }

    val supportLoad = calculateSupportLoad(platform)
    println("The total load on the north support beams is $supportLoad")
    validate(supportLoad, 64)
}

private fun runCycle(platform: MutableList<CharArray>) {
    rollNorth(platform)

    rollWest(platform)

    rollSouth(platform)

    rollEast(platform)
}

private fun rollEast(platform: MutableList<CharArray>) {
    flip(platform)
    rollSouth(platform)
    flip(platform)
}

private fun rollWest(platform: MutableList<CharArray>) {
    flip(platform)
    rollNorth(platform)
    flip(platform)
}

private fun rollNorth(platform: MutableList<CharArray>) {
    for (line in platform) {
        var startJ = 0
        for (i in 1..<line.size) {
            if (line[i] == '#') {
                startJ = i + 1
                continue
            } else if (line[i] == '.') continue

            var j = startJ
            while (j < i) {
                if (line[j] == '.') {
                    line[j] = line[i]
                    line[i] = '.'
                    break
                }
                j++
            }
        }
    }
}

private fun rollSouth(platform: MutableList<CharArray>) {
    for (line in platform) {
        var startJ = line.lastIndex
        for (i in line.lastIndex - 1 downTo 0) {
            if (line[i] == '#') {
                startJ = i - 1
                continue
            } else if (line[i] == '.') continue

            var j = startJ
            while (j > i) {
                if (line[j] == '.') {
                    line[j] = line[i]
                    line[i] = '.'
                    break
                }
                --j
            }
        }
    }
}

private fun flip(inplace: MutableList<CharArray>) {
    for (i in inplace[0].indices) {
        for (j in i..<inplace.size) {
            val new = inplace[j][i]
            val old = inplace[i][j]
            inplace[i][j] = new
            inplace[j][i] = old
        }
    }
}

private fun calculateSupportLoad(platform: MutableList<CharArray>): Int {
    val resultArray = IntArray(platform[0].size)

    platform.forEach { line ->
        line.forEachIndexed { i, c ->
            if (c == 'O') resultArray[i]++
        }
    }

    val supportLoad = resultArray.reversed().foldIndexed(0) { idx, acc, num ->
        acc + (num * (idx + 1))
    }
    return supportLoad
}
