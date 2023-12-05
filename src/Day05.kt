fun main() {
    fun part1(input: List<String>): Int {
        return Day05().part1(input)
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

    private var seeds: List<Int> = listOf()
    private var maps: List<Map> = mutableListOf()

    private fun parseSeeds(line: String): List<Int> = line.removePrefix("seeds: ").split(" ").map { it.toInt() }
    fun parse(lines: List<String>) {
        seeds = parseSeeds(lines.first())
        val mapStrings = lines.drop(3)
        val tempMaps = mutableListOf<Map>()
        var rows = mutableListOf<Triple<Int,Int,Int>>()
        for ((index,line ) in mapStrings.withIndex()) {
            kotlin.io.println("For loop: $index")
            if (line.isNotEmpty() && line.first().isDigit()) {
                rows.add(Triple(line))
                println(Triple(line))
                if (index == mapStrings.size - 1) {
                    tempMaps.add(Map(rows))
                }
            }
            else if (line.isBlank()) {
                tempMaps.add(Map(rows))
                rows = mutableListOf()
            }
            tempMaps.forEach { it.rows.println() }
        }
        maps = tempMaps
    }
    private fun seedToLocation(seed: Int): Int {
        var location = seed
        maps.forEach { location = it.map(location) }
        return location
    }

    fun part1(lines: List<String>) : Int {
        parse(lines)
        return seeds.minOf { seedToLocation(it) }
    }

    private fun Triple(line: String) : Triple<Int,Int,Int> {
        val list = line.split(" ").map { it.toInt() }
        return  Triple(list[0],list[1],list[2])
    }


}

class Map(val rows: MutableList<Triple<Int,Int,Int>>) {
    fun map(input: Int) : Int {
        rows.forEach {
            if (input in (it.second until(it.second + it.third)))
                return it.first - it.second + input}
        return input
    }
}