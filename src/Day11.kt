val cache = mutableMapOf<Pair<Long, Int>, Long>()

fun blink(stone: Long, blinksRemaining: Int): Long {
    // What this stone will turn into after one blink.
    val afterBlink = when {
        stone == 0L -> listOf(1L)
        stone.toString().length % 2 == 0 -> stone.toString().chunked(stone.toString().length / 2).map { it.toLong() }
        else -> listOf(stone * 2024)
    }

    return if (blinksRemaining == 1) {
        afterBlink.size.toLong()
    } else {
        afterBlink.sumOf { cache.getOrPut(Pair(it, blinksRemaining - 1)) {
            blink(it, blinksRemaining - 1)
        } }
    }
}

fun main() {
    readInput("Day11")[0].split(" ").sumOf { blink(it.toLong(), 25) }.println()
    readInput("Day11")[0].split(" ").sumOf { blink(it.toLong(), 75) }.println()
}