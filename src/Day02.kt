fun main() {
    fun part1(input: List<String>): Int {
        return solvePart1(input)
    }

    fun part2(input: List<String>): Int {
        return solvePart2(input)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

private const val RED_MAX = 12
private const val GREEN_MAX = 13
private const val BLUE_MAX = 14
private fun firstNumberWithEndIndex(string: String): Pair<Int, Int> {
    var number = ""
    var currentIndex = 0
    for (index in string.indices) {
        if (string[index].isDigit()) number += string[index]
        else {
            currentIndex = index
            break
        }
    }
    return Pair(number.toInt(), currentIndex)
}
private fun lineToGame(line: String): Game {
    var substring = line.substring(5)
    val idWithIndex = firstNumberWithEndIndex(substring)
    substring = substring.substring(idWithIndex.second + 2)

    val handfuls = mutableListOf<HandfulOfCubes>()
    var handful = HandfulOfCubes()

    while (true) {
        val numOfCubesWithEndIndex = firstNumberWithEndIndex(substring)
        substring = substring.substring(numOfCubesWithEndIndex.second + 1)
        when (substring[0]) {
            'r' -> {
                handful.red = numOfCubesWithEndIndex.first
                substring = substring.substring(2)
            }

            'g' -> {
                handful.green = numOfCubesWithEndIndex.first
                substring = substring.substring(4)
            }

            'b' -> {
                handful.blue = numOfCubesWithEndIndex.first
                substring = substring.substring(3)
            }
        }
        if (substring.length == 1) {
            handfuls.add(handful)
            break
        }

        if (substring[1] == ';') {
            handfuls.add(handful)
            handful = HandfulOfCubes()
        }

        substring = substring.substring(3)
    }
    return Game(idWithIndex.first, handfuls)

}
private fun linesToGames(lines: List<String>): List<Game> {
    val result = mutableListOf<Game>()
    lines.forEach { result.add(lineToGame(it)) }
    return result
}
private fun isGamePossible(game: Game): Boolean {
    game.handfuls.forEach {
        if (it.red > RED_MAX || it.green > GREEN_MAX || it.blue > BLUE_MAX)
            return false
    }
    return true
}
private fun sumPossibleGameIDs(games: List<Game>): Int {
    return games.sumOf { if (isGamePossible(it)) it.id else 0 }
}
fun solvePart1(lines: List<String>): Int {
    return sumPossibleGameIDs(linesToGames(lines))
}
fun solvePart2(lines: List<String>): Int {
    return sumPowerOfSets(linesToGames(lines))
}
private fun maxBlueCubes(game: Game): Int {
    var max = 0
    game.handfuls.forEach { if (it.blue > max) max = it.blue }
    return max
}
private fun maxRedCubes(game: Game): Int {
    var max = 0
    game.handfuls.forEach { if (it.red > max) max = it.red }
    return max
}
private fun maxGreenCubes(game: Game): Int {
    var max = 0
    game.handfuls.forEach { if (it.green > max) max = it.green }
    return max
}
private fun powerOfSet(game: Game): Int {
    return maxBlueCubes(game) * maxGreenCubes(game) * maxRedCubes(game)
}
private fun sumPowerOfSets(games: List<Game>): Int {
    return games.sumOf { powerOfSet(it) }
}
private data class Game(val id: Int, val handfuls: List<HandfulOfCubes>)
private data class HandfulOfCubes(var red: Int = 0, var green: Int = 0, var blue: Int = 0)