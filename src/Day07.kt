import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Int {
        return Day07.part1(input)
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

object Day07 {
    fun part1(lines: List<String>) : Int {
        val hands = lines.map {
            Hand(it)
        }
        val handsResult = hands.sortedBy { it.strength() }
        handsResult.forEach { kotlin.io.println("Card ${it.bid} strength = ${it.strength()}") }
        return handsResult.mapIndexed { index, hand -> (index + 1) * hand.bid }.sum()
    }
    private fun Hand(string: String): Hand {
        val strings = string.split(" ")
        val cards = strings.first().map { Card(it.toLabel()) }
        val bid = strings.last().toInt()
        return Hand(bid, cards)
    }
    private fun Hand.type(): Type {
        val counts = Label.values().map { this.cards.count { card -> card.label == it } }
        if (counts.contains(5)) return Type.FIVE_OF_A_KIND
        if (counts.contains(4)) return Type.FOUR_OF_A_KIND
        if (counts.contains(3) && counts.contains(2)) return Type.FULL_HOUSE
        if (counts.contains(3)) return Type.THREE_OF_A_KIND
        if (counts.count { it == 2} == 2 ) return Type.TWO_PAIR
        if (counts.contains(2)) return Type.ONE_PAIR
        return Type.HIGH_CARD
    }
    private fun Hand.strength(): Int {
        val result = (this.type().ordinal + 1) * 1000000 + cards.cardLabelStrength()
        return result
    }

    private fun List<Card>.cardLabelStrength(): Int {
        val cards = this.reversed().map { it.label.ordinal + 1 }
        val newCardStrengths = listOf(
            cards[0] * 13.0.pow(0).toInt(),
            cards[1] * 13.0.pow(1).toInt(),
            cards[2] * 13.0.pow(2).toInt(),
            cards[3] * 13.0.pow(3).toInt(),
            cards[4] * 13.0.pow(4).toInt(),)
        return newCardStrengths.sum()
    }
    /*private fun Hand.rank() : Int {}
    private fun Hand.winnings(): Int {
        return rank() * bid
    }*/
    enum class Label {
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        TEN,
        J,
        Q,
        K,
        A,
    }

    private fun Char.toLabel(): Label {
        return when (this) {
            'A' -> Label.A
            'K' -> Label.K
            'Q' -> Label.Q
            'J' -> Label.J
            'T' -> Label.TEN
            '9' -> Label.NINE
            '8' -> Label.EIGHT
            '7' -> Label.SEVEN
            '6' -> Label.SIX
            '5' -> Label.FIVE
            '4' -> Label.FOUR
            '3' -> Label.THREE
            else -> Label.TWO
        }
    }

    enum class Type() {
        HIGH_CARD,
        ONE_PAIR,
        TWO_PAIR,
        THREE_OF_A_KIND,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        FIVE_OF_A_KIND
    }
    data class Card(val label: Label)
    data class Hand(val bid: Int, val cards: List<Card>)
}

