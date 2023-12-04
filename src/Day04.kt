import kotlin.math.pow

data class Card(val id: Int, val winningNumbers: List<Int>, val numbers: List<Int>)
fun main() {
    fun part1(input: List<String>): Int {
        return Day04(input).solvePart1()
    }

    fun part2(input: List<String>): Int {
        return Day04(input).solvePart2()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

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
        numberSetString.split("\\s+".toRegex()).map { it.toInt() }

    private fun Card.matches() : Int = this.winningNumbers.filter { numbers.contains(it) }.size

    private fun Card.points() : Int = 2.0.pow(this.matches() - 1).toInt()

    fun solvePart1() : Int = cards.sumOf { it.points() }

    private fun Card.winsCardsWithSelf(): Int {
        return (0 until this.matches()).sumOf {
            cards[this.id + it].winsCardsWithSelf()
        } + 1
    }

    fun solvePart2() : Int{
        return cards.sumOf { it.winsCardsWithSelf() }
    }
    //Card 1 -> matches = 4 -> 1 + c2() + c3() + c4() + c5() -> 1 + 7 + 4 + 2 + 1 -> 15
    //Card 2 -> matches = 2 -> 1 + c3() + c4() -> 1 + 4 + 2 -> 7
    //Card 3 -> matches = 2 -> 1 + c4() + c5() -> 1 + 2 + 1 -> 4
    //Card 4 -> matches = 1 -> 1 + c5() -> 1 + 1 -> 2
    //Card 5 -> matches = 0 -> 1
    //Card 6 -> matches = 0 -> 1

}

