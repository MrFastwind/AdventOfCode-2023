import AoCUtils.println
import AoCUtils.readInput
import kotlin.math.max
import kotlin.math.pow
import kotlin.streams.asSequence

fun main() {
    val day = "Day04"

    fun String.digitsToInt() = this
        .filter { it.isDigit() }
        .map { it.digitToInt() }
        .mapIndexed { index, i -> i * (10.0.pow((this.length - index - 1).toDouble())).toInt() }
        .sum()

    fun stringToList(numbers:String,separator:String = ""): List<Int> = numbers.split(separator).asSequence()
        .map { it.digitsToInt() }
        .filter { it!=0 }
        .toList()

    fun recursiveStep(input: List<String>, index: Int): Int {
        if(index>=input.size) return 1
        val numbers= input[index].split(":")[1].split("|")
        val winingNumbers = stringToList(numbers[0])
        val myNumbers = stringToList(numbers[1])

        val count = winingNumbers.asSequence()
            .filter { myNumbers.contains(it) }
            .count()

        var result = 1

        result+=(1..count).toList().parallelStream()
            .map { recursiveStep(input, index+it) }
            .asSequence().sum()

        return result
    }

    /**
     * Solve part 2 through recursion with high cost in time
     */
    fun recursiveSolution(input: List<String>):Int{
        return input.indices.toList().parallelStream()
            .map { recursiveStep(input, it) }
            .reduce { acc, i -> acc + i }.get()


    }

    fun part1(input: List<String>): Int {
        return input.asSequence()
            .map { it.split(":")[1] }
            .map { line -> val numbers = line.split("|")
                val winingNumbers = stringToList(numbers[0]," ")
                val myNumbers = stringToList(numbers[1]," ")
                var count = winingNumbers.asSequence()
                    .filter { myNumbers.contains(it) }
                    .count()
                count--
                max(0, 2.0.pow(count.toDouble()).toInt())
            }
            .sum()
    }

    fun part2(input: List<String>): Int {
        val copies: MutableMap<Int, Int> = mutableMapOf()
        return input.asSequence()
            .map { it.split(":")[1] }
            .mapIndexed { idx, line -> val numbers = line.split("|")
                val actualCopies = copies.compute(idx) { _, v -> if(v==null) 1 else v+1 }!!
                val winingNumbers = stringToList(numbers[0]," ")
                val myNumbers = stringToList(numbers[1]," ")
                winingNumbers.asSequence()
                    .filter { myNumbers.contains(it) }
                    .mapIndexed { cidx, _ -> idx+cidx+1}
                    .forEach { copies.compute(it){ _, v -> if(v==null) actualCopies else v+actualCopies} }
                actualCopies
            }
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    var testInput = readInput("${day}_example")
    var solution = part1(testInput)
    check( solution == 13) {"Part1 failed with $solution"}

    val input = readInput(day)
    part1(input).println()

    testInput = readInput("${day}_example")
    solution = part2(testInput)
    check(solution == 30) {"Part2 failed with $solution"}

    part2(input).println()
    //{ recursiveSolution(input).println() }
}
