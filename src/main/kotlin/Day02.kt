import AoCUtils.println
import AoCUtils.readInput

fun main() {
    val day = "Day02"

    fun part1(input: List<String>): Int {
        val maxCubes = mapOf(Pair("red",12), Pair("green",13), Pair("blue",14))
        return input.asSequence()
            .map { line:String ->
                val set:List<String> = line.split(":")
                val game:Int = set[0].split(" ")
                    .last()
                    .toInt()
                val sectors = set[1].split(";")
                if ( sectors.asSequence()
                    .map { sector -> sector.split(",").asSequence()
                        .map {
                            block -> block.removePrefix(" ")
                            Pair(block.split(" ")[2], block.split(" ")[1])
                        }
                        .all { pair -> maxCubes[pair.first]!! >=pair.second.toInt() }
                    }.all { it }) game else 0

            }.sum()
    }

    fun part2(input: List<String>): Int {

        return input.asSequence()
            .map { line:String ->
                val set:List<String> = line.split(":")
                val sectors = set[1].split(";")
                val map = sectors.asSequence()
                        .flatMap { sector -> sector.split(",").asSequence()
                            .map {
                                    block -> block.removePrefix(" ")
                                Pair(block.split(" ")[2], block.split(" ")[1].toInt())
                            }
                        }.sortedBy { it.second }
                        .toMap()
                map.values.asSequence().reduce { acc, i -> acc * i }
            }.sum()
    }

    // test if implementation meets criteria from the description, like:
    var testInput = readInput("${day}_example1")
    check(part1(testInput) == 8)

    val input = readInput(day)
    part1(input).println()

    testInput = readInput("${day}_example1")
    check(part2(testInput) == 2286)

    part2(input).println()
}