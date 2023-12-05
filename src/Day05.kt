import java.math.BigInteger

fun main() {
    fun part1(input: List<String>): Int {
        return Day05().part1(input).toInt()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35)


    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}

class Day05 {
    //map//
    //destination range start(50) - source range start(98) - range length(2)//
    // 98 - 99 (seed range 2nd) -> 50 -> 51(soil range 1st)  -48
    // 50 - 97 (seed range) -> 52 - 99 (soil range)  +2

    //----->map function if (seed) -> in (seed range) map function = (soil start - seed start) + (seed)



    // 0 - 49 (seed) -> 0 - 49 (soil)    (not mapped are same)

    //Any source numbers that aren't mapped correspond to the same destination number.
    //So, seed number 10 corresponds to soil number 10.

    //find lowst location from input seeds

    //need to read seeds - the map in every category

    private var seeds: List<BigInteger> = listOf()
    private var maps: List<Map> = mutableListOf()

    private fun parseSeeds(line: String): List<BigInteger> = line.removePrefix("seeds: ").split(" ").map { it.toBigInteger() }
    fun parse(lines: List<String>) {
        seeds = parseSeeds(lines.first())
        val mapStrings = lines.drop(3)
        val tempMaps = mutableListOf<Map>()
        var rows = mutableListOf<Triple<BigInteger,BigInteger,BigInteger>>()
        for ((index,line ) in mapStrings.withIndex()) {
            if (line.isNotEmpty() && line.first().isDigit()) {
                rows.add(Triple(line))
                if (index == mapStrings.size - 1) {
                    tempMaps.add(Map(rows))
                }
            }
            else if (line.isBlank()) {
                tempMaps.add(Map(rows))
                rows = mutableListOf()
            }
        }
        maps = tempMaps
    }
    private fun seedToLocation(seed: BigInteger): BigInteger {
        var location = seed
        maps.forEach { location = it.map(location) }
        return location
    }

    fun part1(lines: List<String>) : BigInteger {
        parse(lines)
        return seeds.minOf { seedToLocation(it) }
    }

    private fun Triple(line: String) : Triple<BigInteger,BigInteger,BigInteger> {
        val list = line.split(" ").map { it.toBigInteger() }
        return  Triple(list[0],list[1],list[2])
    }


}

class Map(val rows: MutableList<Triple<BigInteger,BigInteger,BigInteger>>) {
    fun map(input: BigInteger) : BigInteger {
        rows.forEach {
            if (input in (it.second ..(it.second + it.third).minus(BigInteger.ONE)))
                return it.first - it.second + input}
        return input
    }
}