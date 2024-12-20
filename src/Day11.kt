import kotlin.math.abs
import kotlin.math.log10

fun main() {

    fun Long.digits() = when(this) {
        0L -> 1
        else -> log10(abs(toDouble())).toInt() + 1
    }

    fun part1(input: String): Int {
        var stones: MutableList<Long> = input.split(' ').map { it.toLong() }.toMutableList()
        for (i in 0 until 25) {
            val iterator = stones.listIterator()
            while (iterator.hasNext()) {
                val next = iterator.next()
                if (next == 0L) iterator.set(1)
                else if (next.toString().length % 2 == 0) {
                    val (left, right) = next.toString().chunked(next.toString().length / 2)
                    iterator.set(left.toLong())
                    iterator.add(right.toLong())
                } else {
                    iterator.set(next * 2024)
                }
            }
        }

        return stones.size
    }

    fun part2(input: String): Int {
        val calculations: MutableMap<Long, Any> = mutableMapOf(0L to 1L)
        var stones: MutableList<Long> = input.split(' ').map { it.toLong() }.toMutableList()
        println(stones)

        for (i in 0 until 75) {
            val iterator = stones.listIterator()
            while (iterator.hasNext()) {
                val next = iterator.next()
                val result = calculations.getOrPut(next) {
                    if (next == 0L) 1L
                    else if (next.digits() % 2 == 0) {
                        val (left, right) = next.toString().chunked(next.toString().length / 2)
                        Pair(left.toLong(), right.toLong())
                    } else {
                        next * 2024L
                    }
                }

                if (result is Long)
                    iterator.set(result.toLong())
                else {
                    val (left, right) = result as Pair<Long, Long>
                    iterator.set(left)
                    iterator.add(right)
                }
            }
        }


        return stones.size
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day11_test")
    check(part1(testInput[0]) == 55312)
    //check(part2("0") == 0)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day11")
    part1(input[0]).println()
    part2(input[0]).println()
}
