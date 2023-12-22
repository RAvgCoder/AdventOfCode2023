import HelperUtils.Utils.runPart
import HelperUtils.Utils.validate

/**
 * https://adventofcode.com/2023/day/4
 */
fun main() {
    runPart(::part1, 1, 4)
    runPart(::part2, 2, 4)
}

private fun part1(cardsList: List<StringBuilder>) {
    val points = cardsList.sumOf { line ->
        (1 shl Card(line).matchingCardsCount) shr 1 // (1 << match) >> 1
    }

    validate("Points won in total is", points, 28538)
}


private fun part2(listOfCards: List<StringBuilder>) {
    // Perform operations to determine the total number of scratchcards obtained
    val numOfScratchcards = arrayListOf<MutableList<Card>>().let { cardList ->
        // Initialize an empty cardList and populate it with Cards created from the StringBuilder instances
        listOfCards.asSequence().forEach { cardList.add(arrayListOf(Card(it))) }

        // Iterate through the cardList and modify it based on matching card counts
        cardList.asSequence().forEachIndexed { i, cards ->
            val numMatched = cards.first().matchingCardsCount
            if (numMatched != 0) {
                // If there are matching cards, duplicate and add cards to subsequent positions
                (1..numMatched).asSequence().forEach { offset ->
                    cardList[i + offset].apply {
                        addAll(first().duplicate(cards.size))
                    }
                }
            }
        }

        // Calculate the sum of total scratchcards obtained in the modified cardList
        cardList.sumOf { it.size }
    }

    validate("Total scratchcards you end up with is", numOfScratchcards, 9425061)
}


// Define a class `Card` with a property `rawData` of type `StringBuilder`
private class Card(rawData: StringBuilder) {

    // Define a property `matchingCardsCount` initialized using a let block
    val matchingCardsCount: Int = rawData.split("[:|]".toRegex()).let { cardLine ->
        cardLine[1]
            .trim()
            // Splits the second part (winning cards) and finds the intersection with the third part (your cards)
            .split("\\s+".toRegex())
            .intersect(
                cardLine[2].trim()
                    .split("\\s+".toRegex())
                    .toSet() // Converts your cards to a set
            ).size // Calculates the size of the intersection, which represents the matching cards count
    }

    // Function to create a list of cards duplicated `nTimes`
    fun duplicate(nTimes: Int): MutableList<Card> {
        return MutableList(nTimes) { this }
    }
}

