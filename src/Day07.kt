import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Int {
        return Day07.part1(input)
    }

    fun part2(input: List<String>): Int {
        return Day07.part2(input)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

object Day07 {
    private const val BASE_TYPE_STRENGTH = 1000000
    private const val BASE_CARD_STRENGTH = 13.0
    fun part1(lines: List<String>) = lines.map { Hand(it) }
        .sortedBy { it.strength() }
        .mapIndexed { index, hand -> (index + 1) * hand.bid }.sum()

    fun part2(lines: List<String>) = lines.map { Hand(it) }
        .sortedBy { it.strength2() }
        .mapIndexed { index, hand -> (index + 1) * hand.bid }.sum()

    private fun Hand(string: String): Hand {
        val strings = string.split(" ")
        val cards = strings.first().map { Card(it) }
        val bid = strings.last().toInt()
        return Hand(bid, cards)
    }
    private fun Hand.type(): Type {
        val counts = Card.values().map { card -> this.cards.count { it == card } }
        if (counts.contains(5)) return Type.FIVE_OF_A_KIND
        if (counts.contains(4)) return Type.FOUR_OF_A_KIND
        if (counts.contains(3) && counts.contains(2)) return Type.FULL_HOUSE
        if (counts.contains(3)) return Type.THREE_OF_A_KIND
        if (counts.count { it == 2} == 2 ) return Type.TWO_PAIR
        if (counts.contains(2)) return Type.ONE_PAIR
        return Type.HIGH_CARD
    }

    private fun Hand.type2(): Type {
        val labels = Card.values().toMutableList()
        labels.removeAt(9)
        val jokerCount = cards.count { it == Card.J }
        if (jokerCount == 0) return this.type()

        val counts = labels.map { card -> cards.count { it == card } }
        if (jokerCount > 3 || counts.contains(4) || counts.contains(5))
            return Type.FIVE_OF_A_KIND

        if (counts.contains(3) && counts.contains(2))
            return when (jokerCount) {
                1 -> Type.FOUR_OF_A_KIND
                else -> Type.FIVE_OF_A_KIND
            }
        if (counts.contains(3))
            return when (jokerCount) {
                1 -> Type.FOUR_OF_A_KIND
                else -> Type.FIVE_OF_A_KIND
            }
        if (counts.count { it == 2} == 2 )
            return when (jokerCount) {
                1 -> Type.FULL_HOUSE
                2 -> Type.FOUR_OF_A_KIND
                else -> Type.FIVE_OF_A_KIND
            }
        if (counts.contains(2))
            return when (jokerCount) {
                1 -> Type.THREE_OF_A_KIND
                2 -> Type.FOUR_OF_A_KIND
                else -> Type.FIVE_OF_A_KIND
            }
        return when (jokerCount) {
            1 -> Type.ONE_PAIR
            2 -> Type.THREE_OF_A_KIND
            else -> Type.FOUR_OF_A_KIND
        }
    }
    private fun Hand.strength(): Int = type().ordinal * BASE_TYPE_STRENGTH + cards.cardLabelStrength()

    private fun Hand.strength2(): Int = this.type2().ordinal * BASE_TYPE_STRENGTH + cards.cardLabelStrength2()


    private fun List<Card>.cardLabelStrength() = reversed()
        .mapIndexed { index, card -> (card.ordinal) * BASE_CARD_STRENGTH.pow(index).toInt() }
        .sum()

    private fun List<Card>.cardLabelStrength2() = reversed()
        .mapIndexed { index, card -> (card.strength2()) * BASE_CARD_STRENGTH.pow(index).toInt()}
        .sum()

    private fun Card.strength2(): Int {
        return if (this < Card.J) this.ordinal + 1
        else if (this > Card.J) this.ordinal
        else 0
    }

    enum class Card {
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

    private fun Card(char: Char): Card {
        return when (char) {
            'A' -> Card.A
            'K' -> Card.K
            'Q' -> Card.Q
            'J' -> Card.J
            'T' -> Card.TEN
            '9' -> Card.NINE
            '8' -> Card.EIGHT
            '7' -> Card.SEVEN
            '6' -> Card.SIX
            '5' -> Card.FIVE
            '4' -> Card.FOUR
            '3' -> Card.THREE
            else -> Card.TWO
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
    data class Hand(val bid: Int, val cards: List<Card>)
}

