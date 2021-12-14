import PolymerData.Companion.calculateResult
import PolymerData.Companion.readPolymerDataFromFile
import java.io.File

class PolymerData(
    private val polymerTemplate: String,
    private val insertionRules: Map<String, String>
) {
    fun insertAndCountPolymers(nTimes: Int): List<Long> {
        var polymerTemplateCount = findInitialPolymerTemplateCombinationsCount()
        for (step in 1..nTimes) {
            val currentCount = mutableMapOf<String, Long>()
            for ((key, count) in polymerTemplateCount) {
                if (insertionRules.containsKey(key)) {
                    val toInsert = insertionRules[key]
                    val leftKey = "${key.first()}$toInsert"
                    val rightKey = "$toInsert${key.last()}"
                    currentCount.increaseBy(leftKey, count)
                    currentCount.increaseBy(rightKey, count)
                }
            }
            polymerTemplateCount = currentCount
        }
        return findFinalPolymerCount(polymerTemplateCount, polymerTemplate.first(), polymerTemplate.last())
    }

    private fun findFinalPolymerCount(
        polymerCombinations: Map<String, Long>,
        firstOfTemplate: Char,
        lastOfTemplate: Char
    ): List<Long> {
        val countList = MutableList(26) { 0L }
        polymerCombinations.forEach {
            countList[it.key.first() - 'A'] += it.value
            countList[it.key.last() - 'A'] += it.value
        }
        countList[firstOfTemplate - 'A'] += 1L
        countList[lastOfTemplate - 'A'] += 1L
        return countList.filter { it != 0L }.map { it / 2 }
    }

    private fun findInitialPolymerTemplateCombinationsCount(): Map<String, Long> {
        return polymerTemplate.windowed(2).groupingBy { it }.eachCount().mapValues { it.value.toLong() }
    }

    private fun MutableMap<String, Long>.increaseBy(key: String, value: Long) {
        when (this.containsKey(key)) {
            true -> this[key] = this[key]!! + value
            false -> this[key] = value
        }
    }

    companion object {
        fun readPolymerDataFromFile(path: String = "data/day14/input1.txt"): PolymerData {
            val fileContent = File(path).readLines().flatMap { it.split("\n") }.filter(String::isNotBlank)
            val polymerTemplate = fileContent.first()
            val insertionRules = fileContent.drop(1)
                .map { it.split(" -> ") }
                .associate { it.first() to it.last() }

            return PolymerData(polymerTemplate, insertionRules)
        }

        fun calculateResult(countList: List<Long>) = countList.maxOf { it } - countList.minOf { it }
    }
}

private fun task1(): Long {
    return calculateResult(readPolymerDataFromFile().insertAndCountPolymers(10))
}

private fun task2(): Long {
    return calculateResult(readPolymerDataFromFile().insertAndCountPolymers(40))
}

fun main() {
    println("### TASK 1 ###\n${task1()}")

    println("### TASK 2 ###\n${task2()}")
}