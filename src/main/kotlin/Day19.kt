import HelperUtils.Utils.runPart
import HelperUtils.Utils.validate
import State.*

/**
 * https://adventofcode.com/2023/day/19
 */
fun main() {
    // (partFunc, partNum, dayNum)
    runPart(::part1, 1, 19)
    runPart(::part2, 2, 19)
}

private fun part1(readFile: List<StringBuilder>) {
    val workFlows = readFile
        .takeWhile { it.isNotEmpty() }
        .map(::createWorkFlow)

    val start = workFlows.find { it.nameOfWorkFlow == "in" }!!
    val ratingList = readFile
        .takeLastWhile { it.isNotEmpty() }
        .map { createRating(it, start) }

    val sumOfRatingParts = ratingList.sumOf { rating ->
        while (true) {
            val workFlow = rating.workFlow
            for ((operator, nextWorkflow) in workFlow!!.workFlowList) {
                val (charWorkFlow, symbol, num) = operator
                val value = rating.ratingList.find { (charRating, _) ->
                    charWorkFlow == charRating
                }

                if (value != null) {
                    if (when (symbol) {
                            '>' -> value.second.first > num
                            '<' -> value.second.first < num
                            else -> throw IllegalStateException("$num $symbol")
                        }
                    ) {
                        nextState(nextWorkflow, rating, workFlows)
                        break
                    }
                }
                nextState(workFlow.default, rating, workFlows)
            }

            if (rating.state != WORKING) {
                break
            }
        }

        if (rating.state == ACCEPTED) rating.ratingList.sumOf { it.second.first }
        else 0
    }


    validate(
        "Sum of all of the rating numbers for all of the parts that ultimately get accepted are",
        sumOfRatingParts,
        373302
    )
}

private fun part2(readFile: List<StringBuilder>) {
    val workFlows = readFile
        .takeWhile { it.isNotEmpty() }
        .map(::createWorkFlow)


    var ratings = mutableListOf(
        Ratings(
            mutableListOf(
                'x' to 1..4000,
                'm' to 1..4000,
                'a' to 1..4000,
                's' to 1..4000
            ),
            WORKING,
            workFlows.find { it.nameOfWorkFlow == "in" }!!,
        )
    )

    while (ratings.all { it.state != ACCEPTED }) {
        val new = mutableListOf<Ratings>()
        ratings.fold(mutableListOf<Ratings>()) { acc, rating ->
            rating.workFlow ?: {
                var r = rating

                // x [0]
                rating.workFlow!!.workFlowList.forEach { (operator, nextWork) ->
                    val (charWorkFlow, symbol, num) = operator
                    if (charWorkFlow == 'x') {
                        val (accepted, rejected) = newRanges(symbol, num, r.ratingList[0].second)
                        accepted?.let { setRating(r, accepted, 0, acc, nextWork, workFlows) }
                        rejected?.let { setRating(r, rejected, 0, acc, nextWork, workFlows) }
                    }
                }

                // m [0]
                rating.workFlow!!.workFlowList.forEach { (operator, nextWork) ->
                    val (charWorkFlow, symbol, num) = operator
                    if (charWorkFlow == 'm') {
                        val (accepted, rejected) = newRanges(symbol, num, r.ratingList[1].second)
                        accepted?.let { setRating(r, accepted, 1, acc, nextWork, workFlows) }
                        rejected?.let { setRating(r, rejected, 1, acc, nextWork, workFlows) }
                    }
                }

                // a [0]
                rating.workFlow!!.workFlowList.forEach { (operator, nextWork) ->
                    val (charWorkFlow, symbol, num) = operator
                    if (charWorkFlow == 'm') {
                        val (accepted, rejected) = newRanges(symbol, num, r.ratingList[2].second)
                        accepted?.let { setRating(r, accepted, 2, acc, nextWork, workFlows) }
                        rejected?.let { setRating(r, rejected, 2, acc, nextWork, workFlows) }
                    }
                }

                // s [0]
                rating.workFlow!!.workFlowList.forEach { (operator, nextWork) ->
                    val (charWorkFlow, symbol, num) = operator
                    if (charWorkFlow == 'm') {
                        val (accepted, rejected) = newRanges(symbol, num, r.ratingList[3].second)
                        accepted?.let { setRating(r, accepted, 3, acc, nextWork, workFlows) }
                        rejected?.let { setRating(r, rejected, 3, acc, nextWork, workFlows) }
                    }
                }
            }
            acc
        }

    }


//
//    var state = "in"
//    while (true) {
//        val workFlow = workFlows.find { it.nameOfWorkFlow == state }!!
//        for ((operator, nextWorkflow) in workFlow.workFlowList) {
//            val (c1, op, num) = operator
//            val value = ratings.ratingList.find { (symbol, _) ->
//                c1 == symbol
//            }
//
//            if (value != null) {
//                if (when (op) {
//                        '>' -> value.second.first > num
//                        '<' -> value.second.last num
//                        else -> throw IllegalStateException("$num $op")
//                    }
//                ) {
//                    state = nextWorkflow
//                    break
//                }
//            }
//            state = workFlow.default
//        }
//
//        if (state == "A" || state == "R") {
//            break
//        }
//    }
//
//    if (state == "A") ratings.ratingList.sumOf { it.second.first }
//    else 0
//

    validate("", 0, 0)
}

