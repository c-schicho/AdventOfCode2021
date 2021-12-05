import java.io.File
import kotlin.math.abs

private data class Vector(
    val x1: Int,
    val y1: Int,
    val x2: Int,
    val y2: Int
) {
    fun isHorizontal() = y1 == y2

    fun isVertical() = x1 == x2

    fun isDiagonal() = abs(x1 - x2) == abs(y1 - y2)

    fun getOccupiedFields(): List<List<Int>> {
        val occupiedFields = mutableListOf<List<Int>>()
        val xAxisRange = if (x1 < x2) x1..x2 else x1.downTo(x2)
        val yAxisRange = if (y1 < y2) y1..y2 else y1.downTo(y2)

        if (isHorizontal()) {
            xAxisRange.forEach { occupiedFields.add(listOf(it, y1)) }
        } else if (isVertical()) {
            yAxisRange.forEach { occupiedFields.add(listOf(x1, it)) }
        } else if (isDiagonal()) {
            xAxisRange.forEachIndexed { idx, x -> occupiedFields.add(listOf(x, yAxisRange.elementAt(idx))) }
        }
        return occupiedFields
    }

    companion object {
        fun readFromFile(path: String): List<Vector> {
            return File(path)
                .readLines()
                .map {
                    it.split("->")
                        .flatMap { it.trim().split(",") }
                        .map { it.toInt() }
                }
                .map { Vector(it[0], it[1], it[2], it[3]) }
        }
    }
}

private data class Diagram(
    val diagram: List<MutableList<Int>>
) {
    fun insertVectorInDiagram(vec: Vector) {
        val occupiedFields = vec.getOccupiedFields()
        for ((x, y) in occupiedFields) {
            diagram[y][x] += 1
        }
    }

    fun getNumberOfMultipleOccupiedFields() = diagram.flatMap { it.filter { it > 1 } }.count()

    companion object {
        fun createEmptyDiagram(size: Int) = Diagram(List(size) { MutableList(size) { 0 } })
    }
}

private fun task1(): Int {
    val vectorList = Vector.readFromFile("data/day5/input1.txt")
    val diagram = Diagram.createEmptyDiagram(1_000)

    vectorList.forEach {
        if (it.isHorizontal() || it.isVertical()) {
            diagram.insertVectorInDiagram(it)
        }
    }
    return diagram.getNumberOfMultipleOccupiedFields()
}

private fun task2(): Int {
    val vectorList = Vector.readFromFile("data/day5/input1.txt")
    val diagram = Diagram.createEmptyDiagram(1_000)

    vectorList.forEach {
        if (it.isHorizontal() || it.isVertical() || it.isDiagonal()) {
            diagram.insertVectorInDiagram(it)
        }
    }
    return diagram.getNumberOfMultipleOccupiedFields()
}

fun main() {
    println("### TASK 1 ###\n${task1()}")

    println("### TASK 2 ###\n${task2()}")
}