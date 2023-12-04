import kotlin.math.pow

data class Card(val id: Int, val matches: Int)
fun main() {
    val test = Day04(readInput("Day04_test"))
    check(test.part1() == 13)
    check(test.part2() == 30)

    val solution = Day04(readInput("Day04"))
    solution.part1().println()
    solution.part2().println()
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
        return Card(id, numberSets[0].filter { numberSets[1].contains(it) }.size)
    }

    private fun List(numberSetString: String) : List<Int> =
        numberSetString.split("\\s+".toRegex()).map { it.toInt() }

    private fun Card.points() : Int = 2.0.pow(this.matches - 1).toInt()

    fun part1() : Int = cards.sumOf { it.points() }

    private fun Card.winsCardsWithSelf(): Int = (0 until this.matches).sumOf {
            cards[this.id + it].winsCardsWithSelf() } + 1

    fun part2() : Int = cards.sumOf { it.winsCardsWithSelf() }
}