private fun nextState(nextWorkflow: String, rating: Ratings, workFlows: List<WorkFlow>) {
    rating.state = when (nextWorkflow) {
        "A" -> ACCEPTED
        "R" -> REJECTED
        else -> WORKING
    }

    rating.workFlow = workFlows.find { it.nameOfWorkFlow == nextWorkflow }
}

private fun setRating(
    rating: Ratings,
    newRange: IntRange,
    index: Int,
    storage: MutableList<Ratings>,
    nextWorkflow: String,
    workFlows: List<WorkFlow>
) {
    val newRatingList = cloneRatingList(rating.ratingList).apply {
        this[index] = this[0].first to newRange
    }
    val ratingClone = rating.copy(ratingList = newRatingList)
    nextState(nextWorkflow, ratingClone, workFlows)
    storage.add(ratingClone)
}

fun cloneRatingList(ratingList: MutableList<Pair<Char, IntRange>>): MutableList<Pair<Char, IntRange>> {
    return mutableListOf(
        'x' to IntRange(ratingList[0].second.first, ratingList[0].second.last),
        'm' to IntRange(ratingList[1].second.first, ratingList[1].second.last),
        'a' to IntRange(ratingList[2].second.first, ratingList[2].second.last),
        's' to IntRange(ratingList[3].second.first, ratingList[3].second.last),
    )
}

fun newRanges(symbol: Char, num: Int, range: IntRange): Array<IntRange?> {
    val first = range.first
    val last = range.last

    return if (symbol == '<') {
        if (last < num) { // 20..20 < 50
            arrayOf(range, null)
        } else if (num < first || last == first || first == num) { // 20..20 < 5 | 20..20 < 20 | 2..20 < 2
            arrayOf(null, range)
        } else if (last == num) { // 2..20 < 20
            arrayOf(IntRange(first, last - 1), IntRange(last, last))
        } else { // 0..20 < 10 | In the middle
            arrayOf(IntRange(first, num - 1), IntRange(num, last))
        }
    } else {
        if (first > num || last == first || last == num) { // 20..20 > 1 | 20..20 > 20 | 2..20 > 20
            arrayOf(null, range)
        } else if (num > last) { // 20..20 > 50
            arrayOf(range, null)
        } else if (first == num) { // 2..20 > 2
            arrayOf(IntRange(first + 1, last), IntRange(first, first))
        } else { // 0..20 > 10 | In the middle
            arrayOf(IntRange(first, num), IntRange(num + 1, last))
        }
    }
}

fun createWorkFlow(workFlowData: StringBuilder): WorkFlow {
    val information = workFlowData.split("[{},]".toRegex()).dropLast(1)
    val nameOfWorkFlow = information[0]
    val default = information.last()
    val workFlowList = information.drop(1).dropLast(1)
        .map { op ->
            val option = op.split(":")
            val operation = option[0].split("", limit = 4).drop(1)
            Pair(
                Triple(
                    operation[0][0],
                    operation[1][0],
                    operation[2].toInt()
                ),
                option[1]
            )
        }
    return WorkFlow(nameOfWorkFlow, workFlowList, default)
}

fun createRating(ratingData: StringBuilder, workFlowStart: WorkFlow): Ratings {
    val list = ratingData
        .split("[{,}]".toRegex())
        .drop(1)
        .dropLast(1)
        .map { data ->
            val part = data.split("=")
            part[0][0] to part[1].toInt()..part[1].toInt()
        }.toMutableList()

    return Ratings(list, WORKING, workFlowStart)
}

data class Ratings(
    val ratingList: MutableList<Pair<Char, IntRange>>,
    var state: State,
    var workFlow: WorkFlow?
)

enum class State { ACCEPTED, REJECTED, WORKING }

data class WorkFlow(
    val nameOfWorkFlow: String,
    val workFlowList: List<Pair<Triple<Char, Char, Int>, String>>,
    val default: String
)

