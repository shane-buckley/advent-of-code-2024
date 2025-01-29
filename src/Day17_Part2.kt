import Day17_Part2.calc
import java.io.File
import kotlin.math.pow
import kotlin.system.exitProcess

object Day17_Part2 {

    val input = File("src/Day17.txt").readLines()
    val instructions = input.last().filter { it.isDigit() }.toList().map { it.digitToInt().toLong() }

    fun calc(aIn: Long): List<Long> {
        var a = aIn
        var b = 0L
        var c = 0L
        var instructionPointer = 0
        val outputs = mutableListOf<Long>()

        fun combo(operand: Long): Long = when (operand) {
            0L, 1L, 2L, 3L -> operand
            4L -> a
            5L -> b
            6L -> c
            else -> throw Exception("Invalid combo operand")
        }

        fun adv() {
            val numerator = a
            val denominator = 2.0.pow(combo(instructions[instructionPointer + 1]).toDouble())
            a = (numerator / denominator).toLong()
            instructionPointer += 2
        }

        fun bxl() {
            b = b xor instructions[instructionPointer + 1]
            instructionPointer += 2
        }

        fun bst() {
            b = combo(instructions[instructionPointer + 1]).mod(8).toLong()
            instructionPointer += 2
        }

        fun jnz() {
            if (a == 0L) {
                instructionPointer += 2
                return
            }

            instructionPointer = instructions[instructionPointer + 1].toInt()
        }

        fun bxc() {
            b = b xor c
            instructionPointer += 2
        }

        fun out() {
            val outVal = combo(instructions[instructionPointer + 1]).mod(8)
            outputs += outVal.toLong()
            instructionPointer += 2
        }

        fun bdv() {
            val numerator = a
            val denominator = 2.0.pow(combo(instructions[instructionPointer + 1]).toDouble())
            b = (numerator / denominator).toLong()
            instructionPointer += 2
        }

        fun cdv() {
            val numerator = a
            val denominator = 2.0.pow(combo(instructions[instructionPointer + 1]).toDouble())
            c = (numerator / denominator).toLong()
            instructionPointer += 2
        }

        while (instructionPointer < instructions.size) {
            when (instructions[instructionPointer]) {
                0L -> adv()
                1L -> bxl()
                2L -> bst()
                3L -> jnz()
                4L -> bxc()
                5L -> out()
                6L -> bdv()
                7L -> cdv()
            }
        }

        // println(outputs.joinToString(","))
        return outputs

    }
}

fun findA(depth: Int = 0, previousOctalDigits: List<Int> = listOf()) {
    var aMin = 0L
    for (i in depth downTo 1) {
        aMin += previousOctalDigits[previousOctalDigits.size - i].toLong() * 8.0.pow(i).toLong()
    }
    for (a in 0..7) {
        val result = calc(a.toLong() + aMin)
        if(result == Day17_Part2.instructions.takeLast(depth + 1)){
            // println("a = ${a.toLong() + aMin} = ${(a.toLong() + aMin).toString(8)}o")
            if (depth == Day17_Part2.instructions.size - 1) {
                println("Minimum value for Register A found: ${a.toLong() + aMin}")
                exitProcess(0)
            }
            findA(depth + 1, previousOctalDigits.toMutableList().also { it.add(a) })
        }
    }
}

fun main() {
    findA()
}