import AoCUtils.println
import AoCUtils.readInput


fun main() {
    fun part1(input: List<String>): Int {
        return input.asSequence()
            .map { line ->
                line.asSequence()
                    .filter { it.isDigit() }
                    .toList().let {
                        it.first().digitToInt()*10+ it.last().digitToInt()
                    }
            }
            .sum()
    }

    fun part2(input: List<String>): Int {
        return input.asSequence()
            .map { convertStringWordsToInt(it) }
            .map {it.first()*10+it.last()}
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    var testInput = readInput("Day01_example1")
    check(part1(testInput) == 142)

    val input = readInput("Day01")
    part1(input).println()

    testInput = readInput("Day01_example2")
    check(part2(testInput) == 281)

    part2(input).println()
}


/**
 * The function converts a string of words to a list of digits
 * **/
fun convertStringWordsToInt(line:String):List<Int>{
    val number= listOf(
        DigitWord.ZERO,
        DigitWord.ONE,
        DigitWord.TWO,
        DigitWord.THREE,
        DigitWord.FOUR,
        DigitWord.FIVE,
        DigitWord.SIX,
        DigitWord.SEVEN,
        DigitWord.EIGHT,
        DigitWord.NINE)

    var lineConvert=line
    number.forEach { it ->
        lineConvert = lineConvert.replace(it.word, it.word.first()+it.number.toString()+it.word.last())
    }
    return lineConvert.asSequence()
        .filter { it.isDigit() }
        .map { it.digitToInt() }
        .toList()
}

enum class DigitWord(val word:String, val number: Int){
    ZERO("zero",0),
    ONE("one",1),
    TWO("two",2),
    THREE("three",3),
    FOUR("four",4),
    FIVE("five",5),
    SIX("six",6),
    SEVEN("seven",7),
    EIGHT("eight",8),
    NINE("nine",9)
}