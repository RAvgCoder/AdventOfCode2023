import HelperUtils.Utils.runPart
import HelperUtils.Utils.validate

/**
 * https://adventofcode.com/2023/day/15
 */
fun main() {
    // (partFunc, partNum, dayNum)
    runPart(::part1, 1, 15)
    runPart(::part2, 2, 15)
}

private fun part1(readFile: List<StringBuilder>) {
    val words = setUP(readFile)
    val sum = words.sumOf(::hash)
    validate("The sum of the results is", sum, 509152)
}

private fun part2(readFile: List<StringBuilder>) {
    val words = setUP(readFile)
    val boxes = Array<MutableList<Pair<String, Int>>>(256) { mutableListOf() }

    words.forEach { word ->
        val (label, num) = word.split("[=-]".toRegex())
        val hash = hash(label)
        if (num.isEmpty()) {
            boxes[hash].removeIf { it.first == label }
        } else {
            val focalLen = num.toInt()
            val found = findAndChangeFocalLength(boxes, hash, label, focalLen)
            if (!found) boxes[hash].add(Pair(label, focalLen))
        }
    }

    val focusingPower = getFocusingPower(boxes)

    validate("The the focusing power of the resulting lens configuration is", focusingPower, 244403)
}

private fun findAndChangeFocalLength(
    boxes: Array<MutableList<Pair<String, Int>>>, hash: Int, label: String, lensNum: Int
): Boolean {
    var found = false
    for ((i, pair) in boxes[hash].withIndex()) {
        if (pair.first == label) {
            if (pair.second != lensNum) boxes[hash][i] = pair.first to lensNum
            found = true
            break
        }
    }
    return found
}


private fun getFocusingPower(boxes: Array<MutableList<Pair<String, Int>>>) = boxes.mapIndexed { boxNum, box ->
    box.mapIndexed { slot, (_, focalLen) ->
        (boxNum + 1) * (slot + 1) * focalLen
    }.sum()
}.sum()

private fun hash(str: String) = str.fold(0) { acc: Int, c: Char ->
    ((acc + c.code) * 17) % 256
}

private fun setUP(file: List<StringBuilder>): List<String> = file[0].split(",").toList()
