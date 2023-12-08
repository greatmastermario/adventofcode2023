import kotlin.math.max
import kotlin.math.pow

fun main() {
    fun getDirections(input: String): List<String> {
        return input.split("").slice(1..input.length)
    }

    fun getMap(input: List<String>): Map<String, List<String>> {
        val map = mutableMapOf<String, List<String>>()
        for (mapping in input) {
            val split = mapping.split(" = ")
            val valSplit = split[1].slice(1..<split[1].length-1).split(", ")
            map[split[0]] = valSplit
        }
        return map
    }

    fun getFactorization(input: Long): MutableMap<Long, Long> {
        var factor = 2L
        var left = input
        val factors = mutableMapOf<Long, Long>()
        while(left != 1L) {
            if (left % factor != 0L) {
                factor++
            } else {
                factors[factor] = (factors[factor]?:0) + 1
                left /= factor
            }
        }
        return factors
    }

    fun part1(input: List<String>): Int {
        val directions = getDirections(input[0])
        val map = getMap(input.slice(2..input.lastIndex))
        var counter = 0
        var at = "AAA"
        var directionIdx = 0
        while (at != "ZZZ") {
            val currentMap = map[at]?: listOf("AAA", "AAA")
            at = if (directions[directionIdx] == "L") currentMap[0] else currentMap[1]
            counter++
            directionIdx++
            directionIdx = if (directionIdx == directions.size) 0 else directionIdx
        }
        return counter
    }

    fun part2(input: List<String>): Long {
        val directions = getDirections(input[0])
        val map = getMap(input.slice(2..input.lastIndex))
        var counter = 0
        var ats = map.keys.filter { it.endsWith("A") }.toMutableList()
        var directionIdx = 0
        val factors = mutableListOf<Long>()
        while (ats.isNotEmpty()) {
            counter++
            val toRemove = mutableListOf<Int>()
            for (atIdx in 0..ats.lastIndex) {
                val currentMap = map[ats[atIdx]]?: listOf("AAA", "AAA")
                ats[atIdx] = if (directions[directionIdx] == "L") currentMap[0] else currentMap[1]
                if (ats[atIdx].endsWith("Z")) {
                    println("counter=$counter atIdx=$atIdx location=${ats[atIdx]}")
                    factors.add(counter.toLong())
                    toRemove.add(atIdx)
                }
            }
            for (idx in toRemove.reversed()) {
                ats.removeAt(idx)
            }
            directionIdx++
            directionIdx = if (directionIdx == directions.size) 0 else directionIdx
        }
        val finalFactorization = getFactorization(factors[0])
        for (factor in factors.slice(1..factors.lastIndex)) {
            val nextFactorization = getFactorization(factor)
            for (key in nextFactorization.keys) {
                finalFactorization[key] = max(finalFactorization[key]?:0, nextFactorization[key]?:0)
            }
        }
        var finalCounter = 1L
        for (entry in finalFactorization) {
            finalCounter *= entry.key.toDouble().pow(entry.value.toDouble()).toLong()
        }
        return finalCounter
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 2)
    val testInput2 = readInput("Day08_test1")
    check(part1(testInput2) == 6)

    val input = readInput("Day08")
    part1(input).println()
    val testInput3 = readInput("Day08_test2")
    check(part2(testInput3) == 6L)
    part2(input).println()
}
