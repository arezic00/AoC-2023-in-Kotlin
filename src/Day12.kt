fun main() {
    val input = readInput("Day12")
    val testInput = readInput("Day12_test")
    val day12 = Day12()

    val testResult1 = day12.part1(testInput)
    println("Test1 = $testResult1")
    check(testResult1 == 21L)
    println("Part1 = ${day12.part1(input)}")

    val testResult2 = day12.part2(testInput)
    println("\nTest2 = $testResult2")
    check(testResult2 == 525152L)
    println("Part2 = ${day12.part2(input)}")
}

private class Day12 {
    private val seen = mutableMapOf<Record, Long>()
    fun part1(lines: List<String>): Long = parse(lines).validArrangements()

    fun part2(lines: List<String>): Long = parse(lines).times5().validArrangements()

    private fun parse(lines: List<String>) = lines.map { recordString ->
        Record(
            recordString.substringBefore(' '),
            recordString.substringAfter(' ').split(',').map { it.toInt() })
    }

    private fun List<Record>.times5() = this.map {record ->
        Record(
            buildString {
                append(record.pattern)
                repeat(4) {append("?${record.pattern}")} }.trim('.'),
            buildList { repeat(5) {addAll(record.groups)} })
    }

    private fun List<Record>.validArrangements(): Long {
        return this.sumOf {
            seen.clear()
            it.validArrangements()
        }
    }

    private fun Record.validArrangements(): Long {
        seen[this]?.let { return it }
        if (groups.isEmpty()) {
            return  if (pattern.contains('#')) 0L else 1L
        }

        val len = pattern.length - groups.sumOf { it + 1 } + 1
        var sum = 0L
        for (i in 0 .. len) {
            if (this.canPlace(i)) {
                val subRecord = this.cut(i)
                sum += subRecord.validArrangements()
            }
        }
        seen[this] = sum
        return sum
    }

    private fun Record.canPlace(index: Int): Boolean {
        val broken = pattern.substring(index, index + groups.first())
        val start = pattern.substring(0,index)
        val end = if (groups.first() + index == pattern.length) '.' else pattern[groups.first() + index]
        return broken.all { it == '#' || it == '?' } &&  (end == '?' || end == '.')  && (start.isEmpty() || start.all { it == '?' || it == '.' })
    }

    private fun Record.cut(index: Int) = Record(this.pattern.drop(this.groups.first() + index + 1).trim('.'), this.groups.drop(1))

    private data class Record(val pattern: String, val groups: List<Int>)
}