import aocutils.println
import aocutils.readInput

fun main() {
    val day = "Day11"
    fun part1(input: List<String>): Int {
        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    var testInput = readInput("${day}_example")
    var solution = part1(testInput)
    check( solution == 0) {"Part1 failed with $solution"}

    val input = readInput(day)
    part1(input).println()

    testInput = readInput("${day}_example")
    solution = part2(testInput)
    check(solution == 0) {"Part2 failed with $solution"}

    part2(input).println()
}