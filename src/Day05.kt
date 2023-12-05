import kotlin.math.min

data class Mapper(val destination: Long, val source: Long, val range: Long)

data class Almanac(val seeds: List<Long>, val seedToSoil: List<Mapper>, val soilToFertilizer: List<Mapper>,
    val fertilizerToWater: List<Mapper>, val waterToLight: List<Mapper>, val listToTemp: List<Mapper>,
    val tempToHumidity: List<Mapper>, val humidityToLocation: List<Mapper>)

data class RangedAlmanac(val seeds: List<Pair<Long, Long>>, val seedToSoil: List<Mapper>, val soilToFertilizer: List<Mapper>,
                   val fertilizerToWater: List<Mapper>, val waterToLight: List<Mapper>, val listToTemp: List<Mapper>,
                   val tempToHumidity: List<Mapper>, val humidityToLocation: List<Mapper>)

fun main() {
    fun createAlmanac(input: List<String>): Almanac {
        val seeds = mutableListOf<Long>()
        val mappers = mutableMapOf<String, List<Mapper>>()
        var currentMappers = mutableListOf<Mapper>()
        var currentKey = ""
        for (line in input) {
            when {
                line.startsWith("seeds: ") -> seeds.addAll(line.split("seeds: ")[1].split(" ").map { it.toLong() })
                line.endsWith(":") -> currentKey = line
                currentKey.isNotBlank() && line == "" -> {
                    mappers[currentKey] = currentMappers
                    currentMappers = mutableListOf()
                }
                line.isNotBlank() -> {
                    val split = line.split(" ")
                    currentMappers.add(Mapper(split[0].toLong(), split[1].toLong(), split[2].toLong()))
                }
            }
        }
        val defaultMapper = listOf(Mapper(0L, 0L, 0L))
        return Almanac(seeds, mappers["seed-to-soil map:"]?:defaultMapper, mappers["soil-to-fertilizer map:"]?:defaultMapper,
            mappers["fertilizer-to-water map:"]?:defaultMapper, mappers["water-to-light map:"]?:defaultMapper,
            mappers["light-to-temperature map:"]?:defaultMapper, mappers["temperature-to-humidity map:"]?:defaultMapper,
            mappers["humidity-to-location map:"]?:defaultMapper)
    }

    fun createRangedAlmanac(input: List<String>): RangedAlmanac {
        val seeds = mutableListOf<Pair<Long, Long>>()
        val mappers = mutableMapOf<String, List<Mapper>>()
        var currentMappers = mutableListOf<Mapper>()
        var currentKey = ""
        for (line in input) {
            when {
                line.startsWith("seeds: ") -> {
                    val seedVals = line.split("seeds: ")[1].split(" ").map { it.toLong() }
                    for (idx in 0..seedVals.lastIndex step 2) {
                        seeds.add(Pair(seedVals[idx], seedVals[idx+1]))
                    }
                }
                line.endsWith(":") -> currentKey = line
                currentKey.isNotBlank() && line == "" -> {
                    mappers[currentKey] = currentMappers
                    currentMappers = mutableListOf()
                }
                line.isNotBlank() -> {
                    val split = line.split(" ")
                    currentMappers.add(Mapper(split[0].toLong(), split[1].toLong(), split[2].toLong()))
                }
            }
        }
        val defaultMapper = listOf(Mapper(0L, 0L, 0L))
        return RangedAlmanac(seeds, mappers["seed-to-soil map:"]?:defaultMapper, mappers["soil-to-fertilizer map:"]?:defaultMapper,
            mappers["fertilizer-to-water map:"]?:defaultMapper, mappers["water-to-light map:"]?:defaultMapper,
            mappers["light-to-temperature map:"]?:defaultMapper, mappers["temperature-to-humidity map:"]?:defaultMapper,
            mappers["humidity-to-location map:"]?:defaultMapper)
    }

    fun mapToNext(previous: Long, mappers: List<Mapper>): Long {
        mappers.forEach {
            if (previous in it.source..<it.source+it.range) {
                return it.destination + previous - it.source
            }
        }
        return previous
    }

    fun mapToNextRange(previous: List<Pair<Long, Long>>, mappers: List<Mapper>): List<Pair<Long, Long>> {
        val orderedPrevious = previous.sortedBy { it.first }
        val orderedMappers = mappers.sortedBy { it.source }
        val nextRanges = mutableListOf<Pair<Long, Long>>()
        for (pair in orderedPrevious) {
            val nextRangesStartingSize = nextRanges.size
            var start = pair.first
            for (idx in 0..orderedMappers.lastIndex) {
                val mapper = orderedMappers[idx]
                if (start > mapper.source+mapper.range-1) {
                    continue
                }
                if (start !in mapper.source..<mapper.source+mapper.range) {
                    val end = min(mapper.source - 1, pair.first + pair.second - 1)
                    val range = end - start + 1
                    nextRanges.add(Pair(start, range))
                    start = end + 1
                }
                if (start in mapper.source..<mapper.source+mapper.range) {
                    val end = min(mapper.source+mapper.range - 1, pair.first + pair.second - 1)
                    val diff = mapper.destination - mapper.source
                    val range = end - start + 1
                    nextRanges.add(Pair(start + diff, range))
                    start = end + 1
                }
                if (start > pair.first + pair.second - 1) {
                    break
                }
            }
            if (nextRangesStartingSize == nextRanges.size) {
                nextRanges.add(pair)
            }
        }
        return nextRanges
    }

    fun computeDistance(seed: Long, almanac: Almanac): Long {
        return mapToNext(mapToNext(mapToNext(mapToNext(mapToNext(mapToNext(mapToNext(seed, almanac.seedToSoil), almanac.soilToFertilizer), almanac.fertilizerToWater), almanac.waterToLight), almanac.listToTemp), almanac.tempToHumidity), almanac.humidityToLocation)
    }

    fun computeRangedDistance(seedRange: List<Pair<Long, Long>>, almanac: RangedAlmanac): List<Pair<Long,Long>> {
        return mapToNextRange(mapToNextRange(mapToNextRange(mapToNextRange(mapToNextRange(mapToNextRange(mapToNextRange(seedRange, almanac.seedToSoil), almanac.soilToFertilizer), almanac.fertilizerToWater), almanac.waterToLight), almanac.listToTemp), almanac.tempToHumidity), almanac.humidityToLocation)
    }

    fun part1(input: List<String>): Long {
        val almanac = createAlmanac(input)
        val distances = mutableSetOf<Long>()
        almanac.seeds.forEach { distances.add(computeDistance(it, almanac)) }
        return distances.min()
    }

    fun part2(input: List<String>): Long {
        val almanac = createRangedAlmanac(input)
        val distances = mutableSetOf<Long>()
        computeRangedDistance(almanac.seeds, almanac).forEach { distances.add(it.first) }
        return distances.min()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)

    val input = readInput("Day05")
    part1(input).println()

    check(part2(testInput) == 46L)
    part2(input).println()
}
