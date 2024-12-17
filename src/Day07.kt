import java.util.*
import kotlin.math.pow

fun main() {

    fun part1(input: List<String>): Long {

        var answer = 0L

        input.forEach { case ->
            val (total, operands) = Pair(
                case.substringBefore(":").trim().toLong(),
                case.substringAfter(":").trim().split("""\s+""".toRegex()).map { it.toInt() }
            )




            val operatorCombos = (0 until 2f.pow(operands.size - 1).toInt()).map {
                it.toString(2).padStart(operands.size - 1, '0')
            }

            for (operators in operatorCombos) {
                val queue: Queue<Int> = LinkedList(operands)
                val caseTotal = operators.fold(queue.remove().toLong()) { acc: Long, operator: Char ->
                    if (operator == '0') acc + queue.remove() else acc * queue.remove()
                }

                if (caseTotal == total) {
                    answer += total
                    break
                }
            }
        }

        return answer
    }

    fun part2(input: List<String>): Long {
        var answer = 0L

        input.forEach { case ->
            val (total, operands) = Pair(
                case.substringBefore(":").trim().toLong(),
                case.substringAfter(":").trim().split("""\s+""".toRegex()).map { it.toInt() }
            )




            val operatorCombos = (0 until 3f.pow(operands.size - 1).toInt()).map {
                it.toString(3).padStart(operands.size - 1, '0')
            }

            for (operators in operatorCombos) {
                val queue: Queue<Int> = LinkedList(operands)
                val caseTotal = operators.fold(queue.remove().toLong()) { acc: Long, operator: Char ->
                    when (operator) {
                        '0' -> acc + queue.remove()
                        '1' -> acc * queue.remove()
                        else -> "$acc${queue.remove()}".toLong()
                    }
                }

                if (caseTotal == total) {
                    answer += total
                    break
                }
            }
        }

        return answer
    }

    check(part2(listOf("7290: 6 8 6 15")) == 7290L)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 3749L)
    check(part2(testInput) == 11387L)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
