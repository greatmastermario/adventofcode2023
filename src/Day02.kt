fun main() {
    val maxCubes = mapOf("red" to 12, "green" to 13, "blue" to 14)

    fun parseGame(input: String): List<Map<String, Int>> {
        val sets = mutableListOf<Map<String, Int>>()
        input.split("; ").forEach {
            val set = mutableMapOf<String, Int>()
            it.split(", ").forEach {
                val split = it.split(" ")
                set[split[1]] = split[0].toInt()
            }
            sets.add(set)
        }
        return sets
    }

    fun gameValid(game: List<Map<String, Int>>): Boolean {
        for (set in game) {
            for (key in set.keys) {
                if (set[key]!! > maxCubes[key]!!) {
                    return false
                }
            }
        }
        return true
    }

    fun power(game: List<Map<String, Int>>): Int {
        val minCubes = mutableMapOf("red" to 0, "green" to 0, "blue" to 0)
        game.forEach {
            for (key in it.keys) {
                if (it[key]!! > minCubes[key]!!) {
                    minCubes[key] = it[key]!!
                }
            }
        }
        var power = 1
        for (cubes in minCubes.values) {
            power *= cubes
        }
        return power
    }

    fun part1(input: List<String>): Int {
        var validGameSum = 0
        input.forEach {
            val split = it.split(": ")
            val gameNum = split[0].split(" ")[1].toInt()
            val game = parseGame(split[1])
            if (gameValid(game)) {
                validGameSum += gameNum
            }
        }
        return validGameSum
    }

    fun part2(input: List<String>): Int {
        var powers = 0
        input.forEach {powers += power(parseGame(it.split(": ")[1]))}
        return powers
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)

    val input = readInput("Day02")
    part1(input).println()

    check(part2(testInput) == 2286)
    part2(input).println()
}
