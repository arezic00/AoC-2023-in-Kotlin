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
    val bricks = lines.map { Brick(it) }.sortedBy { it.z1 }
    val settledBricks = buildList<Brick> {
        bricks.forEach { add(it.fallOn(this)) }
    }

    return settledBricks.count { !it.isSoloSupport(settledBricks) }
}




private fun Brick.fallOn(bricks: List<Brick>) : Brick {
    if (z1 == 1)
        return this

    val plane = Rectangle(this.x1, this.y1, this.x2 - this.x1 + 1, this.y2 - this.y1 + 1)
    for (brick in bricks.sortedByDescending { it.z2 }) {
        //maybe remove this after, check if it still works
        if (brick.z2 >= z1)
            continue
        val other = Rectangle(brick.x1, brick.y1, brick.x2 - brick.x1 + 1, brick.y2 - brick.y1 + 1)
        if (plane.intersects(other))
            return fallAt(brick.z2 + 1)
    }
    return fallAt(1)
}

private fun Brick.fallAt(z: Int) = Brick(x1, y1, z, x2, y2, z2 - (z1 - z))

private fun Brick.isSoloSupport(bricks: List<Brick>) : Boolean {
    return this.supports(bricks).any { it.hasOneSupport(bricks) }
}

private fun Brick.hasOneSupport(bricks: List<Brick>) : Boolean {
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

private fun Brick.supports(bricks: List<Brick>) : List<Brick> {
    return bricks.filter { it.z1 == z2 + 1 && it.intersects(this)}
}

private fun Brick.getSupports(bricks: List<Brick>) : List<Brick> {
    return bricks.filter { it.z2 == z1 - 1 && it.intersects(this)}
}

private fun Brick.supports(other: Brick) : Boolean {
    return this.z2 == other.z1 - 1 && this.intersects(other)
}

private fun Brick.intersects(other: Brick): Boolean {
    val plane = Rectangle(this.x1, this.y1, this.x2 - this.x1 + 1, this.y2 - this.y1 + 1)
    val other = Rectangle(other.x1, other.y1, other.x2 - other.x1 + 1, other.y2 - other.y1 + 1)
    return plane.intersects(other)
}

private fun List<Brick>.disintegrate(toBeDisintegrated: List<Brick>): Int {
    if (toBeDisintegrated.isEmpty())
        return 0

    val disintegrationList = buildList {
        for (brick in toBeDisintegrated.support(this@disintegrate)) {
            if (toBeDisintegrated.areTheOnlySupportOf(brick, this@disintegrate))
                add(brick)
        }
    }

    return toBeDisintegrated.count() + this.subtract(toBeDisintegrated).toList().disintegrate(disintegrationList)

}

private fun List<Brick>.support(bricks: List<Brick>) : List<Brick> = map { it.supports(bricks) }.flatten().distinct()

private fun List<Brick>.areTheOnlySupportOf(brick: Brick, bricks: List<Brick>): Boolean {
    return brick.getSupports(bricks).all { it in this }
}

private fun Brick(line: String) : Brick {
    val start = line.substringBefore('~').split(',').map { it.toInt() }
    val end = line.substringAfter('~').split(',').map { it.toInt() }
    return Brick(start[0],start[1],start[2],end[0],end[1],end[2])
}

data class Brick(val x1: Int, val y1: Int, val z1: Int, val x2: Int, val y2: Int, val z2: Int)

private fun part2(lines: List<String>): Int {
    val bricks = lines.map { Brick(it) }.sortedBy { it.z1 }
    val settledBricks = buildList<Brick> {
        bricks.forEach { add(it.fallOn(this)) }
    }

    return settledBricks.sumOf { settledBricks.disintegrate(listOf(it)) - 1 }
}
