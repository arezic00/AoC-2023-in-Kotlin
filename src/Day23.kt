import java.util.*

fun main() {
    val input = readInput("Day23")
    val testInput = readInput("Day23_test")
    val d23t = Day23(testInput)
    val d23 = Day23(input)

    val testResult1 = d23t.part1(testInput)
    println("Test1 = $testResult1")
    check(testResult1 == 94)
    println("Part1 = ${d23.part1(input)}")
//
//    val testResult2 = d23t.part2(testInput)
//    println("\nTest2 = $testResult2")
//    check(testResult2 == 154)
//    println("Part2 = ${part2(input)}")
}

private class Day23(lines: List<String>) {
    private val trail: List<String> = lines

    private val ROWS = lines.size
    private val COLS = lines.first().length
    fun part1(lines: List<String>) =
        bfs()

    //recursive bfs
//    private fun bfs(q: Queue<Pair<Int,Int>>, sn: Set<Pair<Int,Int>>, st: Pair<Int,Int>) {
//        val queue: Queue<Pair<Int,Int>> = LinkedList()
//        val seen = mutableSetOf<Pair<Int,Int>>()
//
//        val start = 0 to trail.first().indexOf('.')
//
//
//    }

    //assume there is no loops
    private fun bfs(): Int {
        val queue: Queue<State> = LinkedList()
        val stepsSoFar = mutableMapOf<State,Int>()

        val start = State(0, trail.first().indexOf('.'), Direction.SOUTH)
        queue.add(start)
        stepsSoFar[start] = 0

        while (queue.isNotEmpty()) {
            val current = queue.poll()

            for (adj in current.neighbours()) {
                val newSteps = stepsSoFar[current]!! + 1
                if (adj !in stepsSoFar || newSteps > stepsSoFar[adj]!!) {
                    stepsSoFar[adj] = newSteps
                    queue.add(adj)
                }
            }
        }

        return stepsSoFar.filterKeys { it.row == ROWS - 1 }.maxOf { it.value }

    }

    private fun State.neighbours(): List<State> {
        trail[row][col].toDirection()?.let {
            if (dir == it.opposite()) return listOf()
            val rc = toRowCol(it, row, col)
            return listOf(State(rc.first, rc.second, it))
        }

        return buildList {
            for (direction in Direction.entries) {
                if (direction == dir.opposite())
                    continue
                val rc = toRowCol(direction, row, col)
                if (rc.first !in 0 until ROWS || rc.second !in 0 until COLS || trail[rc.first][rc.second] == '#')
                    continue
                add(State(rc.first, rc.second, direction))
            }
        }
    }

    private fun Char.toDirection(): Direction? {
        return when (this) {
            '^' -> Direction.NORTH
            '>' -> Direction.EAST
            'v' -> Direction.SOUTH
            '<' -> Direction.WEST
            else -> null
        }
    }

    private data class State(val row: Int, val col: Int, val dir: Direction)


    fun part2(lines: List<String>) =
        0
}