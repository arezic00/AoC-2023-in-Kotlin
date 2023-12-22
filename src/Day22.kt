import java.awt.Rectangle

fun main() {
    val input = readInput("Day22")
    val testInput = readInput("Day22_test")

    val testResult1 = part1(testInput)
    println("Test1 = $testResult1")
    check(testResult1 == 5)
    println("Part1 = ${part1(input)}")

    val testResult2 = part2(testInput)
    println("\nTest2 = $testResult2")
    check(testResult2 == 7)
    println("Part2 = ${part2(input)}")
}

private fun part1(lines: List<String>): Int {
    val settledBricks = parse(lines)
    return settledBricks.count { !it.isSoloSupport(settledBricks) }
}

private fun part2(lines: List<String>): Int {
    val settledBricks = parse(lines)
    return settledBricks.sumOf { settledBricks.disintegrate(setOf(it)) - 1 }
}

private fun parse(lines: List<String>): Set<Brick> {
    val bricks = lines.map { Brick(it) }.toSet().sortedBy { it.z1 }
    return buildSet { bricks.forEach { add(it.fallOn(this)) } }
}

private fun Brick.fallOn(bricks: Set<Brick>) : Brick {
    if (z1 == 1)
        return this

    for (brick in bricks.sortedByDescending { it.z2 }) {
        if (this.intersects(brick))
            return fallAt(brick.z2 + 1)
    }
    return fallAt(1)
}

private fun Brick.fallAt(z: Int) = Brick(x1, y1, z, x2, y2, z2 - (z1 - z))

private fun Brick.isSoloSupport(bricks: Set<Brick>) : Boolean {
    return this.supports(bricks).any { it.hasOneSupport(bricks) }
}

private fun Brick.hasOneSupport(bricks: Set<Brick>) : Boolean {
    var hasSupport = false
    for (brick in bricks) {
        if (brick.supports(this)) {
            if (hasSupport)
                return false
            hasSupport = true
        }
    }
    return hasSupport
}

private fun Brick.supports(bricks: Set<Brick>) : Set<Brick> {
    return bricks.filter { it.z1 == z2 + 1 && it.intersects(this)}.toSet()
}

private fun Brick.getSupports(bricks: Set<Brick>) : Set<Brick> {
    return bricks.filter { it.z2 == z1 - 1 && it.intersects(this)}.toSet()
}

private fun Brick.supports(other: Brick) : Boolean {
    return this.z2 == other.z1 - 1 && this.intersects(other)
}

private fun Brick.intersects(other: Brick): Boolean {
    val plane1 = Rectangle(this.x1, this.y1, this.x2 - this.x1 + 1, this.y2 - this.y1 + 1)
    val plane2 = Rectangle(other.x1, other.y1, other.x2 - other.x1 + 1, other.y2 - other.y1 + 1)
    return plane1.intersects(plane2)
}

private fun Set<Brick>.disintegrate(toBeDisintegrated: Set<Brick>): Int {
    if (toBeDisintegrated.isEmpty())
        return 0

    val disintegrationList = buildSet {
        for (brick in toBeDisintegrated.support(this@disintegrate)) {
            if (toBeDisintegrated.areTheOnlySupportOf(brick, this@disintegrate))
                add(brick)
        }
    }

    return toBeDisintegrated.count() + this.subtract(toBeDisintegrated.toSet()).disintegrate(disintegrationList)
}

private fun Set<Brick>.support(bricks: Set<Brick>) : List<Brick> = map { it.supports(bricks) }.flatten().distinct()

private fun Set<Brick>.areTheOnlySupportOf(brick: Brick, bricks: Set<Brick>): Boolean {
    return brick.getSupports(bricks).all { it in this }
}

private fun Brick(line: String) : Brick {
    val start = line.substringBefore('~').split(',').map { it.toInt() }
    val end = line.substringAfter('~').split(',').map { it.toInt() }
    return Brick(start[0],start[1],start[2],end[0],end[1],end[2])
}

data class Brick(val x1: Int, val y1: Int, val z1: Int, val x2: Int, val y2: Int, val z2: Int)