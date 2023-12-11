import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    fun emptyRows(input: List<List<String>>): List<Int> {
        val empty = mutableListOf<Int>()
        for (idx in 0..input.lastIndex) {
            if (input[idx].filter { it == "." }.size == input[idx].size) {
                empty.add(idx)
            }
        }
        return empty
    }

    fun emptyCols(input: List<List<String>>): List<Int> {
        val empty = mutableListOf<Int>()
        for (idx in 0..input.lastIndex) {
            if (input.map { it[idx] }.filter { it == "." }.size == input.size) {
                empty.add(idx)
            }
        }
        return empty
    }

    fun galaxyCoords(skyMap: List<List<String>>): List<Pair<Int, Int>> {
        val coords = mutableListOf<Pair<Int, Int>>()
        for (row in 0..skyMap.lastIndex) {
            for (col in 0..skyMap[row].lastIndex) {
                if ("#" == skyMap[row][col]) {
                    coords.add(Pair(row, col))
                }
            }
        }
        return coords
    }

    fun expandedTaxicab(first: Pair<Int, Int>, second: Pair<Int, Int>, expandRows: List<Int>, expandCols: List<Int>, expandBy: Long): Long {
        return abs(first.first - second.first) + abs(first.second - second.second) +
                expandRows.filter { it in min(first.first,second.first)..max(first.first,second.first) }.size * expandBy +
                expandCols.filter { it in min(first.second,second.second)..max(first.second,second.second) }.size * expandBy
    }

    fun shortestDistances(input: List<String>, expandBy: Long = 1L): Long {
        val skyMap = input.map { it.split("").slice(1..it.length) }
        val galaxies = galaxyCoords(skyMap)
        var distances = 0L
        for (first in 0..<galaxies.lastIndex) {
            first.println()
            for (second in first+1..galaxies.lastIndex) {
                val distance = expandedTaxicab(galaxies[first], galaxies[second], emptyRows(skyMap), emptyCols(skyMap), expandBy)
                distances += distance
            }
        }
        return distances
    }

    fun part1(input: List<String>): Long {
        return shortestDistances(input)
    }

    fun part2(input: List<String>): Long {
        return shortestDistances(input, 1000000L - 1L)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 374L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
