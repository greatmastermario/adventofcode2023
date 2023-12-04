import java.util.regex.Pattern
import kotlin.math.min
import kotlin.math.pow

data class Card(val winners: List<String>, val yours: List<String>)

fun main() {
    fun parseCard(cardString: String): Card {
        val nums = cardString.split(Pattern.compile(":\\s+"))
        val regex = Pattern.compile("\\s+")
        val halves = nums[1].split(" | ")
        return Card(halves[0].split(regex), halves[1].split(regex))
    }

    fun matches(card: Card): Int {
        var matches = 0
        for (num in card.yours) {
            if (num in card.winners) {
                matches++
            }
        }
        return matches
    }

    fun scoreCard(card: Card): Int {
        val matches = matches(card)
        if (matches == 0) {
            return 0
        } else {
            return 2.0.pow(matches - 1).toInt()
        }
    }

    fun part1(input: List<String>): Int {
        var score = 0
        input.forEach {score += scoreCard(parseCard(it))}
        return score
    }

    fun part2(input: List<String>): Int {
        val cardCounts = mutableMapOf<Int, Int>()
        for (idx in 0..input.lastIndex) {
            if (idx !in cardCounts.keys) {
                cardCounts[idx] = 1
            } else {
                cardCounts[idx] = cardCounts[idx]!! + 1
            }
            for (match in 1..min(matches(parseCard(input[idx])), input.lastIndex)) {
                val add = cardCounts[idx]!!
                val original = when {
                    (idx + match) in cardCounts.keys -> cardCounts[idx + match]!!
                    else -> 0
                }
                cardCounts[idx + match] = add + original
            }
        }
        return cardCounts.values.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)

    val input = readInput("Day04")
    part1(input).println()

    check(part2(testInput) == 30)
    part2(input).println()
}
