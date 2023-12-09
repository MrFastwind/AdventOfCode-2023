import aocutils.println
import aocutils.readInput
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.sync.Semaphore
import java.util.stream.Collectors

@OptIn(DelicateCoroutinesApi::class)
fun main() {
    val day = "Day08"

    fun handleInput(input: List<String>): Pair<List<Char>, Map<String, Pair<String, String>>> {

        val steps = input[0].toList()
        val nodes = input.asSequence()
            .drop(2)
            .map { line ->
                line.split("=")[0]
                    .replace(" ", "") to line
                        .split("=")[1]
                    .replace(" ", "")
                    .replace("(", "")
                    .replace(")", "")
                    .split(",")
                    .zipWithNext()[0]
            }.toMap()

        return Pair(steps,nodes)
    }


    fun step(pathes: Pair<String, String>, choice: Char): String {
        return when (choice){
            'R' -> pathes.second
            'L' -> pathes.first
            else -> throw IllegalArgumentException("choice '$choice' does not exist")
        }
    }

    class CircularIterator<T>(val list: List<T>) : Iterator<T> {
        private var index = 0
        override fun hasNext(): Boolean {
            return true
        }

        override fun next(): T {
            val result = list[index]
            index = if (index + 1 < list.size) (index + 1) else 0
            return result
        }
    }

    fun part1(input: List<String>): Int {
        val ( steps,  nodes) = handleInput(input)
        val iterator = CircularIterator(steps)
        var selected = "AAA"
        var count = 0
            while (selected != "ZZZ"){
            selected = step(nodes[selected]!!, iterator.next())

            count++
        }
        return count
    }

    fun handleInputPart2(input: List<String>): Triple<List<Char>, Map<String, Pair<String, String>>, MutableList<String>> {

        val steps = input[0].toList()
        val nodes: HashMap<String, Pair<String, String>> = hashMapOf()
        input.asSequence()
            .drop(2)
            .map { line ->
                line.split("=")[0]
                    .replace(" ", "") to line
                    .split("=")[1]
                    .replace(" ", "")
                    .replace("(", "")
                    .replace(")", "")
                    .split(",")
                    .zipWithNext()[0]
            }.forEach { nodes[it.first] = it.second }
        val starting = nodes.keys
            .filter { it.endsWith("A") }.toMutableList()
        return Triple(steps,nodes, starting)
    }

    fun part2(input: List<String>): Long {
        val ( steps,  nodes, starters) = handleInputPart2(input)
        var selected:MutableList<String> = starters
        val iterator = CircularIterator(steps)
        var count = 0L

        while (selected.any{ it[2]!='Z'} ){
            val direction = iterator.next()

            for (idx in selected.indices){
                selected[idx]=step(nodes[selected[idx]]!!, direction)
            }
            count++
        }

        return count
    }

    // test if implementation meets criteria from the description, like:
    var testInput = readInput("${day}_example")
    val solution = part1(testInput)
    check( solution == 6) {"Part1 failed with $solution"}

    val input = readInput(day)
    part1(input).println()

    testInput = readInput("${day}_example2")
    val solution2 = part2(testInput)
    check(solution2 == 6L) {"Part2 failed with $solution"}

    part2(input).println()
}