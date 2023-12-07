fun main() {
    fun part1(input: List<String>): Int {
        return Day08.part1(input)
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 1)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}

object Day08 {
    fun part1(lines: List<String>): Int {

        return 0
    }
}