import java.awt.Point
import java.rmi.UnexpectedException

fun main() {
    val input = readInput("Day18")
    val testInput = readInput("Day18_test")

    val testResult1 = part1(testInput)
    println("Test1 = $testResult1")
    check(testResult1 == 62)
    println("Part1 = ${part1(input)}")

    val testResult2 = part2(testInput)
    println("\nTest2 = $testResult2")
    check(testResult2 == 952408144115L)
    println("Part2 = ${part2(input)}")
}

private fun part1(lines: List<String>): Int {
    val boundaryPoints: Int
    val points = buildList {
        var rowCol = 0 to 0
        var sum = 0
        lines.forEach {
            val trio = it.split(" ")
            val dir = trio[0].first().toDirection()
            val len = trio[1].toInt()
            rowCol = toRowCol(dir, rowCol.first, rowCol.second)
            add(Point(rowCol.second, rowCol.first))
            rowCol = toRowCol(dir, rowCol.first, rowCol.second, len - 1)
            add(Point(rowCol.second, rowCol.first))

            sum += len
        }
        boundaryPoints = sum
    }
    val shoelaceArea = (points.windowed(3).sumOf { it[1].x * (it[2].y - it[0].y) }
        + points.first().x * (points[1].y - points.last().y)
        + points.last().x * (points.first().y - points[points.lastIndex - 1].y)) / 2.0

    val interiorPoints = shoelaceArea - boundaryPoints / 2.0 + 1

    return interiorPoints.toInt() + boundaryPoints
}

private fun part2(lines: List<String>): Long {
    val boundaryPoints: Long
    val points = buildList {
        var rowCol = 0L to 0L
        var sum = 0L
        lines.forEach {
            val duo = it.split(" ").last().trim('(',')','#')
            val dir = Direction.entries[duo.last().digitToInt()].rotateCW()
            val len = duo.dropLast(1).toLong(16)
            rowCol = toRowCol(dir, rowCol.first, rowCol.second)
            add(Point(rowCol.second, rowCol.first))
            rowCol = toRowCol(dir, rowCol.first, rowCol.second, len - 1)
            add(Point(rowCol.second, rowCol.first))

            sum += len
        }
        boundaryPoints = sum
    }
    val shoelaceArea = (points.windowed(3).sumOf { it[1].x * (it[2].y - it[0].y) }
            + points.first().x * (points[1].y - points.last().y)
            + points.last().x * (points.first().y - points[points.lastIndex - 1].y)) / 2.0

    val interiorPoints = shoelaceArea - boundaryPoints / 2.0 + 1

    return interiorPoints.toLong() + boundaryPoints
}

private fun Char.toDirection(): Direction {
    return when (this) {
        'U' -> Direction.NORTH
        'R' -> Direction.EAST
        'D' -> Direction.SOUTH
        'L' -> Direction.WEST
        else -> throw UnexpectedException("Unexpected char: $this")
    }
}

private data class Point(val x: Long, val y: Long)