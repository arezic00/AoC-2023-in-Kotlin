import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

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
    private fun winCount(time: Long, distance: Long) : Int {
        val det = time.toDouble().pow(2) - 4 * distance
        if (det <= 0) return 0
        val detRoot = sqrt(det)
        val x1 = (time - detRoot)/2
        val x2 = (time + detRoot)/2
        val result = x2.toInt() - x1.toInt()
        return if (floor(x1) == x1 || floor(x2) == x2) result - 1 else result
    }

    fun parse1(lines: List<String>): Int =
        lines[0].removePrefix("Time: ").trim().split("\\s+".toRegex()).map { it.toLong() }
            .zip(lines[1].removePrefix("Distance: ").trim().split("\\s+".toRegex()).map { it.toLong() })
            .map { winCount(it.first, it.second) }
            .reduce { acc, i -> acc * i }

    fun parse2(lines: List<String>) = winCount(
        lines[0].removePrefix("Time: ").trim().split("\\s+".toRegex()).reduce { acc, s -> acc + s }.toLong(),
        lines[1].removePrefix("Distance: ").trim().split("\\s+".toRegex()).reduce { acc, s -> acc + s }.toLong())
}