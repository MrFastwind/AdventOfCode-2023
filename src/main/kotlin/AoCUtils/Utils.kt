package AoCUtils

import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.div
import kotlin.io.path.readBytes
import kotlin.io.path.readLines

val resourcePath = Path("src/main/resources/")

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = getPath(name).readLines()


/**
 * Get Path from given input file name.
 */
fun getPath(name: String) = (resourcePath / "$name.txt")

/**
 * Get File from given name
 */
fun getFile(name: String) = getPath(name).toFile()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)