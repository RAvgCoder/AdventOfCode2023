fun main() {
    MyArray.runPart(::part1,1,)
    MyArray.runPart(::part2,1,4)
}

private fun part1(readFile: List<StringBuilder>) {
    val points= readFile.sumOf { line ->
        (1 shl Card(line).matchingCards) shr 1
    }
    println("Points won in total is $points")
}
private fun part2(readFile: List<StringBuilder>) {
    val cardList : ArrayList<MutableList<Card>> = ArrayList()
    readFile.forEach { cardList.add(arrayListOf(Card(it))) }

    val sum = cardList.apply {
        forEachIndexed { i, cards ->
            val numMatched = cards.first().matchingCards
            if (numMatched != 0) {
                for (j in (i + 1)..(i + numMatched)) {
                    cardList[j].apply {
                        addAll(first().duplicate(cards.size))
                    }
                }
            }
        }
    }.sumOf { it.size }

    println("Total scratchcards you end up with is $sum")
}

data class Card(val rawData: StringBuilder) {
    val matchingCards: Int = rawData.split("[:|]".toRegex()).let { cardLine ->
        cardLine[1]
            .trim()
            .split("\\s+".toRegex())
            .toHashSet().let {winingCards ->
                cardLine[2]
                    .trim()
                    .split("\\s+".toRegex())
                    .count { winingCards.contains(it) }
            }
    }

    fun duplicate(nTimes : Int) : MutableList<Card> {
        return MutableList(nTimes) {this}
    }
}
