import java.io.File
import java.util.*

fun calculateMinRisk(grid: List<List<Int>>): Int {
    val verticesPQ = PriorityQueue<Pair<Pair<Int, Int>, Int>> { v1, v2 -> v1.second - v2.second }
    val cumulatedRisks = List(grid.size) { MutableList(grid.first().size) { Int.MAX_VALUE } }
    val visitedVertices = List(grid.size) { MutableList(grid.first().size) { false } }
    val verticesInQueue = List(grid.size) { MutableList(grid.first().size) { false } }

    verticesPQ.add(Pair(Pair(0, 0), 0))
    cumulatedRisks[0][0] = 0

    while (verticesPQ.isNotEmpty()) {
        val currCoordinates = verticesPQ.poll().first
        val (currX, currY) = currCoordinates
        visitedVertices[currX][currY] = true
        for ((nX, nY) in currCoordinates.neighbours()) {
            if (0 <= nX && nX < grid.size && 0 <= nY && nY < grid.first().size) {
                val currRisk = cumulatedRisks[currX][currY]
                val correctedCurrRisk = if (currRisk == Int.MAX_VALUE) 0 else currRisk
                val risk = correctedCurrRisk + grid[nX][nY]
                if (risk < cumulatedRisks[nX][nY]) {
                    cumulatedRisks[nX][nY] = risk
                }
                if (!visitedVertices[nX][nY] && !verticesInQueue[nX][nY]) {
                    verticesPQ.add(Pair(Pair(nX, nY), risk))
                    verticesInQueue[nX][nY] = true
                }
            }
        }
    }
    return cumulatedRisks.last().last()
}

fun readGridFromFile(path: String = "data/day15/input1.txt") = File(path).readLines().map { it.map { it.digitToInt() } }

private fun Pair<Int, Int>.neighbours() = listOf(
        Pair(first - 1, second),
        Pair(first + 1, second),
        Pair(first, second + 1),
        Pair(first, second - 1)
    )

private fun List<Int>.increaseListBy(value: Int) = map {
        val increasedValue = it + value
        if (increasedValue > 9) increasedValue - 9 else increasedValue
    }

private fun List<List<Int>>.increaseGridBy(value: Int) = map { it.increaseListBy(value) }

private fun List<List<Int>>.transformToWholeGrid(): List<List<Int>> {
    val wholeGrid = mutableListOf<List<Int>>()
    val firstRow = map {
        val row = it.toMutableList()
        for (i in 1..4) {
            row += it.increaseListBy(i)
        }
        row
    }

    (0..4).forEach { firstRow.increaseGridBy(it).forEach { wholeGrid.add(it) } }

    return wholeGrid
}

private fun task1(): Int {
    return calculateMinRisk(readGridFromFile())
}

private fun task2(): Int {
    return calculateMinRisk(readGridFromFile().transformToWholeGrid())
}

fun main() {
    println("### TASK 1 ###\n${task1()}")

    println("### TASK 2 ###\n${task2()}")
}