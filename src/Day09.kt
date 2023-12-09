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
    part2(testInput).println()
    check(part2(testInput) == 2L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}

object Day09 {
    fun part1(lines: List<String>): Long {
        val histories = lines.map { it.split(" ").map { it.toLong() } }
        return histories.map { extrapolateValue(it) }.sum()
    }

    private fun listOfDifferences(list: List<Long>) : List<Long> {
        val result = mutableListOf<Long>()
        for (index in 1 until list.size) {
            val difference = list[index] - list[index - 1]
            result.add(difference)
        }
        return result
    }

    private fun extrapolateValue(history: List<Long>): Long {
        val lists = mutableListOf(history)
        while (lists.last().any { it != 0L }) {
            lists.add(listOfDifferences(lists.last()))
        }
        return lists.map { it.last() }.runningReduce { acc, l -> acc + l }.last()
    }

    fun part2(lines: List<String>): Long {
        val histories = lines.map { it.split(" ").map { it.toLong() } }
        return histories.map { extrapolateValueBackwards(it) }.sum()
    }

    private fun extrapolateValueBackwards(history: List<Long>): Long {
        val lists = mutableListOf(history)
        while (lists.last().any { it != 0L }) {
            lists.add(listOfDifferences(lists.last()))
        }
        return lists.reversed().map { it.first() }.runningReduce { acc, l -> l - acc }.last()
    }


}