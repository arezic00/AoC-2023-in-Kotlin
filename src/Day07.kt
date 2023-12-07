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
    fun part1(lines: List<String>) : Int {
        val hands = lines.map {
            Hand(it)
        }
        val handsResult = hands.sortedBy { it.strength() }
        return handsResult.mapIndexed { index, hand -> (index + 1) * hand.bid }.sum()
    }

    fun part2(lines: List<String>) : Int {
        val hands = lines.map {
            Hand(it)
        }
        val handsResult = hands.sortedBy { it.strength2() }
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

    //Ordinal of joker is 9
    private fun Hand.type2(): Type {
        val labels = Label.values().toMutableList()
        labels.removeAt(9)
        val jokerCount = cards.count { it.label == Label.J }
        val counts = labels.map { cards.count { card -> card.label == it } }
        if (counts.contains(5)) return Type.FIVE_OF_A_KIND
        if (counts.contains(4))
            return if (jokerCount > 0) Type.FIVE_OF_A_KIND else Type.FOUR_OF_A_KIND
        if (counts.contains(3) && counts.contains(2))
            return when (jokerCount) {
                0 -> Type.FULL_HOUSE
                1 -> Type.FOUR_OF_A_KIND
                else -> Type.FIVE_OF_A_KIND
            }
        if (counts.contains(3))
            return when (jokerCount) {
                0 -> Type.THREE_OF_A_KIND
                1 -> Type.FOUR_OF_A_KIND
                else -> Type.FIVE_OF_A_KIND
            }
        if (counts.count { it == 2} == 2 )
            return when (jokerCount) {
                0 -> Type.TWO_PAIR
                1 -> Type.FULL_HOUSE
                2 -> Type.FOUR_OF_A_KIND
                else -> Type.FIVE_OF_A_KIND
            }
        if (counts.contains(2))
            return when (jokerCount) {
                0 -> Type.ONE_PAIR
                1 -> Type.THREE_OF_A_KIND
                2 -> Type.FOUR_OF_A_KIND
                else -> Type.FIVE_OF_A_KIND
            }
        //0->OP, 1->TH, 2->FR, 3->FV
        return when (jokerCount) {
            0 -> Type.HIGH_CARD
            1 -> Type.ONE_PAIR
            2 -> Type.THREE_OF_A_KIND
            3 -> Type.FOUR_OF_A_KIND
            else -> Type.FIVE_OF_A_KIND
        }
    }

    private fun Hand.strength(): Int = (type().ordinal + 1) * 1000000 + cards.cardLabelStrength()

    private fun Hand.strength2(): Int = (type2().ordinal + 1) * 1000000 + cards.cardLabelStrength2()


    private fun List<Card>.cardLabelStrength() = reversed()
        .mapIndexed { index, card -> (card.label.ordinal + 1) * 13.0.pow(index).toInt() }
        .sum()

    private fun List<Card>.cardLabelStrength2() = reversed()
        .mapIndexed { index, card ->
        if (card.label == Label.J)
            0
        else
            (card.strength2()) * 13.0.pow(index).toInt()}
        .sum()

    private fun Card.strength2(): Int {
        return if (this.label < Label.J) this.label.ordinal + 1
        else if (this.label > Label.J) this.label.ordinal
        else 0
    }


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

