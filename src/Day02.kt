import kotlin.math.abs

fun main() {

    fun isSafe(levels: List<Int>) =
        (levels.sorted() == levels || levels.sortedDescending() == levels) &&
                levels.zipWithNext { a, b -> abs(a - b) }.all { it in 1..3 }

    fun part1(input: List<String>): Int {
        return input.count { line ->
            val levels = line.split("""\s+""".toRegex()).map { it.toInt() }

            isSafe(levels)
        }
    }

    fun part2(input: List<String>): Int {
        return input.count { line ->
            line.indices.any { removedIndex ->
                val modifiedLine = line.split("""\s+""".toRegex()).filterIndexed { index, _ -> index != removedIndex }
                    .map { it.toInt() }

                isSafe(modifiedLine)
            }
        }
    }

    // Test if implementation meets criteria from the description, like:
    check(part1(listOf("1 2 3 4 5")) == 1)
    check(part2(listOf("1 3 2 4 5")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
