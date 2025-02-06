import java.io.File

/*
    It's going to be memoization isn't it? Build up a set of substrings that are possible so that the work of looping through all the towels is avoided.
*/

object Day19 {

    private val towels: List<String>
    private val sequences: List<String>
    private val N: Int
    private var waysToMake: MutableMap<String, Long> = mutableMapOf()

    init {
        val (towels, sequences) = File("src/Day19.txt").readText().split("\r\n\r\n").let { Pair(it[0].replace("\n", "").split(", "), it[1].split("\r\n")) }
        this.towels = towels.sortedBy { it.length }.reversed()
        this.sequences = sequences
        N = this.towels.maxOf { it.length }
    }

    private fun checkSequence(sequence: String): Long {
        if (sequence.isEmpty()) return 1

        var sum = 0L

        if (sequence in waysToMake) return waysToMake[sequence]!!

        for (towel in towels) {
            if (sequence.startsWith(towel)) {
                sum += checkSequence(sequence.removePrefix(towel))
            }
        }

        waysToMake[sequence] = sum

        return sum
    }

    fun checkSequences() {
        var sequencesPossible = 0L
        var arrangements = 0L
        for (sequence in sequences) {
            val a = checkSequence(sequence)
            if (a > 0) {
                sequencesPossible++
                arrangements += a
            }
        }

        println("$sequencesPossible of the sequences are possible, with $arrangements total arrangements.")
    }
}

fun main() {
    Day19.checkSequences()
}