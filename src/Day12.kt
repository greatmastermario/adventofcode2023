fun main() {
    fun arrangements(corrupt: String, config: List<Int>, brokenCnt: Int = 0): Int {
        var count = 0
        var configIdx = 0
        var corruptIdx = 0
        var tempCnt = brokenCnt
        while (corruptIdx < corrupt.length && configIdx < config.size) {
            when (corrupt[corruptIdx]) {
                '#' -> {
                    for (hashCnt in tempCnt..<config[configIdx]) {
                        if (corruptIdx > corrupt.lastIndex || corrupt[corruptIdx++] == '.') {
                            return 0
                        }
                        tempCnt++
                    }
                    if (corruptIdx < corrupt.length && corrupt[corruptIdx] !in listOf('.', '?')) {
                        return 0
                    }
                }
                '.' -> {
                    when {
                        tempCnt == config[configIdx] -> {
                            tempCnt = 0
                            configIdx++
                            corruptIdx++
                        }
                        tempCnt > 0 -> return 0
                        else -> corruptIdx++
                    }
                }
                '?' -> {
                    count += arrangements(".${corrupt.slice(corruptIdx+1..corrupt.lastIndex)}",
                        config.slice(configIdx..config.lastIndex), tempCnt)
                    count += arrangements("#${corrupt.slice(corruptIdx+1..corrupt.lastIndex)}",
                        config.slice(configIdx..config.lastIndex), tempCnt)
                    return count
                }
            }
        }
        when {
            configIdx == config.lastIndex && tempCnt == config[configIdx] -> return 1
            tempCnt > 0 || configIdx < config.size ||
                    (corruptIdx < corrupt.length && corrupt.slice(corruptIdx..corrupt.lastIndex).any { it == '#' }) -> return 0
        }
        return 1
    }

    fun part1(input: List<String>): Int {
        var count = 0
        input.forEach {
            val split = it.split(" ")
            count += arrangements(split[0], split[1].split(",").map { config -> config.toInt() })
            count.println()
        }

        return count
    }

    fun part2(input: List<String>): Long {
        var count = 0L
        input.forEach {
            val split = it.split(" ")
            val configCondensed = split[1].split(",").map { config -> config.toInt() }
            var config = mutableListOf<Int>()
            for (repeat in 1..5) {
                config.addAll(configCondensed)
            }
            count += arrangements("${split[0]}?${split[0]}?${split[0]}?${split[0]}?${split[0]}", config)
            count.println()
        }

        return count
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 21)
    "PASS".println()

    val input = readInput("Day12")
    part1(input).println()
    part2(testInput).println()
}
