fun main() {
    fun part1(input: List<String>): Long {
        return Day09.part1(input)
    }

    fun part2(input: List<String>): Long {
        return Day09.part2(input)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114L)
    check(part2(testInput) == 2L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}

object Day09 {
    fun part1(lines: List<String>): Long {
        val histories = lines.map { line -> line.split(" ").map { it.toLong() } }
        return histories.sumOf { nextValue(it) }
    }

    private fun listOfDifferences(list: List<Long>) : List<Long> {
        val result = mutableListOf<Long>()
        for (index in 1 until list.size) {
            val difference = list[index] - list[index - 1]
            result.add(difference)
        }
        return result
    }

    private fun nextValue(history: List<Long>): Long {
        val lists = mutableListOf(history)
        while (lists.last().any { it != 0L }) {
            lists.add(listOfDifferences(lists.last()))
        }
        return lists.map { it.last() }.reduce { acc, l -> acc + l }
    }

    fun part2(lines: List<String>): Long {
        val histories = lines.map { line -> line.split(" ").map { it.toLong() } }
        return histories.sumOf { previousValue(it) }
    }

    private fun previousValue(history: List<Long>): Long {
        val lists = mutableListOf(history)
        while (lists.last().any { it != 0L }) {
            lists.add(listOfDifferences(lists.last()))
        }
        return lists.reversed().map { it.first() }.reduce { acc, l -> l - acc }
    }


}