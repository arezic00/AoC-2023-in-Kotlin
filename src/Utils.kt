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

fun toRowCol(direction: Direction, fromRow: Int, fromCol: Int) : Pair<Int,Int> {
    return when (direction) {
        Direction.NORTH -> Pair(fromRow - 1,fromCol)
        Direction.EAST -> Pair(fromRow,fromCol + 1)
        Direction.SOUTH -> Pair(fromRow + 1,fromCol)
        Direction.WEST -> Pair(fromRow,fromCol - 1)
    }
}

fun Direction.rotateCW() = if (this != Direction.WEST) Direction.values()[this.ordinal + 1] else Direction.NORTH

fun Direction.rotateCCW() = if (this != Direction.NORTH) Direction.values()[this.ordinal -1] else Direction.WEST
