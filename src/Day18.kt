import java.io.File


/*
No real need for concept of nodes. Every free space is a node and each distance is therefore 1.
This means more nodes, so Dijkstra will probably be too slow, or won't work at all due to memory limits.
Instead, use A*, with the heuristic just being the "as the crow flies" distance i.e. mazeSize - x + mazeSize - y.
This satisfies the criterium of being an optimistic heuristic.
A* is essentially Dijkstra's (a priority queue of nodes), but the value by which the queue is sorted is the distance from the start, plus the heuristic.
 */

const val TEST = false

object Day18_1 {

    lateinit var shortestPath: Set<Coord>

    operator fun Array<Array<Char>>.get(coord: Coord) = this[coord.second][coord.first]


    private val mazeSize = if (TEST) 7 else 71
    private val input = (if (TEST) File("src/Day18_test.txt") else File("src/Day18.txt")).readLines()
    val maze = Array(if (TEST) 7 else 71) { row -> Array(if (TEST) 7 else 71) { col ->
        if ("$col,$row" in input.take(if (TEST) 12 else 1024)) '#' else '.'
    } }

    val neighbours = mutableMapOf<Coord, List<Coord>>()

    private val start = 0 to 0
    private val end = mazeSize-1 to mazeSize-1

    val nodes = mutableSetOf<Coord>()

    fun createNodes() {
        for (row in maze.indices) {
            for (col in maze.indices) {
                val node = Coord(col, row)
                if (maze[node] == '#') continue
                nodes.add(node)
                neighbours[node] = buildList {
                    for (possibleNeighbour in listOf(
                        node.copy(first = node.first - 1),
                        node.copy(second = node.second - 1),
                        node.copy(first = node.first + 1),
                        node.copy(second = node.second + 1)
                    )) {
                        if (possibleNeighbour.first in 0 until mazeSize && possibleNeighbour.second in 0 until mazeSize && maze[possibleNeighbour] == '.') {
                            add(possibleNeighbour)
                        }
                    }
                }.toList()
            }
        }
    }

    fun aStar() {
        val queue = PriorityQueue()
        while (queue.isNotEmpty()) {
            val currentNode = queue.getNext()
            if (currentNode.coord == end) {
                currentNode.distFromStart.println()
                // currentNode.pathToHere.println()
                shortestPath = currentNode.pathToHere.toSet()
            }
            for (neighbour in neighbours[currentNode.coord]!!) {
                val newPath = currentNode.pathToHere.toMutableSet()
                newPath.add(neighbour)
                queue.updateDist(neighbour, currentNode.distFromStart + 1, newPath)
            }
        }
    }

    class PriorityQueue {
        private val backingList: MutableList<PQueueEntry> = nodes.map { PQueueEntry(it, if (it == start) 0 else mazeSize * mazeSize) }.toMutableList()

        operator fun MutableList<PQueueEntry>.get(node: Coord): PQueueEntry = backingList.first { it.coord == node }

        fun updateDist(coord: Coord, newDist: Int, pathToHere: Set<Coord>) {
            if (coord in backingList.map { it.coord })
            backingList[coord].apply {
                distFromStart = newDist
                this.pathToHere = pathToHere
            }
        }

        fun getNext(): PQueueEntry {
            backingList.sortBy { it.distFromStart + it.heuristic }
            return backingList.removeFirst()
        }

        inner class PQueueEntry(val coord: Coord, var distFromStart: Int) {
            var pathToHere = setOf(start)
            val heuristic = mazeSize - coord.first + mazeSize - coord.second
        }

        fun isNotEmpty() = backingList.isNotEmpty()
    }

}

class Day18_2(val bytesToDrop: Int) {

    lateinit var shortestPath: Set<Coord>

    operator fun Array<Array<Char>>.get(coord: Coord) = this[coord.second][coord.first]


    private val mazeSize = if (TEST) 7 else 71
    private val input = (if (TEST) File("src/Day18_test.txt") else File("src/Day18.txt")).readLines()
    val maze = Array(if (TEST) 7 else 71) { row -> Array(if (TEST) 7 else 71) { col ->
        if ("$col,$row" in input.take(bytesToDrop)) '#' else '.'
    } }

    val neighbours = mutableMapOf<Coord, List<Coord>>()

    private val start = 0 to 0
    private val end = mazeSize-1 to mazeSize-1

    val nodes = mutableSetOf<Coord>()

    fun createNodes() {
        for (row in maze.indices) {
            for (col in maze.indices) {
                val node = Coord(col, row)
                if (maze[node] == '#') continue
                nodes.add(node)
                neighbours[node] = buildList {
                    for (possibleNeighbour in listOf(
                        node.copy(first = node.first - 1),
                        node.copy(second = node.second - 1),
                        node.copy(first = node.first + 1),
                        node.copy(second = node.second + 1)
                    )) {
                        if (possibleNeighbour.first in 0 until mazeSize && possibleNeighbour.second in 0 until mazeSize && maze[possibleNeighbour] == '.') {
                            add(possibleNeighbour)
                        }
                    }
                }.toList()
            }
        }
    }

    fun aStar(): Boolean {
        val queue = PriorityQueue()
        while (queue.isNotEmpty()) {
            val currentNode = queue.getNext()
            if (currentNode.coord == end) {
                if (currentNode.distFromStart == mazeSize * mazeSize) {
                    input[bytesToDrop - 1].println()
                    return false
                }
                currentNode.distFromStart.println()
                // currentNode.pathToHere.println()
                shortestPath = currentNode.pathToHere.toSet()
                return true
            }
            for (neighbour in neighbours[currentNode.coord]!!) {
                val newPath = currentNode.pathToHere.toMutableSet()
                newPath.add(neighbour)
                queue.updateDist(neighbour, currentNode.distFromStart + 1, newPath)
            }
        }

        return false
    }

    inner class PriorityQueue {
        private val backingList: MutableList<PQueueEntry> = nodes.map { PQueueEntry(it, if (it == start) 0 else mazeSize * mazeSize) }.toMutableList()

        operator fun MutableList<PQueueEntry>.get(node: Coord): PQueueEntry = backingList.first { it.coord == node }

        fun updateDist(coord: Coord, newDist: Int, pathToHere: Set<Coord>) {
            if (coord in backingList.map { it.coord })
                backingList[coord].apply {
                    distFromStart = newDist
                    this.pathToHere = pathToHere
                }
        }

        fun getNext(): PQueueEntry {
            backingList.sortBy { it.distFromStart + it.heuristic }
            return backingList.removeFirst()
        }

        inner class PQueueEntry(val coord: Coord, var distFromStart: Int) {
            var pathToHere = setOf(start)
            val heuristic = mazeSize - coord.first + mazeSize - coord.second
        }

        fun isNotEmpty() = backingList.isNotEmpty()
    }

}

fun main() {
    Day18_1.createNodes()
    Day18_1.aStar()

    // Obviously far better ways to do this, but this worked, so who cares?
    for (bytesToDrop in 0..if (TEST) 24 else 3449) {
        val a = Day18_2(bytesToDrop)
        a.createNodes()
        if (!a.aStar()) break
    }
}