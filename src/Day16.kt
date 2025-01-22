import java.io.File

object Day16 {
    data class Coord(val x: Int, val y: Int) {
        operator fun rangeTo(other: Coord): List<Coord> {
            val list = mutableListOf<Coord>()
            if (this.x == other.x) {
                if (this.y < other.y) {
                    for (y in this.y..other.y) {
                        list.add(Coord(this.x, y))
                    }
                } else {
                    for (y in other.y..this.y) {
                        list.add(Coord(this.x, y))
                    }
                }
            } else {
                if (this.x < other.x) {
                    for (x in this.x..other.x) {
                        list.add(Coord(x, this.y))
                    }
                } else {
                    for (x in other.x..this.x) {
                        list.add(Coord(x, this.y))
                    }
                }
            }

            return list
        }
    }
    data class Neighbour(val coord: Coord, val directionIn: Direction, var distance: Int)

    data class PathToNode(val path: List<Coord>, val distance: Int): Comparable<PathToNode> {
        override fun compareTo(other: PathToNode): Int = this.distance.compareTo(other.distance)

        override fun toString(): String {
            return "Distance: $distance. Path: ${path.joinToString(", ") { "(${it.x}, ${it.y})"}}"
        }
    }

    class PriorityQueue<T, Q: Comparable<Q>> {
        private var backingMap = mutableMapOf<T, Q>()

        operator fun get(t: T): Q? = backingMap[t]
        operator fun set(t: T, q: Q) {
            backingMap[t] = q
        }

        fun removeFirst(): Pair<T, Q>? {
            val key = backingMap.keys.firstOrNull() ?: return null
            val value = backingMap.remove(key)?: return null
            return key to value
        }

        fun isEmpty(): Boolean = backingMap.isEmpty()

        fun sort() {
            backingMap = backingMap.toList().sortedBy { it.second }.toMap().toMutableMap()
        }

        override fun toString(): String {
            return buildString {
                backingMap.entries.forEach {
                    append("${it.key}: ${it.value}\n")
                }
            }
        }
    }

    enum class Direction(val dx: Int, val dy: Int) {
        NORTH(0, -1), SOUTH(0, 1), EAST(1, 0), WEST(-1, 0)
    }

    operator fun <T> List<List<T>>.get(coord: Coord) = this[coord.y][coord.x]
    operator fun Coord.plus(other: Pair<Int, Int>) = Coord(this.x + other.first, this.y + other.second)
    operator fun Coord.plus(direction: Direction) = Coord(this.x + direction.dx, this.y + direction.dy)

    class Part1(file: File) {
        val map: List<List<Char>> = file.readLines().map { it.toList() }
        private val start = map.withIndex().find { 'S' in it.value }?.let { list -> Coord(list.value.indexOf('S'), list.index) } ?: throw IllegalArgumentException("Map does not have a start.")
        private val end = map.withIndex().find { 'E' in it.value }?.let { list -> Coord(list.value.indexOf('E'), list.index) } ?: throw IllegalArgumentException("Map does not have an end.")
        var minDist = Int.MAX_VALUE

        private val nodes: MutableMap<Coord, MutableList<Neighbour>> = mutableMapOf()
        private val dijkstraQueue = PriorityQueue<Coord, PathToNode>()
        private val shortestPaths = mutableMapOf<Coord, Int>()
        val spacesInMin = mutableSetOf<Coord>()

        fun generateNodes() {
            searchFromNode(start, Direction.NORTH)
            searchFromNode(start, Direction.EAST)
            searchFromNode(start, Direction.SOUTH)
            searchFromNode(start, Direction.WEST)
        }

