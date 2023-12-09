import aocutils.convertions.stringToListOfType
import aocutils.println
import aocutils.readInput

fun main() {
    val day = "Day09"

    fun handleInput(input: List<String>): List<List<Long>> {
        return input.map { it.stringToListOfType(String::toLong, separator = " ") }.toList()
    }

    fun calculateNextRow(row: List<Long>): List<Long> {
        return row.zipWithNext().map { it.second - it.first }.toList()
    }

    fun calculateNextValue(row: List<Long>): Long {
        val list = mutableListOf<List<Long>>()
        list.add(row)
        var result = 0L
        while (list.last().any { it!=list.last().first() }){
            list.add(calculateNextRow(list.last()))
        }
        for (i in (list.lastIndex) downTo 0){
            result += list[i].last()
        }
        return result
    }




    fun part1(input: List<String>): Long {
        val rows: List<List<Long>> = handleInput(input)
        return rows.parallelStream()
            .map { calculateNextValue(it) }
            .reduce { acc, i -> acc + i }.get()
    }

    fun calculatePreviousValue(row: List<Long>): Long {
        val list = mutableListOf<List<Long>>()
        list.add(row)
        var result = 0L
        while (list.last().any { it!=list.last().first() }){
            list.add(calculateNextRow(list.last()))
        }
        for (i in (list.lastIndex) downTo 0){
            result = list[i].first() - result
        }
        return result
    }

    fun part2(input: List<String>): Long {
        val rows: List<List<Long>> = handleInput(input)
        return rows.parallelStream()
            .map { calculatePreviousValue(it) }
            .reduce { acc, i -> acc + i }.get()
    }

    // test if implementation meets criteria from the description, like:
    var testInput = readInput("${day}_example")
    var solution = part1(testInput)
    check( solution == 114L) {"Part1 failed with $solution"}

    val input = readInput(day)
    part1(input).println()

    testInput = readInput("${day}_example")
    solution = part2(testInput)
    check(solution == 2L) {"Part2 failed with $solution"}

    part2(input).println()
}