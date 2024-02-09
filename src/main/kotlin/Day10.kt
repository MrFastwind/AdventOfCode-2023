import aocutils.println
import aocutils.readInput
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.terminal.Terminal
import jdk.internal.util.xml.impl.Input
import javax.naming.directory.InvalidAttributesException
import kotlin.streams.asSequence

fun main() {
    val day = "Day10"




    fun charToDirections(char: Char): Set<Direction> {
        return when (char) {
            '|' -> setOf(Direction.DOWN, Direction.UP)
            '-' -> setOf(Direction.LEFT, Direction.RIGHT)
            'L' -> setOf(Direction.UP,Direction.RIGHT)
            'J' -> setOf(Direction.UP,Direction.LEFT)
            'F' -> setOf(Direction.DOWN,Direction.RIGHT)
            '7' -> setOf(Direction.DOWN,Direction.LEFT)
            'S' -> setOf(Direction.UP,Direction.DOWN,Direction.LEFT,Direction.RIGHT)
            '.' -> emptySet()
            else -> throw InvalidAttributesException("char '$char' does not exist")
        }
    }
    
    fun checkConnection(start: Set<Direction>, direction: Direction, target: Set<Direction>): Boolean{
        return start.contains(direction) && target.contains(!direction)
    }

    fun handleInput(input: List<String>): Pair<Pair<Int, Int>, Map<Pair<Int, Int>, Char>> {
        var start = Pair(-1, -1)
        val map = input.flatMapIndexed { y, line ->
            line.mapIndexed { x, c ->
                (x to y) to c
            }.asSequence()
        }.onEach { if (it.second == 'S') {start = it.first} }
            .toMap()
        return start to map
    }
    
    fun pathFinder(map: Map<Pair<Int, Int>, Char>, start: Pair<Int, Int>, startingDirection: Direction): List<Pair<Int, Int>> {
        var direction = startingDirection
        var next = -1 to -1
        var actual = start
        val path = mutableListOf(start)
        while (next!=start){
            next = actual+direction.vector()
            if (next !in map.keys){
                break
            }
            val directions = charToDirections(map[next]!!)
            if (!checkConnection( charToDirections(map[actual]!!), direction, directions)){
                break
            }
            direction = directions.first { it != !direction }
            actual = next
            path.add(actual)

        }
        return path
    }

    fun printMaze(input:  List<String>) {
        val (start, map) = handleInput(input)
        val path = charToDirections(map[start]!!).map { pathFinder(map, start, it) }.first{it.size>2}

        val t = Terminal()
        input.forEachIndexed{y, line->
            t.println(line.mapIndexed { x,c ->
                val point = x to y
                var char = ""
                char = when (point){
                    in path -> TextColors.green(c.toString())
                    else -> TextColors.white(c.toString())
                }
                if (c=='S')char = TextColors.yellow("S")
                if (c=='.')char = TextColors.red(".")
                char
            }.fold(""){a,b->a+b}
            )
        }


    }

    fun part1(input: List<String>): Int {

        val (start, map) = handleInput(input)
        val path = charToDirections(map[start]!!).map { pathFinder(map, start, it) }.first{it.size>2}
        return (path.size/2)
    }

    fun part2(input: List<String>): Int {
        val (start, map) = handleInput(input)

        val path = charToDirections(map[start]!!).map { pathFinder(map, start, it) }.first{it.size>2}

        var (left, up) = start
        var (right, down) = start

        for (e in path){
            if (e.first<left) left = e.first
            if (e.first>right) right = e.first
            if (e.second<up) up = e.second
            if (e.second>down) down = e.second
        }

        val tiles = map.filter { !path.contains(it.key) }.map { it.key }.asSequence()

        val filteredTiles = tiles.filter{ it.first>left}
            .filter { it.first<right }
            .filter { it.second>up }
            .filter { it.second<down }.toList()




        return filteredTiles.map {tile ->
            var upCounter = 0
            var downCounter = 0
            var leftCounter = 0
            var rightCounter = 0

            for (e in path){
                if (e.first==tile.first){
                    if (map[tile]!!!='|') {
                        if (e.second<tile.second) upCounter++ else downCounter++
                    }
                }
                if(e.second==tile.second){
                    if (map[tile]!!!='-') {
                        if (e.first<tile.first) rightCounter++ else leftCounter++
                    }

                }
            }
            //if ((upCounter+downCounter+leftCounter+rightCounter)%2==1) print(tile)
            ((upCounter+downCounter+leftCounter+rightCounter)%2==1)
        }.count{it}


    }

    // test if implementation meets criteria from the description, like:
    var testInput = readInput("${day}_example")
    var solution = part1(testInput)
    check( solution == 8) {"Part1 failed with $solution"}

    val input = readInput(day)
    part1(input).println()

    //printMaze(input)

    testInput = readInput("${day}_example2")
    solution = part2(testInput)
    check(solution == 10) {"Part2 failed with $solution"}


    testInput = readInput("${day}_example3")
    solution = part2(testInput)
    check(solution == 8) {"Part2 failed with $solution"}


    part2(input).println()
}


enum class Direction {
    UP,
    RIGHT,
    DOWN,
    LEFT;

    operator fun not() = when(this){
        UP -> DOWN
        DOWN -> UP
        RIGHT -> LEFT
        LEFT -> RIGHT
        else -> throw InvalidAttributesException("Not a valid direction")
    }
}

fun Direction.vector(): Pair<Int, Int>{
    return when(this){
        Direction.UP -> 0 to -1
        Direction.RIGHT -> 1 to 0
        Direction.DOWN -> 0 to 1
        Direction.LEFT -> -1 to 0
    }
}

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int>{
    return Pair(first+other.first, second+other.second)
}

operator fun Pair<Int, Int>.unaryMinus(): Pair<Int, Int>{
    return -first to -second
}

operator fun Pair<Int, Int>.minus(other: Pair<Int, Int>): Pair<Int, Int>{
    return plus(-Pair(other.first, other.second))
}

fun Pair<Int, Int>.neighbor(direction:Direction): Pair<Int, Int>{
    return this+direction.vector()
}