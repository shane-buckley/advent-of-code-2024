fun main() {

    fun part1(input: List<String>): Int {

        fun searchUpLeft(row: Int, col: Int): Boolean {
            return try {
                input[row - 1][col - 1] == 'M' && input[row - 2][col - 2] == 'A' && input[row - 3][col - 3] == 'S'
            } catch (e: IndexOutOfBoundsException) {
                false
            }
        }

        fun searchUp(row: Int, col: Int): Boolean {
            return try {
                input[row - 1][col] == 'M' && input[row - 2][col] == 'A' && input[row - 3][col] == 'S'
            } catch (e: IndexOutOfBoundsException) {
                false
            }
        }

        fun searchUpRight(row: Int, col: Int): Boolean {
            return try {
                input[row - 1][col + 1] == 'M' && input[row - 2][col + 2] == 'A' && input[row - 3][col + 3] == 'S'
            } catch (e: IndexOutOfBoundsException) {
                false
            }
        }

        fun searchRight(row: Int, col: Int): Boolean {
            return try {
                input[row][col + 1] == 'M' && input[row][col + 2] == 'A' && input[row][col + 3] == 'S'
            } catch (e: IndexOutOfBoundsException) {
                false
            }
        }

        fun searchLeft(row: Int, col: Int): Boolean {
            return try {
                input[row][col - 1] == 'M' && input[row][col - 2] == 'A' && input[row][col - 3] == 'S'
            } catch (e: IndexOutOfBoundsException) {
                false
            }
        }

        fun searchDownLeft(row: Int, col: Int): Boolean {
            return try {
                input[row + 1][col - 1] == 'M' && input[row + 2][col - 2] == 'A' && input[row + 3][col - 3] == 'S'
            } catch (e: IndexOutOfBoundsException) {
                false
            }
        }

        fun searchDown(row: Int, col: Int): Boolean {
            return try {
                input[row + 1][col] == 'M' && input[row + 2][col] == 'A' && input[row + 3][col] == 'S'
            } catch (e: IndexOutOfBoundsException) {
                false
            }
        }

        fun searchDownRight(row: Int, col: Int): Boolean {
            return try {
                input[row + 1][col + 1] == 'M' && input[row + 2][col + 2] == 'A' && input[row + 3][col + 3] == 'S'
            } catch (e: IndexOutOfBoundsException) {
                false
            }
        }

        var matches = 0

        input.forEachIndexed { row, line ->
            line.forEachIndexed { col, value ->
                if (value == 'X') {
                    matches += if (searchUpLeft(row, col)) 1 else 0
                    matches += if (searchUp(row, col)) 1 else 0
                    matches += if (searchUpRight(row, col)) 1 else 0
                    matches += if (searchLeft(row, col)) 1 else 0
                    matches += if (searchRight(row, col)) 1 else 0
                    matches += if (searchDownLeft(row, col)) 1 else 0
                    matches += if (searchDown(row, col)) 1 else 0
                    matches += if (searchDownRight(row, col)) 1 else 0
                }
            }
        }

        return matches
    }

    fun part2(input: List<String>): Int {
        fun findCross(row: Int, col: Int): Boolean {
            return try {
                (input[row - 1][col - 1] == 'M' && input[row + 1][col + 1] == 'S' && input[row - 1][col + 1] == 'M' && input[row + 1][col - 1] == 'S') ||
                        (input[row - 1][col - 1] == 'S' && input[row + 1][col + 1] == 'M' && input[row - 1][col + 1] == 'M' && input[row + 1][col - 1] == 'S') ||
                        (input[row - 1][col - 1] == 'M' && input[row + 1][col + 1] == 'S' && input[row - 1][col + 1] == 'S' && input[row + 1][col - 1] == 'M') ||
                        (input[row - 1][col - 1] == 'S' && input[row + 1][col + 1] == 'M' && input[row - 1][col + 1] == 'S' && input[row + 1][col - 1] == 'M')
            } catch (e: IndexOutOfBoundsException) {
                false
            }
        }

        var matches = 0

        input.forEachIndexed { row, line ->
            line.forEachIndexed { col, value ->
                if (value == 'A') matches += if (findCross(row, col)) 1 else 0
            }
        }

        return matches
    }

    // Test if implementation meets criteria from the description, like:
    check(part1(listOf("XMASXMAS")) == 2)
    check(part2(listOf("MAS","MAS","MAS")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 18)
    check(part2(testInput) == 9)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
