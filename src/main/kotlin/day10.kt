import Chunks.Companion.calculateCorruptError
import Chunks.Companion.calculateMissingError
import java.io.File
import java.util.ArrayDeque

class Chunks(
    private val chunks: List<List<Char>>
) {
    fun findFirstCorruptChars(): List<Char> {
        return chunks.map { validateChunkList(it) }
            .filter { it.isCorrupt }
            .flatMap { it.elements }
    }

    fun findMissingChars(): List<List<Char>> {
        return chunks.map { validateChunkList(it) }
            .filter { !it.isCorrupt }
            .map { it.elements }
    }

    private fun validateChunkList(chunkList: List<Char>): ValidationResult {
        val openingElements = ArrayDeque<Char>(chunkList.size)
        for (element in chunkList) {
            if (element in OPENING_TO_CLOSING.keys) {
                openingElements.push(element)
            } else {
                if (openingElements.pop() != CLOSING_TO_OPENING[element]) {
                    return ValidationResult(true, listOf(element))
                }
            }
        }
        return ValidationResult(false, openingElements.map { OPENING_TO_CLOSING[it]!! }.toList())
    }

    companion object {
        private val CORRUPT_ERROR = mapOf(
            ')' to 3,
            ']' to 57,
            '}' to 1197,
            '>' to 25137
        )

        private val MISSING_ERROR = mapOf(
            ')' to 1,
            ']' to 2,
            '}' to 3,
            '>' to 4
        )

        private val OPENING_TO_CLOSING = mapOf(
            '(' to ')',
            '[' to ']',
            '{' to '}',
            '<' to '>'
        )

        private val CLOSING_TO_OPENING = OPENING_TO_CLOSING.map { it.value to it.key }.toMap()

        fun List<Char>.calculateCorruptError() = sumOf { CORRUPT_ERROR[it]!! }

        fun List<Char>.calculateMissingError(): Long {
            var error = 0L
            forEach { error = error * 5 + MISSING_ERROR[it]!! }
            return error
        }

        fun readChunksFromFile(path: String = "data/day10/input1.txt"): Chunks {
            return Chunks(File(path).readLines().map { it.map { it } })
        }
    }

    private data class ValidationResult(val isCorrupt: Boolean, val elements: List<Char>)
}

private fun List<Long>.median() = sorted()[size / 2]

private fun task1(): Int {
    return Chunks.readChunksFromFile()
        .findFirstCorruptChars()
        .calculateCorruptError()
}

private fun task2(): Long {
    return Chunks.readChunksFromFile()
        .findMissingChars()
        .map { it.calculateMissingError() }
        .median()
}

fun main() {
    println("### TASK 1 ###\n${task1()}")

    println("### TASK 2 ###\n${task2()}")
}