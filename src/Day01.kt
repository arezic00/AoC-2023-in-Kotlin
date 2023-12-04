import Day01.part1
import Day01.part2

fun main() {
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)
    val testInput2 = readInput("Day01_test2")
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

object Day01 {
    private val words = listOf("zero","one","two","three","four","five","six","seven","eight","nine")

    fun part1(lines: List<String>) = lines.sumOf { lineToCalibrationValue(it) }
    private fun lineToCalibrationValue(line: String) : Int {
        val first = line.dropWhile { !(it.isDigit()) }.first()
        val last = line.dropLastWhile { !(it.isDigit()) }.last()
        return first.digitToInt() * 10 + last.digitToInt()
    }
    private fun firstDigitWithIndex(line: String) : Pair<Int,Int> {
        line.forEachIndexed { index, c ->
            if (c.isDigit()) return Pair(c.digitToInt(),index)
        }
        return Pair(-1,-1)
    }
    private fun lastDigitWithIndex(line: String) : Pair<Int,Int> {
        line.reversed().forEachIndexed { index, c ->
            if (c.isDigit()) return Pair(c.digitToInt(),index)
        }
        return Pair(-1,-1)
    }
    private fun firstWordWithIndex(line: String) : Pair<String,Int> {
        var firstWordIndex = 9999
        var firstWord = ""
        words.forEach {
            val index = line.indexOf(it)
            if (index != -1 && index < firstWordIndex) {
                firstWordIndex = index
                firstWord = it
            }
        }
        return Pair(firstWord,firstWordIndex)
    }

    private fun lastWordWithIndex(line: String) : Pair<String,Int> {
        var firstWordIndex = 9999
        var firstWord = ""
        words.forEach {
            val index = line.reversed().indexOf(it.reversed())
            if (index != -1 && index < firstWordIndex) {
                firstWordIndex = index
                firstWord = it
            }
        }
        return Pair(firstWord,firstWordIndex)
    }

    private fun lineToCalibrationValue2(line: String) : Int {
        val firstWordPair = firstWordWithIndex(line)
        val firstDigitPair = firstDigitWithIndex(line)

        if (firstDigitPair.second == -1 && firstWordPair.second == 9999)
            return 0

        if (firstDigitPair.second == -1) {
            return words.indexOf(firstWordPair.first) * 10 + words.indexOf(lastWordWithIndex(line).first)
        }
        else if (firstWordPair.second == 9999) {
            return firstDigitPair.first * 10 + lastDigitWithIndex(line).first
        }

        val firstDigit : Int = if (firstDigitPair.second < firstWordPair.second) firstDigitPair.first else words.indexOf(firstWordPair.first)
        val lastWordPair = lastWordWithIndex(line)
        val lastDigitPair = lastDigitWithIndex(line)
        val lastDigit : Int = if (lastDigitPair.second < lastWordPair.second) lastDigitPair.first else words.indexOf(lastWordPair.first)
        return firstDigit * 10 + lastDigit
    }
    fun part2(lines: List<String>) :Int = lines.sumOf { lineToCalibrationValue2(it) }
}