import java.io.File

object Day19 {

    private val towels: List<String>
    private val sequences: List<String>
    private var waysToMake: MutableMap<String, Long> = mutableMapOf("" to 1L)

    init {
        val (towels, sequences) = File("src/Day19.txt").readText().split("\r\n\r\n").let { Pair(it[0].replace("\n", "").split(", "), it[1].split("\r\n")) }
        this.towels = towels.sortedBy { it.length }.reversed()
        this.sequences = sequences
    }

    private fun checkSequence(sequence: String): Long {
        return waysToMake.getOrPut(sequence) {
            towels.fold(0L) { acc: Long, towel: String ->
                if (sequence.startsWith(towel)) acc + checkSequence(sequence.removePrefix(towel))
                else acc
            }
        }
    }

    fun checkSequences() {
        var sequencesPossible = 0L
        val arrangements = sequences.fold(0L) { acc: Long, sequence: String ->
            val possibilities = checkSequence(sequence)
            if (possibilities > 0) sequencesPossible++
            acc + possibilities
        }

        println("$sequencesPossible of the sequences are possible, with $arrangements total arrangements.")
    }
}

fun main() {
    Day19.checkSequences()
}