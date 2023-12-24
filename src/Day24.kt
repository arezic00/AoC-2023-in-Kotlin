import kotlin.math.abs
import kotlin.math.min

fun main() {
    val input = readInput("Day24")
    val testInput = readInput("Day24_test")

    val testResult1 = part1(testInput, 7, 27)
    println("Test1 = $testResult1")
    check(testResult1 == 2)
    println("Part1 = ${part1(input, 200000000000000, 400000000000000)}")

//    val testResult2 = part2(testInput)
//    println("\nTest2 = $testResult2")
//    check(testResult2 == 1)
//    println("Part2 = ${part2(input)}")
}

fun part1(lines: List<String>, min: Long, max: Long): Int {
    val hailstones = lines.map { row ->
        val positions = row.substringBefore(" @").split(", ").map { it.toLong() }
        val velocities = row.substringAfter("@ ").split(", ").map { it.toLong() }
        Hailstone(positions[0], positions[1], positions[2], velocities[0], velocities[1], velocities[2])
    }

    return hailstones.sumOf { h1 -> hailstones.count { h2 -> h2.intersectInFutureInBoundary(h1, min, max) } } / 2
}

private fun Hailstone.intersectInFutureInBoundary(other: Hailstone, min: Long, max: Long): Boolean {

    val end1x = x + vx
    val end1y = y + vy
    val end2x = other.x + other.vx
    val end2y = other.y + other.vy
    //y = m*x + b
    //b = y - mx
    val m1 = (end1y - y).toDouble() / (end1x - x)
    val b1 = y - m1 * x

    val m2 = (end2y - other.y).toDouble() / (end2x - other.x)
    val b2 = other.y - m2 * other.x

    if (m1 == m2)
        return false

    val crossX = (b2 - b1) / (m1 - m2)
    if (crossX < min || crossX > max || !isInFuture(crossX, x, vx) || !isInFuture(crossX, other.x,  other.vx))
        return false

    val crossY = m1 * crossX + b1
    return crossY >= min && crossY <= max && isInFuture(crossY, y, vy) && isInFuture(crossY, other.y, other.vy)

    //y1 = y2
    //y1 = m1*x + b1
    //y2 = m2*x + b2

    //m1*x + b1 = m2*x + b2
    //x(m1 - m2) = b2 - b1
    //x = (b2 - b1) / (m1 -m2)
}

private fun isInFuture(x: Double, startX: Long, vx: Long) = if (vx < 0) x <= startX else x >= startX

private fun Hailstone.toBoundary(min: Long, max: Long): Hailstone {

    val tx = if (vx < 0) abs((x - min).toDouble()/vx) else abs((max - x).toDouble()/vx)
    val ty = if (vy < 0) abs((y - min).toDouble()/vy) else abs((max - y).toDouble()/vy)
    val time = (min(tx, ty) + 1).toLong()

    return Hailstone(x + time*vx, y + time*vy, z, vx, vy, vz)
}

private data class Hailstone(val x: Long, val y: Long, val z: Long, val vx: Long, val vy: Long, val vz: Long)

private fun part2(lines: List<String>) =
    0