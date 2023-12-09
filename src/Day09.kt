fun main() {
    fun officialSequenceMethod(input: List<Int>): Int {
        val nextInput = mutableListOf<Int>()
        for (idx in 1..input.lastIndex) {
            nextInput.add(input[idx] - input[idx - 1])
        }
        if (nextInput.any { it != 0 }) {
            return officialSequenceMethod(nextInput) + input.last()
        }
        return input.last()
    }

    fun priorSequenceMethod(input: List<Int>): Int {
        val nextInput = mutableListOf<Int>()
        for (idx in 1..input.lastIndex) {
            nextInput.add(input[idx] - input[idx - 1])
        }
        if (nextInput.any { it != 0 }) {
            return input.first() - priorSequenceMethod(nextInput)
        }
        return input.first()
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { line -> officialSequenceMethod(line.split(" ").map { value -> value.toInt() }) }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line -> priorSequenceMethod(line.split(" ").map { value -> value.toInt() }) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114)

    val input = readInput("Day09")
    part1(input).println()

    check(part2(testInput) == 2)
    part2(input).println()
}
