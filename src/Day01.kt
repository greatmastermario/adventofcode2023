fun main() {
    fun part1(input: List<String>): Int {
        val nums = mutableListOf<Int>()
        input.forEach {
            var numStr = ""
            for (char in it) {
                if (char in '0'..'9') {
                    numStr += char
                    break
                }
            }
            for (char in it.reversed()) {
                if (char in '0'..'9') {
                    numStr += char
                    break
                }
            }
            nums.add(numStr.toInt())
        }
        return nums.sum()
    }

    fun part2(input: List<String>): Int {
        val nums = mutableListOf<Int>()
        val numMap = mapOf("1" to 1, "2" to 2, "3" to 3, "4" to 4, "5" to 5, "6" to 6, "7" to 7, "8" to 8, "9" to 9, "0" to 0,
            "one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5, "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9, "zero" to 0)
        input.forEach {
            var numStr = ""
            for (index in it.indices) {
                var slice = it.slice(index..<it.length)
                for (key in numMap.keys) {
                    if (slice.startsWith(key)) {
                        numStr += numMap[key]
                        break
                    }
                }
                if (numStr.length == 1) {
                    break
                }
            }
            for (index in it.indices.reversed()) {
                var slice = it.slice(0..index)
                for (key in numMap.keys) {
                    if (slice.endsWith(key)) {
                        numStr += numMap[key]
                        break
                    }
                }
                if (numStr.length == 2) {
                    break
                }
            }
            nums.add(numStr.toInt())
        }
        return nums.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val input = readInput("Day01")
    part1(input).println()

    val testInput2 = readInput("Day01_test2")
    check(part2(testInput2) == 281)
    part2(input).println()
}
