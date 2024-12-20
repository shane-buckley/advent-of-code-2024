class Guard(var position: Coordinate, var direction: Direction, val maze: List<MutableList<Char>>) {

    private var nextPosition: Coordinate = position + direction

    private var leaveBehind = when(direction) {
        Direction.UP, Direction.DOWN -> '|'
        else -> '-'
    }

    fun step() {
        if (maze[nextPosition] == '#') {
            direction = direction.turn()
            maze[position] = when(direction) {
                Direction.UP -> '^'
                Direction.RIGHT -> '>'
                Direction.DOWN -> 'v'
                else -> '<'
            }
            leaveBehind = '+'
        } else {
            maze[position] = leaveBehind
            if (maze[nextPosition] == '|' && direction in listOf(Direction.LEFT, Direction.RIGHT)) leaveBehind = '+'
            else if (maze[nextPosition] == '-' && direction in listOf(Direction.UP, Direction.DOWN)) leaveBehind = '+'
            else leaveBehind = when(direction) {
                Direction.UP, Direction.DOWN -> '|'
                else -> '-'
            }

            maze[nextPosition] = when(direction) {
                Direction.UP -> '^'
                Direction.RIGHT -> '>'
                Direction.DOWN -> 'v'
                else -> '<'
            }

            position += direction
        }
        nextPosition = position + direction
    }

}

operator fun List<List<Char>>.get(coordinate: Coordinate) = get(coordinate.y)[coordinate.x]

operator fun <T: Any> List<MutableList<T>>.set(coordinate: Coordinate, newChar: T) {
    get(coordinate.y)[coordinate.x] = newChar
}

data class Coordinate(val y: Int, val x: Int) {
    operator fun plus(direction: Direction): Coordinate {
        return Coordinate(y + direction.y, x + direction.x)
    }
}

enum class Direction(val y: Int, val x: Int) {
    UP(-1, 0),
    RIGHT(0, 1),
    DOWN(1, 0),
    LEFT(0, -1);

    fun turn(): Direction = Direction.entries[(this.ordinal + 1) % 4]
}

fun main() {

    fun part1(input: List<String>): Int {
        val maze = input.map { it.toMutableList() }
        val start = maze.flatMap { it.toList() }.withIndex().find { it.value in "^>v<" }
        val index = start!!.index
        val direction = when (start.value) {
            '^' -> Direction.UP
            '>' -> Direction.RIGHT
            'v' -> Direction.DOWN
            else -> Direction.LEFT
        }
        val startingCoordinate = Coordinate(index / maze[0].size, index % maze[0].size)

        val guard = Guard(startingCoordinate, direction, maze)

        while (true) {
            try {
                guard.step()
                //println(guard.maze.joinToString("") { "${it.joinToString("")}\n" })
            } catch (e: IndexOutOfBoundsException) {
                break
            }
        }
        return guard.maze.flatten().filterNot { it == '.' || it == '#' }.size
    }

    fun part2(input: List<String>): Int {
        val initialMaze = input.map { it.toMutableList() }
        val start = initialMaze.flatMap { it.toList() }.withIndex().find { it.value in "^>v<" }
        val index = start!!.index
        val direction = when (start.value) {
            '^' -> Direction.UP
            '>' -> Direction.RIGHT
            'v' -> Direction.DOWN
            else -> Direction.LEFT
        }
        val startingCoordinate = Coordinate(index / initialMaze[0].size, index % initialMaze[0].size)

        var loops = 0

        for ((y, row) in initialMaze.withIndex()) {
            for ((x, col) in row.withIndex()) {
                val maze = initialMaze.map { it.map { Char(it.code) }.toMutableList()}
                val initialCharacter = Char(col.code)
                if (col == '#' || col in listOf('^', '>', 'v', '<')) continue

                val positions = mutableSetOf(Pair(startingCoordinate, direction))

                maze[y][x] = '#'
                val guard = Guard(startingCoordinate, direction, maze)
                var loop = true
                while (true) {
                    try {
                        guard.step()
                        if (Pair(guard.position, guard.direction) in positions) break
                        positions += Pair(guard.position, guard.direction) // Forgot this line for an embarrassingly long time.
                    } catch (e: IndexOutOfBoundsException) {
                        loop = false
                        break
                    }
                }


                if (loop) loops++

                maze[y][x] = initialCharacter
            }
        }

        return loops
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
