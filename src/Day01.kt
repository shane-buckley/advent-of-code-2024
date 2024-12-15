import kotlin.math.abs

fun main() {
    fun splitLists(input: List<String>): Pair<List<Long>, List<Long>> {
        val (left, right) = input.map {
            val (a, b) = it.split("""\s+""".toRegex(), 2)
            a.toLong() to b.toLong()
        }.unzip()

        return Pair(left, right)
    }

    fun part1(input: List<String>): Int {
        val (left, right) = splitLists(input)
        return left.sorted().zip(right.sorted()).sumOf { (l, r) ->
            abs(l - r)
        }.toInt()
    }

    fun part2(input: List<String>): Int {
        val (left, right) = splitLists(input)
        return  left.sumOf { l -> l * right.count{ r -> r == l } }.toInt()
    }

    // Test if implementation meets criteria from the description, like:
    check(part1(listOf("2 3", "3 4")) == 2)
    check(part2(listOf("2 3", "3 2")) == 5)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
