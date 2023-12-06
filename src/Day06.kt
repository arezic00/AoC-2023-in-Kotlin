fun main() {
    fun part1(input: List<String>): Int {
        return Day06.parse1(input)
    }

    fun part2(input: List<String>): Int {
        return Day06.parse2(input)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288)
    check(part2(testInput) == 71503)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}

object Day06 {
    private fun winCount(time: Long, distance: Long) = (1 until time).count { (time - it)*it > distance }
    fun parse1(lines: List<String>): Int =
        lines[0].removePrefix("Time: ").trim().split("\\s+".toRegex()).map { it.toLong() }
            .zip(lines[1].removePrefix("Distance: ").trim().split("\\s+".toRegex()).map { it.toLong() })
            .map { winCount(it.first, it.second) }
            .reduce { acc, i -> acc * i }

    fun parse2(lines: List<String>) = winCount(
        lines[0].removePrefix("Time: ").trim().split("\\s+".toRegex()).reduce { acc, s -> acc + s }.toLong(),
        lines[1].removePrefix("Distance: ").trim().split("\\s+".toRegex()).reduce { acc, s -> acc + s }.toLong())
}