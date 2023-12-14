import kotlin.time.measureTime

fun main() {
    val input = readInput("Day14")
    val testInput = readInput("Day14_test")

    val testResult1 = part1(testInput)
    println("Test1 = $testResult1")
    check(testResult1 == 136)
    println("Part1 = ${part1(input)}")

    val testResult2 = part2(testInput)
    println("\nTest2 = $testResult2")
//    check(testResult2 == 64)
    println("Part2 = ${part2(input)}")
}

private fun part1(lines: List<String>) = lines.rotateCCW().tilt().rotateCW().load()


//maybe add a noRockInInterval: Boolean to optimize
//make a better function that just takes the isolated islands
private fun String.tilt() : String {
    val result = this.toMutableList()
    var firstEmpty = 0
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
private fun String.load() = this.reversed().withIndex().filter { it.value == 'O' }.sumOf { it.index + 1 }

private  fun List<String>.load() = this.reversed().mapIndexed { index, row -> row.count { it == 'O' } * (index + 1) }.sum()
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

//optimize columns function to be CCW
private fun List<String>.rotateCCW(): List<String> {

    return this.columns().reversed()
}

private fun List<String>.rotateCW(): List<String> {
    return this.reversed().columns()
}

private fun List<String>.cycle(): List<String> = this.rotateCCW().tilt().rotateCW().tilt().rotateCW().tilt().rotateCW().tilt().rotateCCW().rotateCCW()


//5 seconds for 100000 iterations
//50 000s/833min for 1bil iterations
//find out loop cycle size that has same results

private fun part2(lines: List<String>) {
    var result = lines
    val time = measureTime {
        repeat(1000)
        {result = result.cycle()}
    }
    time.println()
    result.load().println()

}
