fun main() {
    MyArray.runPart(::part1, 1, 6)
    MyArray.runPart(::part2, 2,6)
}

private fun part1(readFile: List<StringBuilder>) {
    val time = readFile[0].split("\\s+".toRegex()).drop(1).map { it.toInt() }
    val distance = readFile[1].split("\\s+".toRegex()).drop(1).map { it.toInt() }

    val timeDistance = time.mapIndexed { index, ms ->
        ms to distance[index]
    }.map { (waitTime, distance) ->
        // timeDistance = (Min time - Max time) + 1
        ((waitTime downTo (waitTime / 2) + 1).first {
            ((waitTime - it) * it) > distance
        } - (0..waitTime / 2).first {
            (waitTime - it) * it > distance
        }) + 1
    }.fold(1) { acc: Int, i: Int ->
        acc * i
    }.apply { println(this) }
    println("Number of ways you can beat the record is $timeDistance")
}

private fun part2(readFile: List<StringBuilder>) {
    val waitTime = readFile[0]
        .split("\\s+".toRegex())
        .drop(1)
        .joinToString(separator = "")
        .toLong()

    val distance = readFile[1]
        .split("\\s+".toRegex())
        .drop(1)
        .joinToString(separator = "")
        .toLong()

    // timeDistance = (Min time - Max time) + 1
    val timeDistance = ((waitTime downTo (waitTime / 2) + 1).first {
            ((waitTime - it) * it) > distance
        } - (0..waitTime / 2).first {
            (waitTime - it) * it > distance
        }) + 1

    println("Number of ways you can beat the record is $timeDistance")
}