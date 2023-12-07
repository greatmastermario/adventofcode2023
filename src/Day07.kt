val handType = listOf("five", "four", "full", "three", "two", "one", "high")
val card = listOf("A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2")
val jCard = listOf("A", "K", "Q", "T", "9", "8", "7", "6", "5", "4", "3", "2", "J")

data class Hand(val cards: List<String>, val bid: Int) {
    fun handType(): String {
        val sortCards = cards.sorted()
        return getHandType(sortCards)
    }

    private fun getHandType(sortCards: List<String>): String {
        return when (sortCards.toSet().size) {
            1 -> "five"
            2 -> if (sortCards.filter { it == sortCards[0] }.size in listOf(1, 4)) "four" else "full"
            3 -> if (sortCards.groupBy { it }.values.filter { it.size == 3 }.size == 1) "three" else "two"
            4 -> "one"
            else -> "high"
        }
    }

    fun jHandType(): String {
        val sortCards = cards.filter { it != "J" }.sorted()
        val finalCards = sortCards.toMutableList()
        if (sortCards.isEmpty()) {
            return "five"
        }
        if (sortCards.size < 5) {
            val counts = sortCards.groupBy { it }
            val matchCard = counts.values.maxBy { it.size }
            for (count in 1..(5-finalCards.size)) {
                finalCards.add(matchCard[0])
            }
        }
        return getHandType(finalCards.sorted())
    }
}

fun main() {
    fun getHands(input: List<String>): List<Hand> {
        return input.map {
            val split = it.split(" ")
            Hand(split[0].split("").slice(1..5), split[1].toInt())
        }
    }

    fun compareHands(hand1: Hand, hand2: Hand): Int {
        if (hand1.handType() != hand2.handType()) {
            return handType.indexOf(hand1.handType()) - handType.indexOf(hand2.handType())
        } else {
            for (idx in 0..hand1.cards.lastIndex) {
                if (hand1.cards[idx] != hand2.cards[idx]) {
                    return card.indexOf(hand1.cards[idx]) - card.indexOf(hand2.cards[idx])
                }
            }
        }
        return 0
    }

    fun compareJHands(hand1: Hand, hand2: Hand): Int {
        if (hand1.jHandType() != hand2.jHandType()) {
            return handType.indexOf(hand1.jHandType()) - handType.indexOf(hand2.jHandType())
        } else {
            for (idx in 0..hand1.cards.lastIndex) {
                if (hand1.cards[idx] != hand2.cards[idx]) {
                    return jCard.indexOf(hand1.cards[idx]) - jCard.indexOf(hand2.cards[idx])
                }
            }
        }
        return 0
    }

    fun getWinnings(input: List<String>,  handComparator: Comparator<Hand>): Int {
        val sortedHands = getHands(input).sortedWith(handComparator).reversed()
        var winnings = 0
        for (idx in 1..sortedHands.size) {
            winnings += idx * sortedHands[idx - 1].bid
        }
        return winnings
    }

    fun part1(input: List<String>): Int {
        return getWinnings(input) { hand1, hand2 -> compareHands(hand1, hand2) }
    }

    fun part2(input: List<String>): Int {
        return getWinnings(input) { hand1, hand2 -> compareJHands(hand1, hand2) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440)

    val input = readInput("Day07")
    part1(input).println()

    check(part2(testInput) == 5905)
    part2(input).println()
}
