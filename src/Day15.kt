fun main() {
    val input = readInput("Day15")
    val testInput = readInput("Day15_test")

    val testResult1 = part1(testInput)
    println("Test1 = $testResult1")
    check(testResult1 == 1320)
    println("Part1 = ${part1(input)}")

    val testResult2 = part2(testInput)
    println("\nTest2 = $testResult2")
    check(testResult2 == 145)
    println("Part2 = ${part2(input)}")
}

private fun part1(lines: List<String>) = lines.first().split(",").sumOf { it.hash() }

private fun String.hash() : Int {
    var current = 0
    for (char in this) {
        current += char.code
        current *= 17
        current %= 256
    }
    return current
}
private fun part2(lines: List<String>): Int {
    val steps = lines.first().split(",")
    val lightFacility = LightFacility(steps)
    return lightFacility.focusingPower()
}

private data class Box(val id: Int, val lenses: MutableMap<String,Int> = mutableMapOf())

private class LightFacility(steps: List<String>) {
    val boxes: List<Box>

    init {
        val emptyBoxes = mutableListOf<Box>()

        for (id in 0..255) {
            emptyBoxes.add(Box(id))
        }
        boxes = emptyBoxes

        for (step in steps)
            executeStep(step)
    }

    private fun executeStep(step: String) {
        val label = step.takeWhile { it != '-' && it != '='}
        val operation = step[label.length]
        val boxId = label.hash()
        if (operation == '-')
        {
            boxes[boxId].lenses.remove(label)
        }
        else
        {
            val focalLength = step.substring(label.length + 1).toInt()
            boxes[boxId].lenses[label] = focalLength
        }
    }

    fun focusingPower() = boxes.sumOf { it.focusingPower() }
    private fun Box.focusingPower() = this.lenses.toList()
        .mapIndexed { index, pair -> (index + 1) * pair.second }
        .sum() * (this.id + 1)
}