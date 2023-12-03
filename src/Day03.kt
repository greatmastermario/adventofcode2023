import java.util.regex.Pattern
import kotlin.math.max
import kotlin.math.min

fun main() {
    val notSymbol = listOf('.', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9')

    fun getPartNums(input: String): Map<List<Int>, Int> {
        val parts = mutableMapOf<List<Int>, Int>()
        val pattern = Pattern.compile("\\D+")
        val splitParts = input.split(pattern)
        var lastIndex = 0
        splitParts.forEach {
            if (it.isNotBlank()) {
                val index = input.indexOf(it, lastIndex)
                val partLocations = mutableListOf<Int>()
                for (idx in index..<index+it.length) {
                    partLocations.add(idx)
                    lastIndex = index + it.length
                }
                parts[partLocations] = it.toInt()
            }
        }
        return parts
    }

    fun getSymbols(input: String): List<Int> {
        val symbolLocs = mutableListOf<Int>()
        for (idx in 0..input.lastIndex) {
            if (input[idx] !in notSymbol) {
                symbolLocs.add(idx)
            }
        }
        return symbolLocs
    }

    fun getGears(input: String): List<Int> {
        val gearLocs = mutableListOf<Int>()
        for (idx in 0..input.lastIndex) {
            if (input[idx] == '*') {
                gearLocs.add(idx)
            }
        }
        return gearLocs
    }

    fun part1(input: List<String>): Int {
        val parts = mutableListOf<Map<List<Int>, Int>>()
        val symbols = mutableListOf<List<Int>>()
        input.forEach {
            parts.add(getPartNums(it))
            symbols.add(getSymbols(it))
        }
        val validParts = mutableMapOf(mapOf(-1 to listOf(-1)) to 0)
        for (row in 0..parts.lastIndex) {
            for (col in symbols[row]) {
                for (checkRow in max(0, row-1)..min(row+1, parts.lastIndex)) {
                    for (part in parts[checkRow]) {
                        for (idx in col-1..col+1) {
                            if (idx in part.key) {
                                validParts[mapOf(checkRow to part.key)] = part.value
                                break
                            }
                        }
                        if (col + 1 < part.key.first()) {
                            break
                        }
                    }
                }
            }
        }
        return validParts.values.sum()
    }

    fun part2(input: List<String>): Int {
        val parts = mutableListOf<Map<List<Int>, Int>>()
        val gears = mutableListOf<List<Int>>()
        input.forEach {
            parts.add(getPartNums(it))
            gears.add(getGears(it))
        }
        var gearRatios = 0
        for (row in 0..parts.lastIndex) {
            for (col in gears[row]) {
                val foundParts = mutableListOf<Int>()
                for (checkRow in max(0, row - 1)..min(row + 1, parts.lastIndex)) {
                    for (part in parts[checkRow]) {
                        for (idx in col-1..col+1) {
                            if (idx in part.key) {
                                foundParts.add(part.value)
                                break
                            }
                        }
                        if (col + 1 < part.key.first()) {
                            break
                        }
                    }
                }
                if (foundParts.size == 2) {
                    gearRatios += foundParts[0] * foundParts[1]
                }
            }
        }
        return gearRatios
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)

    val input = readInput("Day03")
    part1(input).println()

    check(part2(testInput) == 467835)
    part2(input).println()
}
