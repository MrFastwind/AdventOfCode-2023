package aocutils.readers

import java.io.File

object FileSequenceFactory{
    fun sequenceOfChars(file: File): Sequence<Char> {
        val reader = file.bufferedReader()
        return generateSequence {
            reader.read().let { if (it == -1) null else it.toChar()}
        }
    }

    fun sequenceOfLines(file: File): Sequence<String> {
        val reader = file.bufferedReader()
        return generateSequence {
            reader.readLine()
        }
    }

    fun sequenceOfBytes(file: File): Sequence<Byte> {
        return file.byteIterator().asSequence()}

}

fun  File.byteIterator():Iterator<Byte>{
    val reader = this.bufferedReader()
    val iterator = object: Iterator<Byte>{
        var byte: Byte = 0x00
        override fun hasNext(): Boolean {
            return this.byte != 0xFF.toByte()
        }

        override fun next(): Byte {
            if (!hasNext()){
                throw NoSuchElementException()
            }
            val temp = this.byte
            this.byte = reader.read().toByte()
            return temp
        }
    }
    iterator.next()
    return iterator
}
