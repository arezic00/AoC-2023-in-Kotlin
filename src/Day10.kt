fun main() {
    fun part1(input: List<String>) = Day10.part1(input)

    fun part2(input: List<String>)= Day10.part2(input)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    val testResult1 = part1(testInput)
    println("Test1 result = $testResult1")
    check(testResult1 == 4)


//    val testResult2 = part2(testInput)
//    println("Test2 result = $testResult2")
//    check(testResult2 == 1)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}

object Day10 {

    var COLUMNS = 102
    var ROWS = 140
    fun part1(lines: List<String>): Int {
        val grid = lines.mapIndexed { row, line ->
            lineToElementRow(line,row)
        }
        ROWS = grid.size
        COLUMNS = grid.first().size

        val startPipe = findStart(grid)

        val loop = buildLoop(grid,startPipe)
        return loop.size / 2



    }



    //S has exactly 2 pipes connecting to it
    //there can be loops that arent main
    //need to find tile in the loop that is farthest from the S - longest number of steps from S to it
    //(along the loop)

    private fun buildLoop(grid: List<List<Element>>,startPipe: Pipe) : List<Pipe> {
        val loop = mutableListOf(startPipe)
        loop.add(findConnections(grid, startPipe).first())
        while(true) {
            val previous = loop[loop.lastIndex - 1]
            val current = loop.last()
            val next = findConnections(grid, current).first { it != previous }
            if (next == startPipe)
                break
            loop.add(next)
        }
        return loop
    }

    private fun findConnections(grid: List<List<Element>>, pipe: Pipe) : List<Pipe> {
        val result = mutableListOf<Pipe>()
        if (pipe.row != 0)
        {
            val element = grid[pipe.row - 1][pipe.col]
            if (element is Pipe && pipe.connectsTo(element))
                result.add(element)
        }
        if (pipe.row != ROWS - 1)
        {
            val element = grid[pipe.row + 1][pipe.col]
            if (element is Pipe && pipe.connectsTo(element))
                result.add(element)
        }
        if (pipe.col != 0)
        {
            val element = grid[pipe.row][pipe.col - 1]
            if (element is Pipe && pipe.connectsTo(element))
                result.add(element)
        }
        if (pipe.col != COLUMNS - 1)
        {
            val element = grid[pipe.row][pipe.col + 1]
            if (element is Pipe && pipe.connectsTo(element))
                result.add(element)
        }
        return result

    }

    private fun findStart(grid: List<List<Element>>) : Pipe {
        grid.forEach { row ->
            row.forEach {
                if (it is Pipe && it.directions.size == 4)
                    return it
            }
        }
        return Pipe(listOf(),0,0)
    }

    private fun lineToElementRow(line: String, row: Int) : List<Element> {
        return line.withIndex().map { Element(it.value,row, it.index) }
    }

    enum class Direction {
        NORTH, EAST, SOUTH, WEST
    }

    enum class Sign {
        EMPTY,
        START,
        VERTICAL,
        HORIZONTAL,
        NORTHEAST,
        NORTHWEST,
        SOUTHWEST,
        SOUTHEAST
    }

    private fun Char.toSign() : Sign {
        return when (this) {
            'S' ->  Sign.START
            '|' ->  Sign.VERTICAL
            '-' ->  Sign.HORIZONTAL
            'L' ->  Sign.NORTHEAST
            'J' ->  Sign.NORTHWEST
            '7' ->  Sign.SOUTHWEST
            'F' -> Sign.SOUTHEAST
            else -> Sign.EMPTY
        }
    }

    private fun Pipe.connectsTo(other: Pipe) : Boolean {
        if (other.row == this.row) {
            if (other.col == this.col + 1)
                return this.directions.contains(Direction.EAST) && other.directions.contains(Direction.WEST)
            if (this.col == other.col + 1)
                return this.directions.contains(Direction.WEST) && other.directions.contains(Direction.EAST)
        }
        if (other.col == this.col) {
            if (other.row == this.row + 1)
                return this.directions.contains(Direction.SOUTH) && other.directions.contains(Direction.NORTH)
            if (this.row == other.row + 1)
                return this.directions.contains(Direction.NORTH) && other.directions.contains(Direction.SOUTH)
        }
        return false
    }


    private fun Element(char: Char, row: Int, col: Int) : Element {
        return when (char.toSign()) {
            Sign.EMPTY -> Element(row, col)
            Sign.START -> Pipe(listOf(Direction.SOUTH,Direction.EAST, Direction.NORTH, Direction.WEST),row,col)
            Sign.VERTICAL -> Pipe(listOf(Direction.NORTH,Direction.SOUTH),row,col)
            Sign.HORIZONTAL -> Pipe(listOf(Direction.EAST,Direction.WEST),row,col)
            Sign.NORTHEAST -> Pipe(listOf(Direction.NORTH,Direction.EAST),row,col)
            Sign.NORTHWEST -> Pipe(listOf(Direction.NORTH,Direction.WEST),row,col)
            Sign.SOUTHEAST -> Pipe(listOf(Direction.SOUTH,Direction.EAST),row,col)
            Sign.SOUTHWEST -> Pipe(listOf(Direction.SOUTH,Direction.WEST),row,col)
        }
    }

    data class Pipe(val directions: List<Direction>,
                    override val row: Int,
                    override val col: Int,
    ) : Element(row, col) {

    }
    open class Element(open val row: Int, open val col: Int)


    fun part2(lines: List<String>) =
        0
}