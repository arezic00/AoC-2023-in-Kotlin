fun main() {
    val input = readInput("Day13")
    val testInput = readInput("Day13_test")

    val testResult1 = part1(testInput)
    println("Test1 = $testResult1")
    check(testResult1 == 405)
    println("Part1 = ${part1(input)}")

    val testResult2 = part2(testInput)
    println("\nTest2 = $testResult2")
    check(testResult2 == 400)
    println("Part2 = ${part2(input)}")
}

private fun parse (lines: List<String>): List<List<String>> {
    val patterns = mutableListOf<List<String>>()
    var rows = mutableListOf<String>()
    for ((index, line) in lines.withIndex()) {
        if (line.isNotEmpty()){
            rows.add(line)
        }
        if (line.isEmpty() || index == lines.lastIndex) {
            patterns.add(rows)
            rows = mutableListOf()
        }
    }
    return patterns
}

private fun part1(lines: List<String>)= parse(lines).sumOf { summarize(it) }

private fun summarize(pattern: List<String>) : Int {
    val result = mirrorIndex(pattern) * 100
    return if (result == 0) mirrorIndex(pattern.columns()) else result
}

private fun mirrorIndex(pattern: List<String>): Int {
    for (index in 1..pattern.lastIndex) {
        var curIndex = index
        var prevIndex = index - 1
        var previous = pattern[prevIndex]
        var current = pattern[curIndex]
        while (previous == current) {
            if (prevIndex == 0 || curIndex == pattern.lastIndex)
                return index
            prevIndex--
            curIndex++
            previous = pattern[prevIndex]
            current = pattern[curIndex]
        }
    }
    return 0
}

private fun newMirrorIndex(pattern: List<String>, oldIndex: Int): Int {
    for (index in 1..pattern.lastIndex) {
        var curIndex = index
        var prevIndex = index - 1
        var previous = pattern[prevIndex]
        var current = pattern[curIndex]
        while (previous == current) {
            if (prevIndex == 0 || curIndex == pattern.lastIndex)
                if (index != oldIndex)
                    return index
                else
                    break
            prevIndex--
            curIndex++
            previous = pattern[prevIndex]
            current = pattern[curIndex]
        }
    }
    return 0
}

private fun List<String>.columns() : List<String> {
    val result = mutableListOf<String>()
    for (col in this.first().indices) {
        var resultString = ""
        for (row in this.indices) {
            resultString += this[row][col]
        }
        result.add(resultString)
    }
    return result
}

private fun summarize2(pattern: List<String>): Int {
    val oldRow = mirrorIndex(pattern)
    val oldCol = mirrorIndex(pattern.columns())

    for (row in pattern.indices) {
        for (col in pattern[row].indices) {
            val newPattern = pattern.flip(row,col)
            val newRow = newMirrorIndex(newPattern, oldRow)
            if (newRow != 0)
                return newRow * 100
            val newCol = newMirrorIndex(newPattern.columns(), oldCol)
            if (newCol != 0)
                return newCol
        }
    }
    return if (oldRow == 0) oldCol else oldRow * 100
}

private fun List<String>.flip(row: Int, col: Int) : List<String> {
    val result = this.toMutableList()
    val newChar = if (this[row][col] == '.') '#' else '.'
    var newString = ""
    for (index in this[row].indices)
        newString += if (index == col) newChar else this[row][index]
    result[row] = newString
    return result
}

private fun part2(lines: List<String>) = parse(lines).sumOf { summarize2(it) }