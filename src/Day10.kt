fun main() {
    fun pipeMap(input: List<String>): List<List<String>> {
        return input.map { it.split("").slice(1..it.length) }
    }

    fun findAnimal(map: List<List<String>>): Pair<Int, Int> {
        for (row in 0..map.lastIndex) {
            for (col in 0..map[row].lastIndex) {
                if ("S" == map[row][col]) {
                    return Pair(row, col)
                }
            }
        }
        return Pair(-1, -1)
    }

    fun pipeLength(map: List<List<String>>, animal: Pair<Int, Int>): List<Pair<Int, Int>> {
        var row = animal.first
        var col = animal.second
        var count = 1
        val pipeCoords = mutableListOf(animal)
        var dir = when {
            row > 0 && map[row - 1][col] in listOf("|", "7", "F") -> {
                row--
                when (map[row][col]) {
                    "|" -> "N"
                    "7" -> "E"
                    "F" -> "W"
                    else -> "UHOH"
                }
            }
            row < map.lastIndex && map[row + 1][col] in listOf("|", "J", "L") -> {
                row++
                when (map[row][col]) {
                    "|" -> "S"
                    "J" -> "E"
                    "L" -> "W"
                    else -> "UHOH"
                }
            }
            col < map[row].lastIndex && map[row][col + 1] in listOf("-", "J", "7") -> {
                col++
                when (map[row][col]) {
                    "-" -> "W"
                    "J" -> "N"
                    "7" -> "S"
                    else -> "UHOH"
                }
            }
            col > 0 && map[row][col - 1] in listOf("-", "L", "F") -> {
                col--
                when (map[row][col]) {
                    "-" -> "E"
                    "L" -> "N"
                    "F" -> "S"
                    else -> "UHOH"
                }
            }
            else -> "UHOH"
        }
        do {
            pipeCoords.add(Pair(row, col))
            dir = when (dir) {
                "N" -> {
                    row--
                    when (map[row][col]) {
                        "|" -> "N"
                        "7" -> "E"
                        "F" -> "W"
                        else -> "UHOH"
                    }
                }
                "S" -> {
                    row++
                    when (map[row][col]) {
                        "|" -> "S"
                        "J" -> "E"
                        "L" -> "W"
                        else -> "UHOH"
                    }
                }
                "W" -> {
                    col++
                    when (map[row][col]) {
                        "-" -> "W"
                        "J" -> "N"
                        "7" -> "S"
                        else -> "UHOH"
                    }
                }
                "E" -> {
                    col--
                    when (map[row][col]) {
                        "-" -> "E"
                        "L" -> "N"
                        "F" -> "S"
                        else -> "UHOH"
                    }
                }
                else -> "DONE"
            }
            count++
        } while (map[row][col] != "S")
        return pipeCoords
    }

    fun inside(map: List<List<String>>, pipe: List<Pair<Int, Int>>): Int {
        val pipeMap = map.toMutableList().map { it.map { "." }.toMutableList() }
        pipe.forEach { pipeMap[it.first][it.second] = map[it.first][it.second] }
        var insideCount = 0
        val excludeRow = listOf(".", "-")
        val south = listOf("F", "7")
        val north = listOf("J", "L")
        for (row in 0..map.lastIndex) {
            var inside = false
            var lineDir = ""
            for (col in 0..pipeMap[row].lastIndex) {
                if (pipeMap[row][col] !in excludeRow) {
                    if (pipeMap[row][col] == "|") {
                        inside = !inside
                        lineDir = ""
                    }
                    if (pipeMap[row][col] in south) {
                        if (lineDir == "") {
                            inside = !inside
                            lineDir = "south"
                        } else {
                            if (lineDir != "north") {
                                inside = !inside
                            }
                            lineDir = ""
                        }
                    }
                    if (pipeMap[row][col] in north) {
                        if (lineDir == "") {
                            inside = !inside
                            lineDir = "north"
                        } else {
                            if (lineDir != "south") {
                                inside = !inside
                            }
                            lineDir = ""
                        }
                    }
                }
                else if (pipeMap[row][col] == "." && inside) {
                    insideCount++
                    lineDir = ""
                }
            }
        }

        return insideCount
    }

    fun part1(input: List<String>): Int {
        val map = pipeMap(input)
        return pipeLength(map, findAnimal(map)).size / 2
    }

    fun part2(input: List<String>): Int {
        val map = pipeMap(input)
        return inside(map, pipeLength(map, findAnimal(map)))
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 8)

    val input = readInput("Day10")
    part1(input).println()

    val testInput1 = readInput("Day10_test1")
    check(part2(testInput1) == 10)
    part2(input).println()
}
