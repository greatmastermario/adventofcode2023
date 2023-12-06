import kotlin.math.*

fun main() {
    fun parseLine(line: String): List<Long> {
        return line.split(":\\s+".toRegex())[1].split("\\s+".toRegex()).map { it.toLong() }
    }

    fun parseLineKerning(line: String): Long {
        val parts = line.split(":\\s+".toRegex())[1].split("\\s+".toRegex())
        var final = ""
        for (part in parts) {
            final += part
        }
        return final.toLong()
    }

    fun getRecordCount(time: Long, distance: Long): Long {
        val time1 = (-time + sqrt((time*time - 4*distance - 4).toDouble())) / -2
        val time2 = (-time - sqrt((time*time - 4*distance - 4).toDouble())) / -2
        return floor(max(time1, time2)).toLong() - ceil(min(time1, time2)).toLong() + 1
    }

    fun part1(input: List<String>): Long {
        val times = parseLine(input[0])
        val distance = parseLine(input[1])
        var records = 1L
        for (idx in 0..times.lastIndex) {
            records *= getRecordCount(times[idx], distance[idx])
        }
        return records
    }

    fun part2(input: List<String>): Long {
        val time = parseLineKerning(input[0])
        val distance = parseLineKerning(input[1])
        return getRecordCount(time, distance)
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288L)

    val input = readInput("Day06")
    part1(input).println()

    check(part2(testInput) == 71503L)
    part2(input).println()
}
