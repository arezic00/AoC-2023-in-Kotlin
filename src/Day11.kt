import java.awt.Point
import kotlin.math.abs
fun main() {
    val input = readInput("Day11")
    val testInput = readInput("Day11_test")

    val testResult1 = part1(testInput)
    println("Test1 = $testResult1")
    check(testResult1 == 374L)
    println("Part1 = ${part1(input)}")

    val testResult2 = part2(testInput)
    println("\nTest2 = $testResult2")
    check(testResult2 == 82000210L)
    println("Part2 = ${part2(input)}")
}

private fun parse(lines: List<String>) = lines.mapIndexed { row, string ->
    string.withIndex().filter {
        it.value == '#'
    }.map { Point(it.index,row) }
}.flatten()

private fun part1(lines: List<String>) = parse(lines).expand(2).sumDistances()

private fun List<Point>.sumDistances(): Long {
        var distanceSum = 0L
        for (index in this.indices) {
            for (comparedIndex in index + 1 until this.size) {
                val distance = distance(this[index],this[comparedIndex])
                distanceSum += distance
            }
        }
        return distanceSum
    }

private fun distance(point1: Point, point2: Point) : Int {
        return abs(point1.x - point2.x) + abs(point1.y - point2.y)
    }

private fun part2(lines: List<String>)  = parse(lines).expand(1000000).sumDistances()

private fun List<Point>.expand(factor: Int) : List<Point> {
        val xSorted = this.sortedBy { it.x }
        for (index in 1 until xSorted.size) {
            val xDiff = xSorted[index].x - xSorted[index - 1].x
            if (xDiff > 1)
            {
                for (subIndex in index until xSorted.size) {
                    xSorted[subIndex].x += (factor - 1)*(xDiff - 1)
                }
            }
        }

        val ySorted = xSorted.sortedBy { it.y }
        for (index in 1 until ySorted.size) {
            val yDiff = ySorted[index].y - ySorted[index - 1].y
            if (yDiff > 1)
            {
                for (subIndex in index until ySorted.size) {
                    ySorted[subIndex].y += (factor - 1)*(yDiff - 1)
                }
            }
        }
        return ySorted
        //x1 =3 x2 = 6 asset 24 -> xDiff = 3 18 + 6 = 24
    }