import MyArray.runPart

/**
 * https://adventofcode.com/2023/day/7
 */
fun main() {
    // (partFunc, partNum, dayNum)
    runPart(::part1, 1, 7)
    runPart(::part2, 2, 7)
}

private fun part1(readFile: List<StringBuilder>) {
    val winnings = readFile.map { cardData ->
        Cards(cardData, jIsWeak = false)
    }.let(::findScores).apply {
        check(this == 251121738) { "Expected: 251121738 Got: $this" }
    }

    println("The new total winnings is $winnings")
}

private fun part2(readFile: List<StringBuilder>) {
    val winnings = readFile.map { cardData ->
        Cards(cardData, jIsWeak = true)
    }.let(::findScores).apply {
        check(this == 251421071) { "Expected: 251421071 Got: $this" }
    }

    println("The new total winnings is $winnings")
}

private fun findScores(cards: List<Cards>): Int {
    var score = 1
    return cards.groupBy {
        it.pokerHandType
    }.toList().sortedBy { (hand, _) ->
        hand.toString()
    }.reversed().sumOf { (_, cards) ->
        cards.sortedBy {
            it.cardData
        }.reversed().apply {
            this.onEach(::println)
            println()
        }.sumOf { card ->
            score++ * card.bid
        }
    }
}

private data class Cards(var cardData: StringBuilder, val jIsWeak: Boolean) {
    val pokerHandType: PokerHandType
    val bid: Int

    init {
        pokerHandType = getPokerHandType(cardData.split("\\s+".toRegex()).let {
            bid = it[1].toInt()
            it[0]
        }.let { StringBuilder(it) }.apply {
            this.forEachIndexed { index, char ->
                this[index] = ((if (!jIsWeak) rankMap1 else rankMap2).indexOf(char) + 'A'.code).toChar()
            }
        }.apply { cardData = this }.fold(hashMapOf()) { charToInt, c ->
            charToInt[c] = (charToInt[c] ?: 0) + 1
            charToInt
        })
    }

    private fun getPokerHandType(cardRaw: HashMap<Char, Int>): PokerHandType {
        return if (jIsWeak) {
            var remainingCards: List<Pair<Char, Int>>
            val jCount = cardRaw.toList()
                .sortedByDescending { it.second }
                .partition { it.first == 'M' }
                .let {
                    remainingCards = it.second
                    (it.first.firstOrNull()?.second ?: 0)
                }

            return when ((remainingCards.firstOrNull()?.second ?: 0) + jCount) {
                5 -> PokerHandType.A_FIVE_OF_A_KIND
                4 -> PokerHandType.B_FOUR_OF_A_KIND
                3 -> if (remainingCards[1].second == 2) PokerHandType.C_FULL_HOUSE
                else PokerHandType.D_THREE_OF_A_KIND

                2 -> if (remainingCards[1].second == 1) PokerHandType.F_ONE_PAIR
                else PokerHandType.E_TWO_PAIR

                else -> PokerHandType.G_HIGH_CARD
            }
        } else when {
            cardRaw.values.any { it == 5 } -> PokerHandType.A_FIVE_OF_A_KIND
            cardRaw.values.any { it == 4 } -> PokerHandType.B_FOUR_OF_A_KIND
            cardRaw.values.any { it == 3 } && cardRaw.values.any { it == 2 } -> PokerHandType.C_FULL_HOUSE
            cardRaw.values.any { it == 3 } -> PokerHandType.D_THREE_OF_A_KIND
            cardRaw.values.count { it == 2 } == 2 -> PokerHandType.E_TWO_PAIR
            cardRaw.values.count { it == 2 } == 1 -> PokerHandType.F_ONE_PAIR
            else -> PokerHandType.G_HIGH_CARD
        }
    }

    override fun toString(): String {
        return "Cards(data=$cardData, bid=$bid, PokerHandType=$pokerHandType)"
    }
}

enum class PokerHandType {
    A_FIVE_OF_A_KIND, B_FOUR_OF_A_KIND, C_FULL_HOUSE, D_THREE_OF_A_KIND, E_TWO_PAIR, F_ONE_PAIR, G_HIGH_CARD;
}

private val rankMap1 = arrayOf(
    'A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2'
)

private val rankMap2 = arrayOf(
    'A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J'
)
