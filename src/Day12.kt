import kotlin.math.pow

fun main() {
    val input = readInput("Day12")
    val testInput = readInput("Day12_test")

    val testResult1 = part1(testInput)
    println("Test1 = $testResult1")
    check(testResult1 == 21)
    println("Part1 = ${part1(input)}")

//    val testResult2 = part2(testInput)
//    println("\nTest2 = $testResult2")
//    check(testResult2 == 1)
//    println("Part2 = ${part2(input)}")
}

private fun part1(lines: List<String>): Int {
    val records = lines.map { recordString ->
        Record(
            recordString.substringBefore(' '),
            recordString.substringAfter(' ').split(',').map { it.toInt() })
    }
    return records.sumOf { it.validArrangements() }
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








private fun part2(lines: List<String>) =
    0

private data class Record(val pattern: String, val groups: List<Int>)