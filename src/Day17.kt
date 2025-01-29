import java.io.File
import kotlin.math.pow

fun main() {
    val answer = StringBuilder()
    val input = File("src/Day17.txt").readLines()
    var hasOutput = false
    var a = input[0].filter { it.isDigit() }.toInt()
    var b = input[1].filter { it.isDigit() }.toInt()
    var c = input[2].filter { it.isDigit() }.toInt()
    var instructionPointer = 0
    val instructions = input.last().filter { it.isDigit() }.toList().map { it.digitToInt() }

    fun combo(operand: Int) = when(operand) {
        0, 1, 2, 3 -> operand
        4 -> a
        5 -> b
        6 -> c
        else -> throw Exception("Invalid combo operand")
    }

    fun adv() {
        val numerator = a
        val denominator = 2.0.pow(combo(instructions[instructionPointer + 1]))
        a = (numerator/denominator).toInt()
        instructionPointer += 2
    }

    fun bxl() {
        b = b xor instructions[instructionPointer + 1]
        instructionPointer += 2
    }

    fun bst() {
        b = combo(instructions[instructionPointer + 1]).mod(8)
        instructionPointer += 2
    }

    fun jnz() {
        if (a == 0) {
            instructionPointer += 2
            return
        }

        instructionPointer = instructions[instructionPointer + 1]
    }

    fun bxc() {
        b = b xor c
        instructionPointer += 2
    }

    fun out() {
        if (hasOutput) print(",")
        hasOutput = true
        val outVal = combo(instructions[instructionPointer + 1]).mod(8)
        print(outVal)
        answer.append(outVal)
        instructionPointer += 2
    }

    fun bdv() {
        val numerator = a
        val denominator = 2.0.pow(combo(instructions[instructionPointer + 1]))
        b = (numerator/denominator).toInt()
        instructionPointer += 2
    }

    fun cdv() {
        val numerator = a
        val denominator = 2.0.pow(combo(instructions[instructionPointer + 1]))
        c = (numerator/denominator).toInt()
        instructionPointer += 2
    }

    while (instructionPointer < instructions.size) {
        when (instructions[instructionPointer]) {
            0 -> adv()
            1 -> bxl()
            2 -> bst()
            3 -> jnz()
            4 -> bxc()
            5 -> out()
            6 -> bdv()
            7 -> cdv()
        }
    }

    println()
    println(answer.toString())

}