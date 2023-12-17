import HelperUtils.Utils.runPart

/**
 * https://adventofcode.com/2023/day/5
 */
fun main() {
    runPart(::part1, 1, 5)
    runPart(::part2, 2, 5)
}

private fun part1(lookUpTable: List<StringBuilder>) {
    val farmMapList = setup(lookUpTable)
    val seeds = lookUpTable[0].split("\\s+".toRegex())
        .drop(1)
        .map { it.toLong() }
        .minOfOrNull { seedState ->
            findMinMapState(seedState, farmMapList)
        }
    println("The lowest location number that corresponds to any of the initial seed number is $seeds")
}

private fun part2(lookUpTable: List<StringBuilder>) {
    val farmMapList = setup(lookUpTable)

    val seeds =
        lookUpTable[0].split("\\s+".toRegex())
            .drop(1)
            .asSequence()
            .map { it.toLong() }
            // get the seed ranges 2 3 4 5 -> [[2,3], [4,5]]
            .windowed(2, 2)
            .map { LongRange(it[0], it[1] + (it[0] - 1)) }
            .minOf { seedRange ->
                seedRange.minOf { findMinMapState(it, farmMapList) }
            }

    println("The lowest location number that corresponds to any of the initial seed number is $seeds")
}

private data class FarmMap(
    // seed-to-soil map:
    val name: String,
    // Pair <destination, source>
    val ranges: MutableList<Pair<LongRange, LongRange>>,
    // 2 4 -> mapNum = 4-2 = 2
    val mapNum: List<Long>
)

private fun setup(lookUpTable: List<StringBuilder>): List<FarmMap> {
    return lookUpTable.asSequence().fold(mutableListOf<MutableList<String>>()) { acc, str ->
        if (str.isBlank()) {
            acc.add(mutableListOf())
        } else {
            if (acc.isEmpty() || acc.last().isEmpty()) {
                acc.add(mutableListOf())
            }
            acc.last().add(str.toString())
        }
        acc
    }.filter {
        it.isNotEmpty()
    }.drop(1).map { rawData ->
        var dest: Long
        var source: Long
        val mapNum = mutableListOf<Long>()
        FarmMap(
            rawData[0],
            rawData.drop(1).map {
                val rangeData = it.split("\\s+".toRegex())
                dest = rangeData[0].toLong()
                source = rangeData[1].toLong()
                val range = rangeData[2].toInt() - 1
                mapNum.add(dest - source)
                Pair(
                    LongRange(dest, dest + range),
                    LongRange(source, source + range)
                )
            }.toMutableList(),
            mapNum
        )
    }.toList()
}

private fun findMinMapState(seedState: Long, farmMapList: List<FarmMap>): Long {
    var state: Long = seedState
    farmMapList.asSequence().forEach { map ->
        state = map.ranges.indexOfFirst { (_, range) ->
            state in range
        }.let { idx ->
            if (idx != -1) state + map.mapNum[idx] else state
        }
    }
    return state
}

