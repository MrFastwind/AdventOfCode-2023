import AoCUtils.println
import AoCUtils.readInput
import kotlin.math.pow

fun main() {

    fun search(position:Pair<Int,Int>,charsMap:Map<Pair<Int,Int>,Char>, step:Int=0):Set<Pair<Int,Int>>{
        if(!charsMap.containsKey(position) || charsMap[position]?.isDigit() == false){
            return mutableSetOf()
        }
        val solution:MutableSet<Pair<Int,Int>> = mutableSetOf()
        solution.add(position)
        if (step == 0){
            for (i in -1..1 step 2){
                val nextPosition = Pair(position.first+i,position.second)
                if (charsMap.containsKey(nextPosition)){
                    solution.addAll(search(nextPosition,charsMap,i))
                }
            }
        }else{
            val nextPosition = Pair(position.first+step,position.second)
            if (charsMap.containsKey(nextPosition)){
                solution.addAll(search(nextPosition,charsMap,step))
            }
        }
        return solution
    }


    val day = "Day03"
    fun part1(input: List<String>): Int {
        val charMap:Map<Pair<Int,Int>,Char> =input.asSequence()
            .flatMapIndexed { y, line ->
                line.asSequence()
                    .mapIndexed { x, c -> if(c!='.') Pair(x, y) to c else null }
                    .filterNotNull()
            }.toMap()
        return charMap.asSequence()
            .filter { !it.value.isDigit() }
            .flatMap {
                symbol ->
                val foundedPositions: MutableSet<Pair<Int,Int>> = mutableSetOf()
                val solutions:MutableSet<Int> = mutableSetOf()
                for (yOffset in -1..1){
                    for (xOffset in -1..1){
                        val position = Pair(xOffset+symbol.key.first,yOffset+symbol.key.second)
                        if (!foundedPositions.contains(position)){
                            val sequence = search(position,charMap)
                            solutions.add(sequence.asSequence()
                                .sortedByDescending { it.first }
                                .mapIndexed { idx, it ->
                                    charMap[it]!!.digitToInt() * (10.0.pow(idx)) }
                                .map { it.toInt() }
                                .sum()
                            )
                            foundedPositions.addAll(sequence)
                        }
                    }
                }
                //println("tag $symbol has solutions $solutions")
                solutions.asSequence()
            }
            .sum()
    }

    fun part2(input: List<String>): Int {
        val charMap:Map<Pair<Int,Int>,Char> =input.asSequence()
            .flatMapIndexed { y, line ->
                line.asSequence()
                    .mapIndexed { x, c -> if(c!='.') Pair(x, y) to c else null }
                    .filterNotNull()
            }.toMap()
        return charMap.asSequence()
            .filter { it.value=='*' }
            .map {
                    symbol ->
                val foundedPositions: MutableSet<Pair<Int,Int>> = mutableSetOf()
                val solutions:MutableList<Int> = mutableListOf()
                var gearCount=0
                for (yOffset in -1..1){
                    for (xOffset in -1..1){
                        val position = Pair(xOffset+symbol.key.first,yOffset+symbol.key.second)
                        if (!foundedPositions.contains(position) && charMap.containsKey(position) && charMap[position]?.isDigit()==true){
                            val sequence = search(position,charMap)
                            solutions.add(sequence.asSequence()
                                .sortedByDescending { it.first }
                                .mapIndexed { idx, it ->
                                    charMap[it]!!.digitToInt() * (10.0.pow(idx)) }
                                .map { it.toInt() }
                                .sum()
                            )
                            foundedPositions.addAll(sequence)
                            gearCount++
                        }

                    }
                }
                println("$symbol has gear count $gearCount: $solutions")
                if (gearCount==2) solutions[0]*solutions[1] else 0
                //println("tag $symbol has solutions $solutions")
            }
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    var testInput = readInput("${day}_example1")
    var solution = part1(testInput)
    check( solution == 4361) {"Part1 failed with $solution"}

    val input = readInput(day)
    part1(input).println()

    testInput = readInput("${day}_example1")
    solution = part2(testInput)
    check( solution == 467835) {"Part2 failed with $solution"}

    part2(input).println()
}