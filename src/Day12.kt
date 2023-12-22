import kotlin.math.pow

fun main() {
    val input = readInput("Day12")
    val testInput = readInput("Day12_test")

    val testResult1 = part1(testInput)
    println("Test1 = $testResult1")
    check(testResult1 == 21L)
    println("Part1 = ${part1(input)}")

    val testResult2 = part2(testInput)
    println("\nTest2 = $testResult2")
    check(testResult2 == 525152L)
    println("Part2 = ${part2(input)}")
}

private fun part1(lines: List<String>): Long {
    val records = lines.map { recordString ->
        Record(
            recordString.substringBefore(' '),
            recordString.substringAfter(' ').split(',').map { it.toInt() })
    }
    return records.sumOf { it.validArrangements2() }
}

private fun Record.validArrangements(): Int {
    val questionMarkIndices = pattern.indices.filter { pattern[it] == '?' }
    val totalArrangements = 2.0.pow(questionMarkIndices.count()).toInt()

    fun String.isValid() : Boolean {
        var remaining = this
        for (group in groups) {
            remaining = remaining.dropWhile { it != '#' }
            if (remaining.substringBefore('.').length != group)
                return false
            remaining = remaining.drop(group)
        }
        return !(remaining.isNotEmpty() && remaining.contains('#'))
    }

    var result = 0
    for (arrangement in 0 until totalArrangements) {
        val binary = arrangement.toString(2).padStart(questionMarkIndices.count(),'0')
        val arrangedPattern = arrange(pattern, binary, questionMarkIndices)
        if (arrangedPattern.isValid())
            result++
    }

    return result
}

private fun arrange(pattern: String, arrangement: String, questionMarkIndices: List<Int>): String {
    return buildString {
        append(pattern)
        for ((bitIndex, qmIndex) in questionMarkIndices.withIndex())
        {
            replace(qmIndex, qmIndex + 1, if (arrangement[bitIndex] == '1') "." else "#")
        }
    }
}



private fun Record.validArrangements2(): Long {
    seen[this]?.let { return it }
    if (groups.isEmpty()) {
        return  if (pattern.contains('#')) 0L else 1L
    }

    val len = pattern.length - groups.sumOf { it + 1 } + 1
    var sum = 0L
    for (i in 0 .. len) {
        if (this.canPlace(i)) {
            val subRecord = this.cut(i)
            sum += subRecord.validArrangements2()
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

val seen = mutableMapOf<Record, Long>()
private fun List<Record>.validArrangements(): Long {
    return this.sumOf {
        seen.clear()
        it.validArrangements2()
    }
}
private fun part2(lines: List<String>): Long {
    val records = lines.map { recordString ->
        val pattern = recordString.substringBefore(' ')
        val groups = recordString.substringAfter(' ').split(',').map { it.toInt() }
        Record(
            buildString {
                append(pattern)
                repeat(4) {append("?$pattern")} }.trim('.'),
            buildList { repeat(5) {addAll(groups)} })
    }

    return records.validArrangements()

}

data class Record(val pattern: String, val groups: List<Int>)