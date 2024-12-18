import kotlin.io.path.Path
import kotlin.io.path.readText

fun main() {

    fun List<String>.isOptimized() : Boolean {
        val end = this.subList(this.indexOf("."), this.size)
        return end.isEmpty() || end.all { it == "." }
    }

    fun inputToBlockForm(input: String): List<String> {
        var id = 0
        val blockForm = mutableListOf<String>()
        input.forEachIndexed { index, char ->
            blockForm += if (index % 2 == 0) {
                List(char.digitToInt()) {id.toString()}.also { id++ }
            } else {
                (".".repeat(char.digitToInt())).toList().map { it.toString() }
            }
        }
        return blockForm.filter { it.isNotEmpty() }
    }

    fun part1(input: String): Long {
        val blockForm = inputToBlockForm(input).toMutableList()

        for ((index, fileName) in blockForm.withIndex()) {
            if (fileName == ".") {
                val swapIndex = blockForm.indexOfLast { it != "." }
                blockForm[index] = blockForm[swapIndex]
                blockForm[swapIndex] = "."
                if (blockForm.isOptimized()) break
            }
        }

        var acc = 0L

        blockForm.filter { it != "." }.withIndex().forEach{ withIndex ->
            val (index, value) = withIndex
            acc += (value.toLong() * index.toLong())
        }

        return acc
    }

    fun part2(input: String): Long {
        var id = 0
        val blockForm = mutableListOf<Any>()
        input.forEachIndexed { index, char ->
            if (index % 2 == 0) {
                blockForm.add(List(char.digitToInt()) {id.toString()}.also { id++ })
            } else {
                blockForm.addAll((".".repeat(char.digitToInt())).toList().map { it.toString() })
            }
        }

        // Awful and slow, but works
        for(file in blockForm.filter { it != "." }.reversed()) {
            val size = (file as List<*>).size
            for (index in 0 until blockForm.indexOf(file)){
                try {
                    if (blockForm.slice(index until index + size).all { it == "." } && index < blockForm.indexOf(file)) {
                        val fileIndex = blockForm.indexOf(file)
                        blockForm.remove(file)
                        blockForm[index] = file
                        for (j in 0 until size) {
                            blockForm.add(fileIndex, ".")
                        }
                        for ((offset, i) in (index+1 until index + size).withIndex()) {
                            blockForm.removeAt(i - offset)
                        }
                        break
                    }
                } catch (_: IndexOutOfBoundsException) {}
            }

        }

        var index = 0
        val total = blockForm.sumOf { section ->
            if (section == ".") {
                index++
                0L
            }
            else {
                (section as List<*>).sumOf { num -> (num as String).toLong() * index++ }
            }
        }

        return total
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day09_test").joinToString("")
    check(part1(testInput) == 1928L)
    check(part2(testInput) == 2858L)

    // Read the input from the `src/Day01.txt` file.
    val input = Path("src/Day09.txt").readText()
    part1(input).println()
    part2(input).println()
}
