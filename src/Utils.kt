import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.*

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun main() {
    (1..3).forEach {
        val dayString = it.toString().padStart(2, '0')
        val solutionFile = Path("src/Day$dayString.kt")
        if (!solutionFile.exists()) {
            solutionFile.createFile()
            var template = Path("src/DayXX.kt").readLines()
            template = template.map { line ->
                line.replace("XX", dayString)
            }
            solutionFile.writeLines(template)
        }
        val inputFile = Path("src/Day$dayString.txt")
        if (!inputFile.exists()) inputFile.createFile()

        val testInputFile = Path("src/Day${dayString}_test.txt")
        if (!testInputFile.exists()) testInputFile.createFile()
    }
}