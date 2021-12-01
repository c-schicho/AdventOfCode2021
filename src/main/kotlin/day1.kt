import java.io.File

fun task1() {
    print(
        File("day1/input1.txt")
            .readLines()
            .map { it.toInt() }
            .windowed(2, 1)
            .count { it.first() < it.last() }
    )
}

fun task2() {
    print(
        File("day1/input1.txt")
            .readLines()
            .map { it.toInt() }
            .windowed(4, 1)
            .count { it.subList(0, 3).sum() < it.subList(1, 4).sum() }
    )
}

fun main() {
    println("### TASK 1 ###")
    task1()

    println("### TASK 2 ###")
    task2()
}