import aocutils.convertions.stringToListOfType
import aocutils.println
import aocutils.readInput
import kotlin.jvm.optionals.getOrDefault
import kotlin.streams.asSequence

fun main() {
    val day = "Day05"

    fun translate(item:Long,ranges:Collection<Triple<Long,Long,Long>>):Long {
        val translationRange = ranges.parallelStream()
            .filter { it.second <= item && it.second+it.third>=item }
            .findFirst().getOrDefault(Triple(0L,0L,0L))
        return ((item - translationRange.second)+translationRange.first)
    }

    fun chainTranslate(item:Long,categories:List<Collection<Triple<Long,Long,Long>>>):Long{
        var itemTranslated = item
        categories.forEach { itemTranslated=translate(itemTranslated,it) }
        return itemTranslated
    }

    fun handleInput(input: List<String>) : Pair<List<Long>,List<Collection<Triple<Long,Long,Long>>>>{
        val seeds = input[0].split(" ")
            .drop(1)
            .asSequence()
            .map { it.toLong()}
            .toList()

        var count = 0
        val categories = input.drop(2)
            .asSequence()
            .filterNot { it.isEmpty() }
            .onEach { if (it.contains(":")) count++ }
            .filterNot { it.contains(":") }
            .groupBy { count }
            .map {
                group-> group.value.asSequence()
                .map { it.stringToListOfType({str->str.toLong()}," ") }
                .map {
                    Triple(it[0],it[1],it[2]) }
                .toSet()

            }.toList()
        return Pair(seeds,categories)
    }


    fun part1(input: List<String>): Int {
        val (seeds,categories) = handleInput(input)
        return seeds.asSequence()
            .map { chainTranslate(it,categories) }
            .min().toInt()
    }

    fun intersection(a1: Long,a2: Long,b1 :Long,b2: Long): Boolean = a1<=b2 && a2>=b1

    fun splitRangeTranslation(pair: Pair<Long,Long>, ranges:Collection<Triple<Long,Long,Long>>) :List<Pair<Long,Long>>{

        val intersectedRanges = ranges.parallelStream()
            .filter {intersection(pair.first,pair.second,it.second,it.second+it.third-1) }
            .sorted { o1,o2 -> o1.second.compareTo(o2.second) }
            .asSequence().toList()

        if (intersectedRanges.isEmpty()) return listOf(pair)

        var epsilon = pair.first
        return intersectedRanges.asSequence()
            .flatMap { range->
                val list:MutableList<Pair<Long,Long>> = mutableListOf()
                if(epsilon<range.second) {
                    list.add(Pair(epsilon,range.second-1))
                    epsilon = range.second
                }

                epsilon = if (pair.second<range.second+range.third) {
                    list.add(Pair(range.first-range.second+epsilon,range.first-range.second+pair.second))
                    pair.second
                }else {
                    list.add(Pair(range.first-range.second+epsilon,range.first+range.third-1))
                    range.second+range.third
                }
                list
            }.toList()
            .toList()
    }

    fun part2(input: List<String>): Int {
        val (seeds,categories) = handleInput(input)
        var list= seeds.asSequence()
            .zipWithNext()
            .filterIndexed { index, _ -> index%2==0 }
            .map { Pair(it.first,it.first+it.second-1) }.toList()
        for (category in categories) {
            list = list
                .parallelStream()
                .flatMap { splitRangeTranslation(it,category).stream() }
                .asSequence()
                .toList()
        }
        return list.flatMap { it.toList() }.min().toInt()
    }

    // test if implementation meets criteria from the description, like:
    var testInput = readInput("${day}_example")
    var solution = part1(testInput)
    check( solution == 35) {"Part1 failed with $solution"}

    val input = readInput(day)
    part1(input).println()

    testInput = readInput("${day}_example")

    solution = part2(testInput)
    check(solution == 46) {"Part2 failed with $solution"}

    part2(input).println()
}