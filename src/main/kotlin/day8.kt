import java.io.File
import java.util.*

private data class ObservedPatternsAndActiveSegments(
    val observedPatterns: List<SortedSet<Char>>,
    val activeSegments: List<SortedSet<Char>>
)

private fun readOnlyActiveSegmentsFromFile(path: String = "data/day8/input1.txt"): List<String> {
    return File(path).readLines().map { it.split("|")[1].trim() }.flatMap { it.split(" ") }
}

private fun readObservedPatternsAndActiveSegmentsFromFile(
    path: String = "data/day8/input1.txt"
): List<ObservedPatternsAndActiveSegments> {
    return File(path)
        .readLines()
        .map {
            ObservedPatternsAndActiveSegments(
                it.split("|")[0].trim().split(" ").map(String::toSortedSet),
                it.split("|")[1].trim().split(" ").map(String::toSortedSet)
            )
        }
}

private fun findSegmentPatterns(signals: List<SortedSet<Char>>): List<SortedSet<Char>> {
    val one = signals.first { it.size == 2 }
    val seven = signals.first { it.size == 3 }
    val four = signals.first { it.size == 4 }
    val eight = signals.first { it.size == 7 }

    val sixNineZero = signals.filter { it.size == 6 }
    val nine = sixNineZero.first { it.containsAll(four) }
    val six = sixNineZero.first { !it.containsAll(one) }
    val zero = sixNineZero.first { it != six && it != nine }

    val twoThreeFive = signals.filter { it.size == 5 }
    val five = twoThreeFive.first { six.containsAll(it) }
    val three = twoThreeFive.first { it.containsAll(one) }
    val two = twoThreeFive.first { it != five && it != three }

    return listOf(zero, one, two, three, four, five, six, seven, eight, nine)
}

private fun List<SortedSet<Char>>.toInt(signalPatterns: List<SortedSet<Char>>): Int {
    var numberString = ""
    this.forEach { numberString += signalPatterns.indexOf(it) }
    return numberString.toInt()
}

private fun task1(): Int {
    return readOnlyActiveSegmentsFromFile().count { it.length in 2..4 || it.length == 7 }
}

private fun task2(): Int {
    return readObservedPatternsAndActiveSegmentsFromFile()
        .sumOf { it.activeSegments.toInt(findSegmentPatterns(it.observedPatterns)) }
}

fun main() {
    println("### TASK 1 ###\n${task1()}")

    println("### TASK 2 ###\n${task2()}")
}