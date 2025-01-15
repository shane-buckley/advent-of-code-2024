import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.readLines

typealias Coord = Pair<Int, Int>

class Day15 {

    inner class Part1(file: Path) {

        operator fun Coord.plus(otherPair: Pair<Int, Int>): Coord = Pair(this.first + otherPair.first, this.second + otherPair.second)
        operator fun Coord.minus(otherPair: Pair<Int, Int>): Coord = Pair(this.first - otherPair.first, this.second - otherPair.second)
        operator fun MutableList<MutableList<Char>>.set(coords: Pair<Int, Int>, c: Char) { this[coords.second][coords.first] = c }
        operator fun List<List<Char>>.get(coords: Pair<Int, Int>): Char { return this[coords.second][coords.first] }

        val map: MutableList<MutableList<Char>>

        val instructions: MutableList<Char>
        private val robotPos: Coord
            get() = map.withIndex().first { it.value.contains('@') }.let { Pair(it.value.indexOf('@'), it.index) }
        private val positionsToMove: MutableList<Coord> = mutableListOf()

        init {
            file.readLines().withIndex().partition {
                it.index <= file.readLines().indexOf("")
            }.let { pair ->
                map = pair.first.map { it.value.toMutableList() }.dropLast(1).toMutableList()
                instructions = pair.second.joinToString("") { it.value }.toMutableList()
            }
        }

        fun performInstruction() {

            positionsToMove.clear()

            val move = when(instructions.removeFirst()) {
                '^' -> Pair(0, -1)
                '>' -> Pair(1, 0)
                'v' -> Pair(0, 1)
                '<' -> Pair(-1, 0)
                else -> Pair(0, 0)
            }

            val oldRobotPos = robotPos.copy()
            tryMove(robotPos, move)
            positionsToMove.forEach { map[it + move] = map[it]  }
            if (positionsToMove.isNotEmpty()) map[oldRobotPos] = '.'
        }

        private fun tryMove(startPos: Coord, move: Pair<Int, Int>): Boolean =
            when (map[startPos + move]) {
                'O' -> {
                    if (tryMove(startPos + move, move)) {
                        positionsToMove += startPos
                        true
                    } else false
                }
                '.' -> {
                    positionsToMove += startPos
                    true
                }
                else -> false
            }

        fun printMap() {
            map.joinToString("\n") { it.joinToString("") }.println()
        }

        fun gps() {
            var tot = 0
            map.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { colIndex, col ->
                    if (col == 'O') tot += 100 * rowIndex + colIndex
                }
            }

            tot.println()
        }
    }

    inner class Part2(file: Path) {
        operator fun Coord.plus(otherPair: Pair<Int, Int>): Coord = Pair(this.first + otherPair.first, this.second + otherPair.second)
        operator fun Coord.minus(otherPair: Pair<Int, Int>): Coord = Pair(this.first - otherPair.first, this.second - otherPair.second)
        operator fun MutableList<MutableList<Char>>.set(coords: Pair<Int, Int>, c: Char) { this[coords.second][coords.first] = c }
        operator fun List<List<Char>>.get(coords: Pair<Int, Int>): Char { return this[coords.second][coords.first] }

        val map: MutableList<MutableList<Char>> = mutableListOf()

        val instructions: MutableList<Char>
        private val robotPos: Coord
            get() = map.withIndex().first { it.value.contains('@') }.let { Pair(it.value.indexOf('@'), it.index) }
        private val positionsToMove: MutableSet<Coord> = mutableSetOf()
        private val positionsToScan: MutableList<Coord> = mutableListOf()
        private val scannedPositions: MutableSet<Coord> = mutableSetOf()

        init {
            file.readLines().withIndex().partition {
                it.index <= file.readLines().indexOf("")
            }.let { pair ->
                val baseMap = pair.first.map { it.value.toList() }.dropLast(1).toList()
                instructions = pair.second.joinToString("") { it.value }.toMutableList()
                baseMap.forEach {
                    map += buildList {
                        it.forEach {
                            when(it) {
                                '#' -> addAll(listOf('#', '#'))
                                'O' -> addAll(listOf('[', ']'))
                                '@' -> addAll(listOf('@', '.'))
                                else -> addAll(listOf('.', '.'))
                            }
                        }
                    }.toMutableList()
                }
            }
        }

        fun performInstruction() {
            positionsToMove.clear()
            scannedPositions.clear()
            positionsToScan.clear()

            val moveChar = instructions.removeFirst()
            //moveChar.println()
            val move = when(moveChar) {
                '^' -> Pair(0, -1)
                '>' -> Pair(1, 0)
                'v' -> Pair(0, 1)
                '<' -> Pair(-1, 0)
                else -> Pair(0, 0)
            }

            val oldRobotPos = robotPos.copy()

            positionsToScan += robotPos + move

            if(scan(move)) {
                positionsToMove.reversed().forEach {
                    map[it + move] = map[it]
                    map[it] = '.'
                }
                map[oldRobotPos] = '.'
            }
        }

        fun scan(move: Pair<Int, Int>): Boolean {
            while (positionsToScan.isNotEmpty()) {
                val scanningPosition = positionsToScan.removeFirst()
                positionsToMove += scanningPosition - move
                scannedPositions += scanningPosition
                    when(map[scanningPosition]) {
                    '.' -> {}
                    '#' -> return false
                    '[' -> {
                        val a = scanningPosition + move
                        val b = a + Pair(1, 0)
                        if (a !in scannedPositions) positionsToScan += a
                        if (b !in scannedPositions) positionsToScan += b
                    }
                    ']' -> {
                        val a = scanningPosition + move
                        val b = a + Pair(-1, 0)
                        if (a !in scannedPositions) positionsToScan += a
                        if (b !in scannedPositions) positionsToScan += b
                    }
                }
            }

            return true

        }


        fun printMap() {
            map.joinToString("\n") { it.joinToString("") }.println()
        }

        fun gps() {
            var tot = 0
            map.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { colIndex, col ->
                    if (col == '[') {
                        tot += 100 * rowIndex + colIndex
                    }
                }
            }

            tot.println()
        }
    }
}

fun main() {
    val day15 = Day15()
     val part1 = day15.Part1(Path("src/Day15.txt"))
    while (part1.instructions.isNotEmpty()) {
        //part1.printMap()
        part1.performInstruction()
    }
    part1.printMap()
    part1.gps()

    val part2 = day15.Part2(Path("src/Day15.txt"))
    while (part2.instructions.isNotEmpty()) {
        //part2.printMap()
        part2.performInstruction()
    }
    part2.printMap()
    part2.gps()


}