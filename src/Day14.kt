import kotlin.io.path.Path
import kotlin.io.path.readLines

fun main() {
    val input = Path("src/Day14.txt").readLines()
    val robotRegex = """p=(\d+),(\d+)\sv=(-?\d+),(-?\d+)""".toRegex()

    val robots = input.map { inputLine ->
        val (posX, posY, velX, velY) = robotRegex.find(inputLine)!!.groupValues.drop(1).map { it.toInt() }
        Robot(Pair(posX, posY), Pair(velX, velY))
    }

    robots.forEach { it.move(100) }

    var topLeft = 0
    var topRight = 0
    var bottomLeft = 0
    var bottomRight = 0

    for(y in 0 until 103) {
        for(x in 0 until 101) {
            val robotsInSpace = robots.count { it.pos == Pair(x, y) }
            when(y) {
                in 0..50 -> {
                    when(x) {
                        in 0..49 -> {
                            topLeft += robotsInSpace
                        }

                        in 51..100 -> {
                            topRight += robotsInSpace
                        }
                    }
                }

                in 52..102 -> {
                    when(x) {
                        in 0..49 -> {
                            bottomLeft += robotsInSpace
                        }

                        in 51..100 -> {
                            bottomRight += robotsInSpace
                        }
                    }
                }
            }
//            if (robotsInSpace > 0) print(robotsInSpace)
//            else print('.')
        }
//        println()
    }

    println(topLeft * topRight * bottomLeft * bottomRight)


    robots.forEach { it.move(-100) }
    var a = 0L

    while (true){
        robots.forEach { it.move(1) }
        a++
        // Easter egg only occurs if no robots overlap. I don't really understand why this is a requirement, had to
        // look for a hint on this one after staring at the outputs for ages.
        if (robots.map { it.pos }.size == robots.map { it.pos }.toSet().size) {
            for(y in 0 until 103) {
                for(x in 0 until 101) {
                    val robotsInSpace = robots.count { it.pos == Pair(x, y) }
                    if (robotsInSpace > 0) print(robotsInSpace)
                    else print('.')
                }
                println()
            }
            break
        }
    }

    a.println()

}

data class Robot(var pos: Pair<Int, Int>, val velocity: Pair<Int, Int>) {
    fun move(moves: Int) {
        pos = pos.copy(
            first = (pos.first + velocity.first * moves).mod(101),
            second = (pos.second + velocity.second * moves).mod(103)
        )
    }
}
