import java.util.PriorityQueue

fun main() {
    val d17t = Day17(readInput("Day17_test"))
    val d17 = Day17(readInput("Day17"))

    val testResult1 = d17t.part1()
    println("Test1 = $testResult1")
    check(testResult1 == 102)
    println("Part1 = ${d17.part1()}")

    val testResult2 = d17t.part2()
    println("\nTest2 = $testResult2")
    check(testResult2 == 94)
    check(Day17(readInput("Day17_test2")).part2() == 71)
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

    fun part1() = dij(1,3)

    private fun State.neighbours(startRadius: Int, endRadius: Int): List<State> {
        return buildList {
            for (direction in Direction.entries) {
                if (direction == this@neighbours.direction.opposite())
                    continue
                for (i in startRadius..endRadius) {
                    val dirCtr = if (direction == this@neighbours.direction) this@neighbours.dirCtr + i else i
                    if (dirCtr > endRadius)
                        continue
                    val adj = toRowCol(direction, this@neighbours.row, this@neighbours.col, i)
                    if (adj.first !in 0 until ROWS || adj.second !in 0 until COLS)
                        continue
                    add(State(adj.first, adj.second, direction, dirCtr))

                }
            }
        }
    }

    private fun dij(minDist: Int, maxDist: Int): Int {
        val queue = PriorityQueue<Pair<State,Int>> { state1, state2 ->
            state1.second - state2.second
        }
        val costSoFar = mutableMapOf<State, Int>()

        val start = State(0,0, Direction.EAST, 0)
        queue.add(start to 0)
        costSoFar[start] = 0

        while (queue.isNotEmpty()) {
            val current = queue.poll()
            val cHl = current.second
            val cState = current.first

            if (cState.row == ROWS - 1 && cState.col == COLS - 1)
                break

            for (adj in cState.neighbours(minDist, maxDist)) {
                val newCost = cHl + heatLossFromTo(cState, adj)
                if (adj !in costSoFar || newCost < costSoFar[adj]!!) {
                    costSoFar[adj] = newCost
                    queue.add(adj to newCost)
                }
            }
        }

        return costSoFar.filterKeys { it.row == ROWS - 1 && it.col == COLS - 1  }.minOf { it.value }
    }

    private fun heatLossFromTo(current: State, adjacent: State): Int {
        val startR: Int
        val endR: Int
        val startC: Int
        val endC: Int
        if (current.row == adjacent.row) {
            startR = current.row
            endR = current.row
            if (current.col < adjacent.col) {
                startC = current.col + 1
                endC = adjacent.col
            }
            else {
                startC = adjacent.col
                endC = current.col - 1
            }
        }
        else {
            startC = current.col
            endC = current.col
            if (current.row < adjacent.row) {
                startR = current.row + 1
                endR = adjacent.row
            }
            else {
                startR = adjacent.row
                endR = current.row - 1
            }
        }

        var result = 0
        for (row in startR..endR)
            for (col in startC..endC)
                result += hl[row][col]
        return result
    }

    fun part2() = dij(4,10)
    private data class State(val row: Int, val col: Int, val direction: Direction, val dirCtr: Int = 1)
}