import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)


/**
 * Input lines transformations
 */
fun List<String>.columns() : List<String> {
    val result = mutableListOf<String>()
    for (col in this.first().indices) {
        var resultString = ""
        for (row in this.indices) {
            resultString += this[row][col]
        }
        result.add(resultString)
    }
    return result
}

fun List<String>.rotateCCW(): List<String> {

    return this.columns().reversed()
}

fun List<String>.rotateCW(): List<String> {
    return this.reversed().columns()
}

enum class Direction {
    NORTH, EAST, SOUTH, WEST
}

fun toRowCol(direction: Direction, fromRow: Int, fromCol: Int, steps: Int = 1) : Pair<Int,Int> {
    return when (direction) {
        Direction.NORTH -> Pair(fromRow - steps,fromCol)
        Direction.EAST -> Pair(fromRow,fromCol + steps)
        Direction.SOUTH -> Pair(fromRow + steps,fromCol)
        Direction.WEST -> Pair(fromRow,fromCol - steps)
    }
}

fun Direction.rotateCW() = Direction.entries[(this.ordinal + 1) % 4]

fun Direction.rotateCCW() = Direction.entries[(this.ordinal -1) % 4]

fun Direction.opposite() = Direction.entries[(this.ordinal + 2) % 4]

fun Direction.toChar() = when (this) {
    Direction.NORTH -> '^'
    Direction.EAST -> '>'
    Direction.SOUTH -> 'V'
    Direction.WEST -> '<'
}
