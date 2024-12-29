fun main() {
    val map = readInput("Day12").map { it.toList() }
    val mutableMap = map.map { it.toMutableList() }
    val regions = buildList {
        while (!mutableMap.flatten().all { it == '.' }) {
            val startingRow = mutableMap.indexOfFirst { row -> row.any { it != '.' } }
            val startingColumn = mutableMap[startingRow].indexOfFirst { it != '.' }
            val startingVector = Vector(startingColumn, startingRow)
            val regionChar = mutableMap[startingVector]
            val region = Region(regionChar)
            depthFirstSearch(mutableMap, startingVector, regionChar, region)
            add(region)
        }
    }

    println("Part 1: ${regions.sumOf { it.area * it.perimeter }}, Part 2: ${regions.sumOf { it.area * it.numberOfSides }}")
}


operator fun List<List<Char>>.get(vector: Vector): Char {
    return this[vector.y][vector.x]
}

operator fun List<MutableList<Char>>.set(vector: Vector, newChar: Char) {
    this[vector.y][vector.x] = newChar
}

fun depthFirstSearch(map: List<MutableList<Char>>, startingVector: Vector, matchChar: Char, region: Region) {
    if (startingVector.y !in map.indices || startingVector.x !in map[0].indices) return
    if (map[startingVector] != matchChar) return
    region.containedCoordinates.add(startingVector)
    map[startingVector] = '.'
    depthFirstSearch(map, startingVector.copy(y = startingVector.y - 1), matchChar, region)
    depthFirstSearch(map, startingVector.copy(x = startingVector.x + 1), matchChar, region)
    depthFirstSearch(map, startingVector.copy(y = startingVector.y + 1), matchChar, region)
    depthFirstSearch(map, startingVector.copy(x = startingVector.x - 1), matchChar, region)
}

class Region(private val regionChar: Char) {
    val containedCoordinates = mutableSetOf<Vector>()
    val area
        get() = containedCoordinates.size

    val perimeter: Int
        get() = containedCoordinates.sumOf { point ->
            val conditions = listOf(
                (point.copy(y = point.y - 1) in containedCoordinates),
                (point.copy(x = point.x + 1) in containedCoordinates),
                (point.copy(y = point.y + 1) in containedCoordinates),
                (point.copy(x = point.x - 1) in containedCoordinates)
            )

            conditions.count { !it }

        }

    // Number of sides = number of corners
    val numberOfSides: Int
        get() =
            containedCoordinates.sumOf {
                var edges = 0

                // External corners: for each pair of orthogonal directions, if both directions don't have neighbours,
                // this point is a corner facing that direction.
                if(it.copy(y = it.y - 1) !in containedCoordinates && it.copy(x = it.x - 1) !in containedCoordinates) edges++
                if(it.copy(y = it.y - 1) !in containedCoordinates && it.copy(x = it.x + 1) !in containedCoordinates) edges++
                if(it.copy(y = it.y + 1) !in containedCoordinates && it.copy(x = it.x - 1) !in containedCoordinates) edges++
                if(it.copy(y = it.y + 1) !in containedCoordinates && it.copy(x = it.x + 1) !in containedCoordinates) edges++

                // Internal corners: If a point has orthogonal neighbours, but not a neighbour in their combined direction
                // i.e. a north neighbour and an east neighbour but no northeast neighbour.
                if (it.copy(y = it.y - 1) in containedCoordinates && it.copy(x = it.x - 1) in containedCoordinates && it.copy(x = it.x - 1, y = it.y - 1) !in containedCoordinates) edges++
                if (it.copy(y = it.y + 1) in containedCoordinates && it.copy(x = it.x - 1) in containedCoordinates && it.copy(x = it.x - 1, y = it.y + 1) !in containedCoordinates) edges++
                if (it.copy(y = it.y - 1) in containedCoordinates && it.copy(x = it.x + 1) in containedCoordinates && it.copy(x = it.x + 1, y = it.y - 1) !in containedCoordinates) edges++
                if (it.copy(y = it.y + 1) in containedCoordinates && it.copy(x = it.x + 1) in containedCoordinates && it.copy(x = it.x + 1, y = it.y + 1) !in containedCoordinates) edges++

                edges
            }

    override fun toString(): String {
        return "$regionChar: $area, $perimeter, $numberOfSides"
    }

}