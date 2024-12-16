fun main() {

    fun part1(input: List<String>): Int {
        val (rules, cases) = input.filter { it.isNotBlank() }.partition { it.contains("|") }

        val (left, right) = rules.map {
            val (left, right) = it.split("|")
            left.toInt() to right.toInt()
        }.unzip()

        val correctCases = cases.map { case ->
            case
                .split(",")
                .map { it.toInt() }
        }
            .filter { case ->
                case
                    .withIndex()
                    .all { (index, value) ->
                        index == right.zip(left).count { (r, l) -> r == value && l in case }
                    }
            }

        return correctCases.sumOf { it[it.size / 2] }
    }

    fun part2(input: List<String>): Int {
        val (rules, cases) = input.filter { it.isNotBlank() }.partition { it.contains("|") }

        val (left, right) = rules.map {
            val (left, right) = it.split("|")
            left.toInt() to right.toInt()
        }.unzip()

        val correctCases = cases.map { case ->
            case
                .split(",")
                .map { it.toInt() }
        }
            .filter { case ->
                case
                    .withIndex()
                    .all { (index, value) ->
                        index == right.zip(left).count { (r, l) -> r == value && l in case }
                    }
            }

        val incorrectCases = cases.map { case -> case.split(',').map { it.toInt() } }.filterNot { it in correctCases }

        return incorrectCases.sumOf { case ->
            case.find {
                right.zip(left).count { (r, l) -> r == it && l in case  } == case.size / 2
            }!!.toInt()
        }
    }


    // Test if implementation meets criteria from the description, like:
//    check(part1(listOf("2|3", "3,4")) == 2)
//    check(part2(listOf("2 3", "3 2")) == 5)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 143)
    check(part2(testInput) == 123)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
