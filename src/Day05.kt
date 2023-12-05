fun main() {
    fun part1(input: List<String>): Int {
        return Day05().part1(input).toInt()
   }

    fun part2(input: List<String>): Int {
        return Day05().part2(input).toInt()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35)
    check(part2(testInput) == 46)


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

    private var seeds: List<UInt> = listOf()
    private var maps: List<Map> = mutableListOf()
    private var seeds2: List<Pair<UInt,UInt>> = mutableListOf()

    private fun parseSeeds1(line: String): List<UInt> =
        line.removePrefix("seeds: ").split(" ").map { it.toUInt() }
    private fun parseSeeds2(line: String) : List<Pair<UInt,UInt>> =
        parseSeeds1(line).chunked(2) {Pair(it.first(),it.first() + it.last() - 1u)}

    private fun parse(lines: List<String>) {
        val mapStrings = lines.drop(3)
        val tempMaps = mutableListOf<Map>()
        var rows = mutableListOf<Triple<UInt,UInt,UInt>>()
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
    private fun seedToLocation(seed: UInt): UInt {
        var location = seed
        maps.forEach { location = it.map(location) }
        return location
    }

    fun part1(lines: List<String>) : UInt {
        seeds = parseSeeds1(lines.first())
        parse(lines)
        return seeds.minOf { seedToLocation(it) }
    }

    private fun Triple(line: String) : Triple<UInt,UInt,UInt> {
        val list = line.split(" ").map { it.toUInt() }
        return  Triple(list[0],list[1],list[2])
    }

    fun part2(lines: List<String>) : UInt {
        seeds2 = parseSeeds2(lines.first())
        seeds2.println()
        parse(lines)
        "parsed".println()
        var min = UInt.MAX_VALUE
        seeds2.forEach {
            for (seed in Seeds2Iterator(it) ) {
                val location = seedToLocation(seed)
                if (location < min)
                    min = location
            }
            kotlin.io.println("Completed seeds2 from ${it.first}")
        }
        min.println()
        return min
    }




}

class Map(val rows: MutableList<Triple<UInt,UInt,UInt>>) {
    fun map(input: UInt) : UInt {
        rows.forEach {
            if (input in (it.second ..(it.second + it.third) - 1u))
                return it.first - it.second + input}
        return input
    }
}

class Seeds2Iterator(private val range:Pair<UInt,UInt>) : Iterator <UInt> {
    private var current = range.first
    override fun hasNext(): Boolean {
        return current <= range.second
    }

    override fun next(): UInt {
        if (!hasNext()) {
            throw NoSuchElementException()
        }
        return current++
    }
}