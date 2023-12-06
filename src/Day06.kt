fun main() {
    fun part1(input: List<String>): Int {
        return Day06.parse1(input)
    }

    fun part2(input: List<String>): Int {
        return Day06.parse2(input)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288)
    check(part2(testInput) == 71503)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}

object Day06 {

    //1mm per second for each second the button is held, starts at 0
    //totaltimeraced = totaltime - timeheld
    //distance = totaltimeraced*speed
    //distance = (tot - timeheld)(timeheld)
    //distance = ( square function)

    //looking for number of ways i can beat each record
    //multiply the numbers for each record

    fun parse1(lines: List<String>): Int {
        val times = lines[0].removePrefix("Time: ").trim().split("\\s+".toRegex()).map { it.toInt() }
        val recordDistance = lines[1].removePrefix("Distance: ").trim().split("\\s+".toRegex()).map { it.toInt() }
        var multiplicator = 1
        for (index in times.indices) {
            multiplicator *= (1 until times[index]).count { (times[index] - it)*it > recordDistance[index] }
        }
        return multiplicator
    }

    fun parse2(lines: List<String>) : Int {
        var time1 = ""
        lines[0].removePrefix("Time: ").trim().split("\\s+".toRegex()).forEach {
            time1 += it
        }
        var s = ""
        lines[1].removePrefix("Distance: ").trim().split("\\s+".toRegex()).forEach {
            s += it
        }
        val time = time1.toLong()
        val distance = s.toLong()
        val result = (1 until time).count { (time - it)*it > distance }
        result.println()

        return result
    }

}