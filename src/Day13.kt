import kotlin.io.path.Path
import kotlin.io.path.readLines

fun main() {
    val cases = Path("src/Day13.txt").readLines().filter { it.isNotEmpty() }.chunked(3)

    val movesRegex = """Button [A|B]: X\+(\d+),\sY\+(\d+)""".toRegex()
    val prizeRegex = """Prize: X=(\d+), Y=(\d+)""".toRegex()

    cases.sumOf { case ->
        val (a11, a21) = movesRegex.find(case[0])!!.groupValues.drop(1).map { it.toLong() }.let { it[0] to it[1] }
        val (a12, a22) = movesRegex.find(case[1])!!.groupValues.drop(1).map { it.toLong() }.let { it[0] to it[1] }
        val (y1, y2) = prizeRegex.find(case[2])!!.groupValues.drop(1).map { it.toLong() }.let { it[0] to it[1] }
        val det = a11 * a22 - a12 * a21
        val x1 = (a22 * y1 - a12 * y2)/det
        val x2 = (-a21 * y1 + a11 * y2)/det

        if (a11 * x1 + a12 * x2 == y1 && a21 * x1 + a22 * x2 == y2) 3 * x1 + x2 else 0
    }.println()

    cases.sumOf { case ->
        val (a11, a21) = movesRegex.find(case[0])!!.groupValues.drop(1).map { it.toLong() }.let { it[0] to it[1] }
        val (a12, a22) = movesRegex.find(case[1])!!.groupValues.drop(1).map { it.toLong() }.let { it[0] to it[1] }
        val (y1, y2) = prizeRegex.find(case[2])!!.groupValues.drop(1).map { it.toLong() + 10000000000000}.let { it[0] to it[1] }
        val det = a11 * a22 - a12 * a21
        val x1 = (a22 * y1 - a12 * y2)/det
        val x2 = (-a21 * y1 + a11 * y2)/det

        if (a11 * x1 + a12 * x2 == y1 && a21 * x1 + a22 * x2 == y2) 3 * x1 + x2 else 0
    }.println()
}
