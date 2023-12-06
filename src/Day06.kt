import kotlin.math.*

fun main() {
    fun parseLine(line: String): List<Int> {
        return line.split(":\\s+".toRegex())[1].split("\\s+".toRegex()).map { it.toInt() }
    }

    fun parseLineKerning(line: String): Int {
        val parts = line.split(":\\s+".toRegex())[1].split("\\s+".toRegex())
        var final = ""
        for (part in parts) {
            final += part
        }
        return final.toInt()
    }

    fun getRecordCount(time: Int, distance: Int): Int {
        val time1 = (-time + sqrt((time*time - 4*distance - 4).toDouble())) / -2
        val time2 = (-time - sqrt((time*time - 4*distance - 4).toDouble())) / -2
        return floor(max(time1, time2)).toInt() - ceil(min(time1, time2)).toInt() + 1
    }

    fun part1(input: List<String>): Int {
        val times = parseLine(input[0])
        val distance = parseLine(input[1])
        var records = 1
        for (idx in 0..times.lastIndex) {
            records *= getRecordCount(times[idx], distance[idx])
        }
        return records
    }

    fun part2(input: List<String>): Int {
        val time = parseLineKerning(input[0])
        val distance = parseLineKerning(input[1])
        return getRecordCount(time, distance)
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288)

    val input = readInput("Day06")
    part1(input).println()

    check(part2(testInput) == 71503)
    part2(input).println()
}
