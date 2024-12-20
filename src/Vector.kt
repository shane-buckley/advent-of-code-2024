import kotlin.math.sqrt

data class Vector(val x: Int, val y: Int) {
    val magnitude = sqrt((x*x + y*y).toDouble())
    operator fun plus(vector: Vector) = Vector(x + vector.x, y + vector.y)
    operator fun minus(vector: Vector) = Vector(x - vector.x, y - vector.y)
}