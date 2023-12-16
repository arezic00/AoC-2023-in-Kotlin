fun main() {
    val input = readInput("Day16")
    val testInput = readInput("Day16_test")
    val testCave = Cave(testInput)
    val cave = Cave(input)

    val testResult1 = testCave.part1()
    println("Test1 = $testResult1")
    check(testResult1 == 46)
    println("Part1 = ${cave.part1()}")

    val testResult2 = testCave.part2()
    println("\nTest2 = $testResult2")
    check(testResult2 == 51)
    println("Part2 = ${cave.part2()}")
}

private class Cave(lines: List<String>) {
    val grid: List<List<Tile>>
    val ROWS = lines.size
    val COL = lines.first().length

    init {
        grid = lines.map { row -> row.map { Tile(it) } }
    }
    fun part1(): Int {
        spreadLight(Direction.EAST,0,0)
        return energy()
    }

    private fun energy() = grid.sumOf { row -> row.count { it.beams.isNotEmpty() } }

    private fun spreadLight(direction: Direction, row: Int, col: Int) {
        if (row !in 0 until ROWS || col !in 0 until COL || grid[row][col].beams.contains(direction))
            return
        val sign = grid[row][col].sign
        grid[row][col].beams.add(direction)

        if (sign == '|' && direction.ordinal % 2 == 1 || sign == '-' && direction.ordinal % 2 == 0) {
            val dir1 = direction.rotateCW()
            val tile1 = toRowCol(dir1,row,col)
            spreadLight(dir1, tile1.first, tile1.second)

            val dir2 = direction.rotateCCW()
            val tile2 = toRowCol(dir2,row,col)
            spreadLight(dir2, tile2.first, tile2.second)
        }

        else {
            val dir = direction.rotate(sign)
            val tile = toRowCol(dir, row, col)
            spreadLight(dir, tile.first, tile.second)
        }
    }

    private fun Direction.rotate(sign: Char) : Direction {
        return when (sign) {
            '/' -> if (this.ordinal % 2 == 0) this.rotateCW() else this.rotateCCW()
            '\\' -> if (this.ordinal % 2 == 0) this.rotateCCW() else this.rotateCW()
            else -> this
        }
    }

    data class Tile(val sign: Char, val beams: MutableList<Direction> = mutableListOf())

    fun part2(): Int {
        var max = 0
        for (col in 0 until COL)
        {
            clearBeams()
            spreadLight(Direction.SOUTH,0,col)
            val energyFromTop = energy()
            if (energyFromTop > max) max = energyFromTop

            clearBeams()
            spreadLight(Direction.NORTH,ROWS - 1,col)
            val energyFromBottom = energy()
            if (energyFromBottom > max) max = energyFromBottom
        }

        for (row in 0 until ROWS)
        {
            clearBeams()
            spreadLight(Direction.EAST, row, 0)
            val energyFromLeft = energy()
            if (energyFromLeft > max) max = energyFromLeft

            clearBeams()
            spreadLight(Direction.WEST,row,COL - 1)
            val energyFromRight = energy()
            if (energyFromRight > max) max = energyFromRight
        }

        return max
    }

    private fun clearBeams() = grid.forEach { row -> row.forEach { it.beams.clear() } }
}