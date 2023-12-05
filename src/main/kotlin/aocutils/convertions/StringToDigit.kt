package aocutils.convertions

fun stringToListOfInt(numbers:String, separator:String = ""): List<Int> =
    stringToListOfType(numbers, String::toInt, separator)

fun<T> stringToListOfType(numbers:String, parser:(String) -> T, separator:String = "") : List<T> =
    numbers.split(separator).asSequence()
        .filter{ it!="" }
        .map { parser(it) }
        .toList()