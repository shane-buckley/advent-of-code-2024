fun main() {

    fun getMap(input: List<String>): Map<Vector, Char> {
        val map = input.mapIndexed { rowNum, row ->
            row.mapIndexed { colNum, value ->
                Pair(Vector(colNum, rowNum), value)
            }
        }.flatten().associate { it.first to it.second }
        return map
    }

    fun part1(input: List<String>): Int {
        fun Vector.inMap(): Boolean {
            return x in 0 until input[0].length && y in input.indices
        }

        val map = getMap(input)
        val (antennae, antennaSymbols) = map.filterValues { it != '.' }.let { Pair(it.keys, it.values.toSet()) }

        val antiNodes = mutableSetOf<Vector>()

        antennaSymbols.forEach { symbol ->
            val matchingAntennae = antennae.filter { map[it] == symbol }
            matchingAntennae.forEachIndexed { index, antenna1 ->
                matchingAntennae.takeLast(matchingAntennae.size - index - 1).forEach { antenna2 ->
                    val dist = antenna2 - antenna1
                    val antiNodePair = listOf(antenna1 - dist, antenna2 + dist)
                    antiNodes.addAll(antiNodePair.filter { it.inMap() })
                }
            }
        }

        return antiNodes.size
    }

    fun part2(input: List<String>): Int {
        fun Vector.inMap(): Boolean {
            return x in 0 until input[0].length && y in input.indices
        }

        val map = getMap(input)
        val (antennae, antennaSymbols) = map.filterValues { it != '.' }.let { Pair(it.keys, it.values.toSet()) }
        val antiNodes = mutableSetOf<Vector>()

        antennaSymbols.forEach { symbol ->
            val matchingAntennae = antennae.filter { map[it] == symbol }
            matchingAntennae.forEachIndexed { index, antenna1 ->
                matchingAntennae.takeLast(matchingAntennae.size - index - 1).forEach { antenna2 ->
                    val dist = antenna2 - antenna1
                    var point = antenna1
                    do {
                        point -= dist
                    } while (point.inMap())
                    point += dist

                    do {
                        antiNodes += point
                        point += dist
                    } while (point.inMap())
                }
            }
        }

        return antiNodes.size
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 14)
    check(part2(testInput) == 34)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
