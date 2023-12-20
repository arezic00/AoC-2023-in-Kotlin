import java.util.PriorityQueue

fun main() {
    val input = readInput("Day17")
    val testInput = readInput("Day17_test")
    val testInput2 = readInput("Day17_test2")
    val d17t = Day17(testInput)
    val d17 = Day17(input)

    val testResult1 = d17t.part1()
    println("Test1 = $testResult1")
    check(testResult1 == 102)
    println("Part1 = ${d17.part1()}")

    val testResult2 = d17t.part2()
    println("\nTest2 = $testResult2")
    check(testResult2 == 94)
    check(Day17(testInput2).part2() == 71)
    println("Part2 = ${d17.part2()}")
}

private class Day17(lines: List<String>) {
    private val hl: List<List<Int>>
    private val ROWS: Int
    private val COLS: Int

    init {
        hl = lines.map { row -> row.map { it.digitToInt() } }
        ROWS = hl.size
        COLS = hl.first().size
    }

    fun part1(): Int {
        return dij(0,0)
    }

    private fun dij(row: Int, col: Int): Int {
        val queue = PriorityQueue<Pair<State,Int>> { state1, state2 ->
            state1.second - state2.second
        }
        val seen = mutableSetOf<State>()
        val costSoFar = mutableMapOf<State, Int>()

        val start = State(row,col, Direction.EAST, 0)
        queue.add(start to 0)
        seen.add(start)
        costSoFar[start] = 0

        while (queue.isNotEmpty()) {
            val current = queue.poll()
            val cHl = current.second
            val cState = current.first

            for (adj in cState.neighbours()) {
                val newCost = cHl + hl[adj.row][adj.col]
                if (adj !in seen) {
                    seen.add(adj)
                    costSoFar[adj] = newCost
                    queue.add(adj to newCost)
                }
                else if (newCost < costSoFar[adj]!!) {
                    costSoFar[adj] = newCost
                    queue.add(adj to newCost)
                }
            }
        }

        return buildList {
            for (i in 1 .. 3) {
                val lastE = State(ROWS - 1, COLS - 1, Direction.EAST, i)
                val costE = costSoFar[lastE]
                costE?.let { add(it) }

                val lastS = State(ROWS - 1, COLS - 1, Direction.SOUTH, i)
                val costS = costSoFar[lastS]
                costS?.let { add(it) }

            }
        }.min()
    }

    private fun State.neighbours(): List<State> {
        return buildList<State> {
            for (direction in Direction.entries) {
                if (direction == this@neighbours.direction.opposite())
                    continue
                if (direction == this@neighbours.direction && this@neighbours.dirCtr == 3)
                    continue

                val adj = toRowCol(direction, this@neighbours.row, this@neighbours.col)
                if (adj.first !in 0 until ROWS || adj.second !in 0 until COLS)
                    continue

                add(State(adj.first, adj.second, direction, if (this@neighbours.direction == direction) this@neighbours.dirCtr + 1 else 1))
            }
        }
    }

    private fun State.neighbours2(): List<State> {
        return buildList<State> {
            for (direction in Direction.entries) {
                if (direction == this@neighbours2.direction.opposite())
                    continue
                for (i in 4..10) {
                    val dirCtr = if (direction == this@neighbours2.direction) this@neighbours2.dirCtr + i else i
                    if (dirCtr > 10)
                        continue
                    val adj = toRowCol(direction, this@neighbours2.row, this@neighbours2.col, i)
                    if (adj.first !in 0 until ROWS || adj.second !in 0 until COLS)
                        continue
                    add(State(adj.first, adj.second, direction, dirCtr))

                }
            }
        }
    }

    private fun dij2(row: Int, col: Int): Int {
        val queue = PriorityQueue<Pair<State,Int>> { state1, state2 ->
            state1.second - state2.second
        }
        val seen = mutableSetOf<State>()
        val costSoFar = mutableMapOf<State, Int>()

        val start = State(row,col, Direction.EAST, 0)
        queue.add(start to 0)
        seen.add(start)
        costSoFar[start] = 0

        while (queue.isNotEmpty()) {
            val current = queue.poll()
            val cHl = current.second
            val cState = current.first

            for (adj in cState.neighbours2()) {
                val newCost = cHl + heatLossFromTo(cState, adj)
                if (adj !in seen) {
                    seen.add(adj)
                    costSoFar[adj] = newCost
                    queue.add(adj to newCost)
                }
                else if (newCost < costSoFar[adj]!!) {
                    costSoFar[adj] = newCost
                    queue.add(adj to newCost)
                }
            }
        }

        return buildList {
            for (i in 4 .. 10) {
                val lastE = State(ROWS - 1, COLS - 1, Direction.EAST, i)
                val costE = costSoFar[lastE]
                costE?.let { add(it) }

                val lastS = State(ROWS - 1, COLS - 1, Direction.SOUTH, i)
                val costS = costSoFar[lastS]
                costS?.let { add(it) }

            }
        }.min()
    }

    private fun heatLossFromTo(current: State, adjacent: State): Int {
        val startR: Int
        val endR: Int
        if (current.row < adjacent.row) {
            startR = current.row + 1
            endR = adjacent.row
        }
        else if (current.row > adjacent.row) {
            startR = adjacent.row
            endR = current.row - 1
        }
        else {
            startR = adjacent.row
            endR = current.row
        }

        val startC: Int
        val endC: Int
        if (current.col < adjacent.col) {
            startC = current.col + 1
            endC = adjacent.col
        }
        else if (current.col > adjacent.col) {
            startC = adjacent.col
            endC = current.col - 1
        }
        else {
            startC = adjacent.col
            endC = current.col
        }

        var result = 0
        for (row in startR..endR)
            for (col in startC..endC)
                result += hl[row][col]
        return result
    }

    fun part2(): Int {
        return dij2(0,0)
    }
    private data class State(val row: Int, val col: Int, val direction: Direction, val dirCtr: Int = 1)
}