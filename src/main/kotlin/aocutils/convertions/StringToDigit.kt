package aocutils.convertions

fun stringToListOfInt(numbers:String, separator:String = ""): List<Int> =
    numbers.stringToListOfType(String::toInt, separator)

fun<T> String.stringToListOfType(parser:(String) -> T, separator:String = "") : List<T> =
    this.split(separator).asSequence()
        .filter{ it!="" }
        .map { parser(it) }
        .toList()