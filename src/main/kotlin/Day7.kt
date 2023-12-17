import HelperUtils.Utils.runPart
import HelperUtils.Utils.validate

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
    }.let(::findScores)

    validate("The new total winnings is", winnings, 251121738)
}

private fun part2(readFile: List<StringBuilder>) {
    val winnings = readFile.map { cardData ->
        Cards(cardData, jIsWeak = true)
    }.let(::findScores)

    validate("The new total winnings is", winnings, 251421071)
}

private fun findScores(cards: List<Cards>): Int = cards
    .sortedWith(compareBy({ it.pokerHandType }, { it.cardData }))
    .reversed()
    .mapIndexed { index: Int, card: Cards ->
        (index + 1) * card.bid
    }.sum()

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
                // M == J {After mapping}
                .partition { it.first == 'M' }
                .let {
                    remainingCards = it.second
                    (it.first.firstOrNull()?.second ?: 0)
                }

            return when ((remainingCards.firstOrNull()?.second ?: 0) + jCount) {
                5 -> PokerHandType.FIVE_OF_A_KIND
                4 -> PokerHandType.FOUR_OF_A_KIND
                3 -> if (remainingCards[1].second == 2) PokerHandType.FULL_HOUSE
                else PokerHandType.THREE_OF_A_KIND

                2 -> if (remainingCards[1].second == 1) PokerHandType.ONE_PAIR
                else PokerHandType.TWO_PAIR

                else -> PokerHandType.HIGH_CARD
            }
        } else when {
            cardRaw.values.any { it == 5 } -> PokerHandType.FIVE_OF_A_KIND
            cardRaw.values.any { it == 4 } -> PokerHandType.FOUR_OF_A_KIND
            cardRaw.values.any { it == 3 } && cardRaw.values.any { it == 2 } -> PokerHandType.FULL_HOUSE
            cardRaw.values.any { it == 3 } -> PokerHandType.THREE_OF_A_KIND
            cardRaw.values.count { it == 2 } == 2 -> PokerHandType.TWO_PAIR
            cardRaw.values.count { it == 2 } == 1 -> PokerHandType.ONE_PAIR
            else -> PokerHandType.HIGH_CARD
        }
    }

    override fun toString(): String = "Cards(Data=$cardData, Bid=$bid, PokerHandType=$pokerHandType)"
}

enum class PokerHandType {
    FIVE_OF_A_KIND, FOUR_OF_A_KIND, FULL_HOUSE, THREE_OF_A_KIND, TWO_PAIR, ONE_PAIR, HIGH_CARD;
}

private val rankMap1 = arrayOf(
    'A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2'
)

private val rankMap2 = arrayOf(
    'A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J'
)
