fun main() {
    val input = readInput("Day14")
    val testInput = readInput("Day14_test")

    val testResult1 = part1(testInput)
    println("Test1 = $testResult1")
    check(testResult1 == 136)
    println("Part1 = ${part1(input)}")

    val testResult2 = part2(testInput)
    println("\nTest2 = $testResult2")
    check(testResult2 == 64)
    println("Part2 = ${part2(input)}")
}

private fun part1(lines: List<String>) = lines.rotateCCW().tilt().rotateCW().load()

private fun String.tilt() : String {
    val result = this.toMutableList()
    for (index in result.indices) {
        if (result[index] == 'O')
            continue
        if (result[index] == '.') {
            val nextCube =result.subList(index + 1, result.size).indexOfFirst { it == '#' }
            val endIndex = if (nextCube == - 1) result.lastIndex else nextCube + index + 1

            val rockSubIndex = result.subList(index + 1, endIndex + 1).indexOfFirst { it == 'O' }
            if (rockSubIndex != - 1) {
                val firstRockIndex =  rockSubIndex + index + 1
                result[index] = 'O'
                result[firstRockIndex] = '.'
            }
        }

    }
    return result.fold("") { acc, c -> acc + c }
}

private fun List<String>.tilt() : List<String> = this.map { it.tilt() }

private  fun List<String>.load() = this.reversed().mapIndexed { index, row -> row.count { it == 'O' } * (index + 1) }.sum()

private fun List<String>.cycle(): List<String> = this.rotateCCW().tilt().rotateCW().tilt().rotateCW().tilt().rotateCW().tilt().rotateCCW().rotateCCW()

private fun findLoopSize(lines: List<String>): Pair<Int, Int> {
    var dish = lines
    val seen = mutableSetOf(lines)
    var ctr = 0
    while (true) {
        ctr++
        dish = dish.cycle()
        if (dish in seen)
            break
        seen.add(dish)
    }
    val first = seen.indexOfFirst { it == dish }
    val loopSize = ctr - first
    return Pair(first,loopSize)
}

private fun part2(lines: List<String>): Int {
    val pair = findLoopSize(lines)

    val targetCycles = 1000000000
    val cycles = pair.first + (targetCycles - pair.first)%pair.second

    var result = lines
    repeat(cycles) {result = result.cycle()}
    return result.load()
}
