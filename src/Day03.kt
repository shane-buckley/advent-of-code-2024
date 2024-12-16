fun main() {

    fun part1(input: List<String>): Int {
        val pattern = """mul\((\d{1,3}),(\d{1,3})\)""".toRegex()
        val matches = pattern.findAll(input.joinToString())
        return matches.sumOf {
            val (a, b) = it.destructured
            a.toInt() * b.toInt()
        }
    }

    fun part2(input: List<String>): Int {
        val mulPattern = """mul\((\d{1,3}),(\d{1,3})\)""".toRegex()
        val doPattern = """do\(\)""".toRegex()
        val dontPattern = """don't\(\)""".toRegex()

        val pattern = "($mulPattern)|($doPattern)|($dontPattern)".toRegex()
        val matches = pattern.findAll(input.joinToString())
        var active = true
        return matches.sumOf{ line ->
            when(line.value) {
                "do()" -> {
                    active = true
                    0
                }
                "don't()" -> {
                    active = false
                    0
                }
                else -> { // mul
                    val (_, a, b) = line.destructured
                    (a.toInt() * b.toInt())  * (if (active) 1 else 0)
                }
            }
        }

    }

    // Test if implementation meets criteria from the description, like:
    check(part1(listOf("xmul(2,4)%&mul[3,7]")) == 8)
    check(part2(listOf("don't()mul(2,4)do()mul(1,3)")) == 3)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 161)
    check(part2(testInput) == 48)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