        private fun searchFromNode(start: Coord, direction: Direction) {
            var coord = start
            val isVerticalMove = direction == Direction.NORTH || direction == Direction.SOUTH
            var steps = 0
            while (map[coord] != '#') {
                coord += direction
                steps++
                val hasHorizontalNeighbour: Boolean
                val hasVerticalNeighbour: Boolean
                try {
                    hasHorizontalNeighbour = map[coord + Pair(1, 0)] == '.' || map[coord + Pair(-1, 0)] == '.'
                    hasVerticalNeighbour = map[coord + Pair(0, 1)] == '.' || map[coord + Pair(0, -1)] == '.'
                } catch (e: IndexOutOfBoundsException) {
                    return
                }
                if ((isVerticalMove && hasHorizontalNeighbour) || (!isVerticalMove) && hasVerticalNeighbour || coord == end) {
                    if (nodes[start] == null) nodes[start] = mutableListOf()
                    nodes[start]!!.add(Neighbour(coord, direction, steps))

                    if (coord !in nodes) {
                        Direction.entries.forEach {
                            searchFromNode(coord, it)
                        }
                    }
                }
            }
        }

        fun modifiedDijkstra() {
            val visitedVertices = mutableSetOf<Coord>()

            // For start node we have to check if we're going east (no turn) or if we have to turn.
            // Generalised for any start point, even though both inputs have it in bottom left.
            nodes[start]!!.forEach {
                if (it.coord.y == start.y && it.coord.x > start.x) {
                    dijkstraQueue[it.coord] = PathToNode(listOf(start, it.coord), it.distance)
                }
                else if (it.coord.x == start.x) {
                    dijkstraQueue[it.coord] = PathToNode(listOf(start, it.coord), 1000 + it.distance)
                } else {
                    dijkstraQueue[it.coord] = PathToNode(listOf(start, it.coord), 2000 + it.distance)
                }
            }

            while (!dijkstraQueue.isEmpty()) {
                // dijkstraQueue.println()
                val (currentNode, pathFromStart) = dijkstraQueue.removeFirst()!!
                val distFromStart = pathFromStart.distance
                if (currentNode == end) {
                    minDist = pathFromStart.distance
                }
                nodes[currentNode]!!.forEach {
                    if (it.coord !in visitedVertices)
                        if (it.distance + distFromStart + 1000 < (dijkstraQueue[it.coord]?.distance ?: Int.MAX_VALUE))
                            dijkstraQueue[it.coord] = PathToNode(pathFromStart.path + it.coord, dijkstraQueue[it.coord]?.distance?.coerceAtMost(it.distance + distFromStart + 1000) ?: (it.distance + distFromStart + 1000))
                }

                dijkstraQueue.sort()

                visitedVertices += currentNode
                shortestPaths += currentNode to pathFromStart.distance
            }

        }

        fun findAllMinPaths(currentNode: Coord = start, distToHere: Int = 0, pathToHere: List<Coord> = listOf(start)) {
            if (distToHere > (shortestPaths[currentNode] ?: Int.MIN_VALUE)) return

            nodes[currentNode]!!.forEach {
                if (it.coord !in pathToHere) {
                    val path = pathToHere.toMutableList()
                    path.add(it.coord)
                    if (it.coord == end && distToHere + it.distance + 1000 <= minDist) {
                        val spacesTaken = mutableListOf<Coord>()
                        path.zipWithNext().forEach { (first, second) ->
                            for (space in first..second) {
                                spacesTaken.add(space)
                            }
                        }
                        spacesInMin.addAll(spacesTaken)
                        // println("$path\n${distToHere + it.distance + 1000}")
                    }
                    if (currentNode == start && it.coord.y == start.y) {
                        findAllMinPaths(it.coord, distToHere + it.distance, path)
                    } else {
                        findAllMinPaths(it.coord, distToHere + it.distance + 1000, path)
                    }
                }
            }
        }
    }

}

fun main() {
    val p1 = Day16.Part1(File("src/Day16.txt"))
    p1.generateNodes()
    p1.modifiedDijkstra()
    p1.minDist.println()
    p1.findAllMinPaths()
    p1.spacesInMin.size.println()

    /*p1.map.withIndex().joinToString("\n") { (rowNum, row) ->
        row.withIndex().map { if (Day16_2.Coord(it.index, rowNum) in p1.nodesInMin) 'O' else it.value }.joinToString("")
    }.println()*/
}