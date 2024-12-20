import kotlin.math.abs

fun main() {

    fun part1(input: List<String>): Int {
        val map = input.flatMapIndexed { rowNum: Int, row: String ->
            row.mapIndexed { colNum, col ->
                Vector(colNum, rowNum) to col.digitToInt()
            }
        }.toMap()

        val width = input[0].length
        val height = input.size

        fun Vector.inMap() = x in 0 until width && y in 0 until height

        val reachablePeaks: MutableMap<Vector, MutableSet<Vector>> = mutableMapOf()

        fun findPaths(vector: Vector, trailHead: Vector, path: MutableSet<Vector>) {
            path.add(vector)
            val adjacentPoints = listOf(
                Vector(vector.x, vector.y - 1),
                Vector(vector.x + 1, vector.y),
                Vector(vector.x, vector.y + 1),
                Vector(vector.x - 1, vector.y),
            ).filter { it.inMap() && map[it]!! - map[vector]!! == 1 && it !in path }

            if (map[vector] == 9) {
//                println(path)
                reachablePeaks[trailHead]?.add(vector)
            }

            adjacentPoints.forEach {
                findPaths(it, trailHead, path.toMutableSet())
            }
        }

        map.filterValues { it == 0 }.keys.forEach { trailHead ->
            reachablePeaks[trailHead] = mutableSetOf()
            findPaths(trailHead, trailHead, mutableSetOf())
        }



        return reachablePeaks.map { it.value.size }.sum()
    }

    fun part2(input: List<String>): Int {
        val map = input.flatMapIndexed { rowNum: Int, row: String ->
            row.mapIndexed { colNum, col ->
                Vector(colNum, rowNum) to col.digitToInt()
            }
        }.toMap()

        val width = input[0].length
        val height = input.size

        fun Vector.inMap() = x in 0 until width && y in 0 until height

        val reachablePeaks: MutableMap<Vector, MutableList<Vector>> = mutableMapOf()

        fun findPaths(vector: Vector, trailHead: Vector, path: MutableSet<Vector>) {
            path.add(vector)
            val adjacentPoints = listOf(
                Vector(vector.x, vector.y - 1),
                Vector(vector.x + 1, vector.y),
                Vector(vector.x, vector.y + 1),
                Vector(vector.x - 1, vector.y),
            ).filter { it.inMap() && map[it]!! - map[vector]!! == 1 && it !in path }

            if (map[vector] == 9) {
//                println(path)
                reachablePeaks[trailHead]?.add(vector)
            }

            adjacentPoints.forEach {
                findPaths(it, trailHead, path.toMutableSet())
            }
        }

        map.filterValues { it == 0 }.keys.forEach { trailHead ->
            reachablePeaks[trailHead] = mutableListOf()
            findPaths(trailHead, trailHead, mutableSetOf())
        }



        return reachablePeaks.map { it.value.size }.sum()
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 36)
    check(part2(testInput) == 81)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
