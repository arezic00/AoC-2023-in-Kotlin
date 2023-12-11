fun main() {
    fun part1(input: List<String>) = Day10.part1(input)

    fun part2(input: List<String>)= Day10.part2(input)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    val testResult1 = part1(testInput)
    println("Test1 result = $testResult1")
    check(testResult1 == 4)


    val testResult2 = part2(testInput)
    println("Test2 result = $testResult2")
    check(testResult2 == 1)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}

object Day10 {

    var COLUMNS = 102
    var ROWS = 140
    var markedGrid: List<List<MarkedElement>> = emptyList()
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
        if (this.isDirectlyWestOf(other))
            return this.directions.contains(Direction.EAST) && other.directions.contains(Direction.WEST)

        if (this.isDirectlyEastOf(other))
            return this.directions.contains(Direction.WEST) && other.directions.contains(Direction.EAST)

        if (this.isDirectlyNorthOf(other))
            return this.directions.contains(Direction.SOUTH) && other.directions.contains(Direction.NORTH)

        if (this.isDirectlySouthOf(other))
            return this.directions.contains(Direction.NORTH) && other.directions.contains(Direction.SOUTH)

        return false
    }

    private fun Pipe.isDirectlyNorthOf(other: Pipe) : Boolean = this.col == other.col && this.row == other.row - 1
    private fun Pipe.isDirectlySouthOf(other: Pipe) : Boolean = this.col == other.col && this.row == other.row + 1
    private fun Pipe.isDirectlyEastOf(other: Pipe) : Boolean = this.row == other.row && this.col == other.col + 1
    private fun Pipe.isDirectlyWestOf(other: Pipe) : Boolean = this.row == other.row && this.col == other.col - 1


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

    data class MarkedElement(var mark: Mark)

    enum class Mark {
        ENCLOSED,
        UNMARKED,
        LOOP_PIPE
    }


    //this function keeps track of the inside of the loop with each new loop pipe (directionInsideLoop)
    //spreads the ENCLOSED mark recursively starting from each element that touches the inside of the loop
    private fun markAllEnclosedTiles(pipes: List<Pipe>) {
        //it's a 50/50 guess and I got it right for both the test and the real input :D
        var directionInsideLoop = Direction.EAST

        for (i in 1 until pipes.size) {
            if (pipes[i].isDirectlyNorthOf(pipes[i-1])) {
                when (directionInsideLoop) {
                    Direction.NORTH -> println("ERROR")
                    Direction.EAST -> {
                        if (pipes[i].directions.contains(Direction.NORTH)) {
                            spreadFromLoopPipe(pipes[i],directionInsideLoop)
                        }
                        else if (pipes[i].directions.contains(Direction.EAST)) {
                            directionInsideLoop = Direction.SOUTH
                        }
                        else if (pipes[i].directions.contains(Direction.WEST))
                        {
                            spreadFromLoopPipe(pipes[i],directionInsideLoop)
                            directionInsideLoop = Direction.NORTH
                            spreadFromLoopPipe(pipes[i],directionInsideLoop)
                        }
                    }
                    Direction.SOUTH -> {
                        kotlin.io.println("ERROR")
                    }
                    Direction.WEST -> {
                        if (pipes[i].directions.contains(Direction.NORTH))
                            spreadFromLoopPipe(pipes[i],directionInsideLoop)
                        else if (pipes[i].directions.contains(Direction.EAST)) {
                            spreadFromLoopPipe(pipes[i],directionInsideLoop)
                            directionInsideLoop = Direction.NORTH
                            spreadFromLoopPipe(pipes[i],directionInsideLoop)
                        }
                        else if (pipes[i].directions.contains(Direction.WEST))
                        {
                            directionInsideLoop = Direction.SOUTH
                        }
                    }
                }
            }
            else if (pipes[i].isDirectlyEastOf(pipes[i-1])) {
                when (directionInsideLoop) {
                    Direction.NORTH -> {
                        if (pipes[i].directions.contains(Direction.NORTH))
                            directionInsideLoop = Direction.WEST
                        else if (pipes[i].directions.contains(Direction.EAST))
                            spreadFromLoopPipe(pipes[i], directionInsideLoop)
                        else if (pipes[i].directions.contains(Direction.SOUTH)) {
                            spreadFromLoopPipe(pipes[i],directionInsideLoop)
                            directionInsideLoop = Direction.EAST
                            spreadFromLoopPipe(pipes[i],directionInsideLoop)
                        }
                    }
                    Direction.EAST -> println("ERROR")
                    Direction.SOUTH -> {
                        if (pipes[i].directions.contains(Direction.NORTH)) {
                            spreadFromLoopPipe(pipes[i], directionInsideLoop)
                            directionInsideLoop = Direction.EAST
                            spreadFromLoopPipe(pipes[i],directionInsideLoop)
                        }
                        else if (pipes[i].directions.contains(Direction.EAST))
                            spreadFromLoopPipe(pipes[i], directionInsideLoop)
                        else if (pipes[i].directions.contains(Direction.SOUTH)) {
                            directionInsideLoop = Direction.WEST
                        }
                    }
                    Direction.WEST -> kotlin.io.println("ERROR")
                }
            }
            else if (pipes[i].isDirectlySouthOf(pipes[i-1])) {
                when (directionInsideLoop) {
                    Direction.NORTH -> println("ERROR")
                    Direction.EAST -> {
                        if (pipes[i].directions.contains(Direction.EAST))
                            directionInsideLoop = Direction.NORTH
                        else if (pipes[i].directions.contains(Direction.SOUTH))
                            spreadFromLoopPipe(pipes[i],directionInsideLoop)
                        else if (pipes[i].directions.contains(Direction.WEST))
                        {
                            spreadFromLoopPipe(pipes[i],directionInsideLoop)
                            directionInsideLoop = Direction.SOUTH
                            spreadFromLoopPipe(pipes[i],directionInsideLoop)
                        }
                    }
                    Direction.SOUTH -> println("ERROR")
                    Direction.WEST -> {
                        if (pipes[i].directions.contains(Direction.EAST)) {
                            spreadFromLoopPipe(pipes[i],directionInsideLoop)
                            directionInsideLoop = Direction.SOUTH
                            spreadFromLoopPipe(pipes[i],directionInsideLoop)
                        }
                        else if (pipes[i].directions.contains(Direction.SOUTH))
                            spreadFromLoopPipe(pipes[i],directionInsideLoop)
                        else if (pipes[i].directions.contains(Direction.WEST))
                        {
                            directionInsideLoop = Direction.NORTH
                        }
                    }
                }
            }
            else if (pipes[i].isDirectlyWestOf(pipes[i-1])) {
                when (directionInsideLoop) {
                    Direction.NORTH -> {
                        if (pipes[i].directions.contains(Direction.SOUTH)) {
                            spreadFromLoopPipe(pipes[i],directionInsideLoop)
                            directionInsideLoop = Direction.WEST
                            spreadFromLoopPipe(pipes[i],directionInsideLoop)
                        }
                        else if (pipes[i].directions.contains(Direction.WEST))
                            spreadFromLoopPipe(pipes[i],directionInsideLoop)
                        else if (pipes[i].directions.contains(Direction.NORTH))
                        {
                            directionInsideLoop = Direction.EAST
                        }
                    }
                    Direction.EAST -> println("ERROR")
                    Direction.SOUTH -> {
                        if (pipes[i].directions.contains(Direction.SOUTH)) {
                            directionInsideLoop = Direction.EAST
                        }
                        else if (pipes[i].directions.contains(Direction.WEST))
                            spreadFromLoopPipe(pipes[i],directionInsideLoop)
                        else if (pipes[i].directions.contains(Direction.NORTH))
                        {
                            spreadFromLoopPipe(pipes[i],directionInsideLoop)
                            directionInsideLoop = Direction.WEST
                            spreadFromLoopPipe(pipes[i],directionInsideLoop)
                        }
                    }
                    Direction.WEST -> println("ERROR")
                }
            }



        }
    }

    private fun spreadFromLoopPipe(pipe: Pipe, direction: Direction) {
        when (direction) {
            Direction.NORTH -> spread(pipe.row -1 , pipe.col)
            Direction.EAST -> spread(pipe.row, pipe.col + 1)
            Direction.SOUTH -> spread(pipe.row + 1, pipe.col)
            Direction.WEST -> spread(pipe.row, pipe.col - 1)
        }
    }

    private fun outOfBounds(row: Int, col: Int) : Boolean = !(row in 0 until ROWS && col in 0 until COLUMNS)

    private fun spread(row: Int, col: Int) {
        if (outOfBounds(row,col) || markedGrid[row][col].mark != Mark.UNMARKED)
            return
        markedGrid[row][col].mark = Mark.ENCLOSED
        spread(row - 1, col)
        spread(row, col + 1)
        spread(row + 1, col)
        spread(row, col - 1)
        return
    }

    fun part2(lines: List<String>) :Int {
        val grid = lines.mapIndexed { row, line ->
            lineToElementRow(line, row)
        }
        ROWS = grid.size
        COLUMNS = grid.first().size

        val startPipe = findStart(grid)
        val loop = buildLoop(grid, startPipe)

        markedGrid = grid.map { row ->
            row.map { element ->
                if (loop.contains(element)) MarkedElement(Mark.LOOP_PIPE)
                else MarkedElement(Mark.UNMARKED)
            }
        }

//        markedGrid.map { it.map { if(it.mark == Mark.UNMARKED) '0' else '*' }   }.forEach { it.println() }

        markedGrid.forEach { it.map { if (it.mark == Mark.LOOP_PIPE) '*' else ' '}.println()  }
        markAllEnclosedTiles(loop)
        markedGrid.forEach { it.map { if (it.mark == Mark.ENCLOSED) '*' else ' '}.println()  }

        return markedGrid.sumOf { it.filter { it.mark == Mark.ENCLOSED }.count() }
    }


    }


