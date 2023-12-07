import aocutils.println
import aocutils.readInput

fun main() {


    val day = "Day07"

    abstract class Hand(val first: Char, val second: Char, val third: Char, val fourth: Char, val fifth: Char):
        Iterable<Char> {
        override fun iterator(): Iterator<Char> {
            return listOf(first, second, third, fourth, fifth).iterator()
        }

        override fun toString(): String {
            return "$first$second$third$fourth$fifth"
        }

        fun cardCountToType(cardCount: List<Int>): CamelTypes {
            return when (cardCount[0]) {
                5 -> CamelTypes.FiveOfAKind
                4 -> CamelTypes.FourOfAKind
                3 -> if(cardCount[1]==2) CamelTypes.FullHouse else CamelTypes.ThreeOfAKind
                2 -> if(cardCount[1]==2) CamelTypes.TwoPair else CamelTypes.OnePair
                1 -> CamelTypes.HighCard
                else -> throw IllegalArgumentException("No cards")
            }
        }

    }

    class HandOriginal(first:Char, second:Char, third:Char, fourth:Char, fifth:Char) : Comparable<HandOriginal>,
        Hand(first, second, third, fourth, fifth) {
        constructor(string: String): this(string[0], string[1], string[2], string[3], string[4])

        fun getHandType(handOriginal: HandOriginal): CamelTypes {
            val cardCount = handOriginal.groupBy { it }
                .map { it.value.count() }
                .sortedDescending()

            return cardCountToType(cardCount)
        }

        fun getCardValue(card: Char):Int{
            return when (card){
                'A' -> 14
                'K' -> 13
                'Q' -> 12
                'J' -> 11
                'T' -> 10
                else -> if (card in '2'..'9') card.digitToInt() else throw IllegalArgumentException("card '$card' does not exist")
            }
        }

        override fun compareTo(other: HandOriginal): Int {

            if (getHandType(this).rank != getHandType(other).rank){
                return getHandType(this).rank-getHandType(other).rank
            }

            val i1 = this.iterator()
            val i2 = other.iterator()

            while (i1.hasNext()){
                val it1 = i1.next()
                val it2 =i2.next()
                if ( it1!=it2 ){
                    return getCardValue(it1)-getCardValue(it2)
                }
            }
            return 0
        }
    }

    fun part1(input: List<String>): Int {

        return input.asSequence()
            .flatMap { it.split(' ').zipWithNext() }
            .map{ HandOriginal(it.first) to it.second.toInt()}
            .sortedWith { o1, o2 -> o1.first.compareTo(o2.first) }
            .mapIndexed { index, bid -> (index+1) * bid.second }.sum()
    }

    class HandPlus(first:Char,  second:Char,  third:Char,  fourth:Char,  fifth:Char) :
        Comparable<HandPlus>, Hand(first, second, third, fourth, fifth) {
        constructor(string: String): this(string[0], string[1], string[2], string[3], string[4])

        override fun iterator(): Iterator<Char> {
            return listOf(first, second, third, fourth, fifth).iterator()
        }

        override fun toString():String{
            return "$first$second$third$fourth$fifth"
        }

        fun getHandType(handOriginal: HandPlus): CamelTypes {
            val cardCountList = handOriginal.groupBy { it }
                .map { it.key to it.value.count() }
                .sortedByDescending { it.second }

            val jollyCount = handOriginal.count { it == 'J' }

            if (jollyCount==5) return CamelTypes.FiveOfAKind

            var cardCount = cardCountList.map { it.second }.toMutableList()

            if (cardCountList[0].first=='J'){
                cardCount.add(cardCount.size, cardCount[0])
                cardCount = cardCount.drop(1).toMutableList()
            }

            cardCount[0]+=jollyCount

            return cardCountToType(cardCount)
        }

        fun getCardValue(card: Char):Int{
            return when (card){
                'A' -> 14
                'K' -> 13
                'Q' -> 12
                'J' -> 1
                'T' -> 10
                else -> if (card in '2'..'9') card.digitToInt() else throw IllegalArgumentException("card '$card' does not exist")
            }
        }

        override fun compareTo(other: HandPlus): Int {

            if (getHandType(this).rank != getHandType(other).rank){
                return getHandType(this).rank-getHandType(other).rank
            }

            val i1 = this.iterator()
            val i2 = other.iterator()

            while (i1.hasNext()){
                val it1 = i1.next()
                val it2 =i2.next()
                if ( it1!=it2 ){
                    return getCardValue(it1)-getCardValue(it2)
                }
            }
            return 0
        }
    }

    fun part2(input: List<String>): Int {

        return input.asSequence()
            .flatMap { it.split(' ').zipWithNext() }
            .map{ HandPlus(it.first) to it.second.toInt()}
            .sortedWith { o1, o2 -> o1.first.compareTo(o2.first) }
            .mapIndexed { index, bid -> (index+1) * bid.second }.sum()
    }

    // test if implementation meets criteria from the description, like:
    var testInput = readInput("${day}_example")
    var solution = part1(testInput)
    check( solution == 6440) {"Part1 failed with $solution"}

    val input = readInput(day)
    part1(input).println()

    testInput = readInput("${day}_example")
    solution = part2(testInput)
    check(solution == 5905) {"Part2 failed with $solution"}

    part2(input).println()
}

enum class CamelTypes(val rank: Int) {
    FiveOfAKind(6),
    FourOfAKind(5),
    FullHouse(4),
    ThreeOfAKind(3),
    TwoPair(2),
    OnePair(1),
    HighCard(0)

}
