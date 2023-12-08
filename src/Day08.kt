fun main() {
    fun part1(input: List<String>): Int {
        return Day08.part1(input)
    }

    fun part2(input: List<String>): Long {
        return Day08.part2(input)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 2)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}

object Day08 {


    fun part1(lines: List<String>): Int {
        val instructions = lines.first()
        val nodes = lines.drop(2).map {
            Node(it)
        }
        return steps(nodes, instructions,"AAA", "ZZZ")
    }

    private fun steps(nodes: List<Node>, instructions: String,startRegex: String, endRegex: String): Int {
        var startIndex = nodes.indexOfFirst {startRegex.toRegex().matches(it.head) }
        var ctr = 0
        while (!(endRegex.toRegex().matches(nodes[startIndex].head)))
        {
            for (instruction in instructions)
            {
                ctr++
                if (instruction == 'L')
                {
                    startIndex = nodes.indexOfFirst { it.head == nodes[startIndex].left }
                }
                else if (instruction == 'R')
                {
                    startIndex = nodes.indexOfFirst { it.head == nodes[startIndex].right }
                }
                if (endRegex.toRegex().matches(nodes[startIndex].head))
                    break
            }
        }
        return ctr
    }

    fun part2(lines: List<String>): Long {
        val instructions = lines.first()
        val nodes = lines.drop(2).map {
            Node(it)
        }
        return loopsMergeAtStep(nodes, instructions)

    }

    private fun loopsMergeAtStep(nodes: List<Node>, instructions: String): Long {
        val loopSizes = nodes
            .filter { it.head.last() == 'A' }
            .map { steps(nodes, instructions, it.head, "[A-Z]{2}Z$") }

        val primeFactors = loopSizes.map { it.primeFactors(primesUpTo(loopSizes.max())) }

        val combinedPrFac = combinePrimeFactors(primeFactors)
        val lcm = combinedPrFac.fold(1L) { acc, prime ->
            acc * prime.value
        }
        return lcm
    }

    private fun combinePrimeFactors(primeLists: List<List<Prime>>) : List<Prime> {
        val union = primeLists.flatten().distinct()

        return union.map { prime ->
            union.filter { it.value == prime.value }.maxBy { it.amount } }
            .distinct()
    }

    private fun Int.primeFactors(primesUpToThis: List<Int>) : List<Prime> {
        var dividend = this
        val result = mutableListOf<Prime>()

        for (prime in primesUpToThis) {
            if (dividend == 1)
                break

            var ctr = 0
            while (dividend % prime == 0) {
                ctr++
                dividend /= prime
            }

            if (ctr != 0)
                result.add(Prime(prime,ctr))
        }
        return result
    }

    private fun primesUpTo(limit: Int) : List<Int> {
        val primes = mutableListOf(2)
        for (ctr in 3..limit step 2) {
            if (primes.all { ctr % it != 0})
                primes.add(ctr)
        }
        return primes
    }

    private fun Node(line: String) : Node {
        val elements = line.dropLast(1).split(" = (",", ")
        return Node(elements[0],elements[1],elements[2])
    }

    private data class Node(val head: String, val left: String, val right: String)

    private data class Prime(val value: Int, val amount: Int)
}