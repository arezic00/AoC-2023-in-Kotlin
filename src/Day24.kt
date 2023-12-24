fun main() {
    val input = readInput("Day24")
    val testInput = readInput("Day24_test")

    val testResult1 = part1(testInput, 7, 27)
    println("Test1 = $testResult1")
    check(testResult1 == 2)
    println("Part1 = ${part1(input, 200000000000000, 400000000000000)}")

    val testResult2 = part2(testInput, 5L)
    println("\nTest2 = $testResult2")
    check(testResult2 == 47L)
    println("Part2 = ${part2(input, 100L)}")
}

fun part1(lines: List<String>, min: Long, max: Long): Int {
    val hailstones = parse(lines)

    return hailstones.sumOf { h1 -> hailstones.count { h2 -> h2.intersectInFutureInBoundary(h1, min, max) } } / 2
}

private fun parse(lines: List<String>) = lines.map { row ->
    val positions = row.substringBefore(" @").split(", ").map { it.toLong().toDouble() }
    val velocities = row.substringAfter("@ ").split(", ").map { it.toLong().toDouble() }
    Hailstone(positions[0], positions[1], positions[2], velocities[0], velocities[1], velocities[2])
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

private fun isInFuture(x: Double, startX: Double, vx: Double) = if (vx < 0) x <= startX else x >= startX

private fun Hailstone.collidesWith(other: Hailstone) : Boolean {

    val difX = other.x - this.x
    val difY = other.y - this.y
    val difZ = other.z - this.z

    val detXY = (other.vx/vx - other.vy/vy)
    val detXZ = (other.vx/vx - other.vy/vz)
    val detYZ = (other.vy/vy - other.vz/vz)

    val t2: Double
    val t1: Double

    if (detXY != 0.0) {
        t2 = (difY/vy - difX/vx) / detXY
        t1 = (difX + other.vx*t2) / vx
    }
    else if (detXZ != 0.0) {
        t2 = (difZ/vz - difX/vx) / detXZ
        t1 = (difX + other.vx*t2) / vx
    }
    else if (detYZ != 0.0) {
        t2 = (difZ/vz - difY/vy) / detYZ
        t1 = (difY + other.vy*t2) / vy
    }
    else return false

    return t1 == (difX + other.vx*t2) / vx && t1 == (difY + other.vy*t2) / vy && t1 == (difZ + other.vz*t2) / vz
}
//add map hailstone -> path to reduce computing


private fun findRock(hailstones: List<Hailstone>, limit: Long): Hailstone? {


    for(vz in -limit..limit) {
        for (vy in -limit..limit) {
            for (vx in -limit..limit) {
                for (hailstone in hailstones) {
                    val colX = hailstone.x + hailstone.vx
                    val colY = hailstone.y + hailstone.vy
                    val colZ = hailstone.z + hailstone.vz
                    //colX = x + vx -> x = colX - vx
                    val x = colX - vx
                    val y = colY - vy
                    val z = colZ - vz
                    val rock = Hailstone(x,y,z, vx.toDouble(), vy.toDouble(), vz.toDouble())
                    if (hailstones.all { rock.collidesWith(it) })
                        return rock
                }
            }
        }
    }

    return null
}

private data class Hailstone(val x: Double, val y: Double, val z: Double, val vx: Double, val vy: Double, val vz: Double)

private fun part2(lines: List<String>, limit: Long): Long {
    val hailstones = parse(lines)
    val rock = findRock(hailstones, limit)
    rock?.let { return (rock.x + rock.y + rock.z).toLong() }
    println("Limit too small, rock not found")
    return -1L
}