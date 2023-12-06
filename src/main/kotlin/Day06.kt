import aocutils.convertions.stringToListOfType
import aocutils.println
import aocutils.readInput
import kotlin.math.floor

fun main() {

    val day = "Day06"

    fun handleInput(input: List<String>): List<Pair<Int, Int>> {
        val set = input.map { it.split(":")[1] }.map { it.stringToListOfType( String::toInt, " ") }
        return set[0].zip(set[1])
    }

    fun countSolutions(records: List<Pair<Long, Long>>): List<Int> {
        return records.map {record ->
            val half = floor(record.first/2.0).toInt()
            (1..half)

                .count {
                    (record.first - it) * it > record.second
                }*2 -1 +record.first.mod(2)
        }
    }

    fun part1(input: List<String>): Int {

        val records = handleInput(input)
        return countSolutions(records.map { it.first.toLong() to it.second.toLong() }).fold(1) { acc, i -> acc * i }
    }

    fun part2(input: List<String>): Int {
        val records = input
            .map { it.split(":")[1] }
            .map { it.split(" ").fold(""){acc, s -> "$acc$s"}.toLong() }.zipWithNext()

        return countSolutions(records).fold(1) { acc, i -> acc * i }
    }

    // test if implementation meets criteria from the description, like:
    var testInput = readInput("${day}_example")
    var solution = part1(testInput)
    check( solution == 288) {"Part1 failed with $solution"}

    val input = readInput(day)
    part1(input).println()

    testInput = readInput("${day}_example")
    solution = part2(testInput)
    check(solution == 71503) {"Part2 failed with $solution"}

    part2(input).println()
}