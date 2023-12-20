import java.util.PriorityQueue

fun main() {
    val input = readInput("Day17")
    val testInput = readInput("Day17_test")
    val d17t = Day17(testInput)
    val d17 = Day17(input)

    val testResult1 = d17t.part1()
    println("Test1 = $testResult1")
    check(testResult1 == 102)
    println("Part1 = ${d17.part1()}")

//    val testResult2 = part2(testInput)
//    println("\nTest2 = $testResult2")
//    check(testResult2 == 1)
//    println("Part2 = ${part2(input)}")
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
    private data class State(val row: Int, val col: Int, val direction: Direction, val dirCtr: Int = 1)
}