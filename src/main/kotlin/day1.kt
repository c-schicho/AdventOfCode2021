import java.io.File

private fun task1(): Int {
    return File("data/day1/input1.txt")
        .readLines()
        .map { it.toInt() }
        .windowed(2, 1)
        .count { it.first() < it.last() }
}

private fun task2(): Int {
    return File("data/day1/input1.txt")
        .readLines()
        .map { it.toInt() }
        .windowed(4, 1)
        .count { it.subList(0, 3).sum() < it.subList(1, 4).sum() }
}

fun main() {
    println("### TASK 1 ###\n${task1()}")

    println("### TASK 2 ###\n${task2()}")
}