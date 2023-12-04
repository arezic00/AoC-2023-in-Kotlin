import kotlin.math.pow

data class Card(val id: Int, val winningNumbers: List<Int>, val numbers: List<Int>)
fun main() {
    fun part1(input: List<String>): Int {
        return Day04(input).solvePart1()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)

    val input = readInput("Day04")
    part1(input).println()
    //part2(input).println()
}

class Day04(lines: List<String>) {
    private val cards = lines.map { Card(it) }
    private fun Card(line: String) : Card {
        val id = line.removePrefix("Card ").trim().takeWhile { it.isDigit() }.toInt()
        val numberSets = line
            .dropWhile { it != ':' }
            .removePrefix(": ")
            .split(" | ")
            .map { List(it.trim()) }
        return Card(id, numberSets[0], numberSets[1])
    }

    private fun List(numberSetString: String) : List<Int> =
        numberSetString.split("  ", " ").map { it.toInt() }

    private fun Card.matches() : List<Int> = this.winningNumbers.filter { numbers.contains(it) }

    private fun Card.points() : Int = 2.0.pow(this.matches().size - 1).toInt()

    fun solvePart1() : Int = cards.sumOf { it.points() }
}

