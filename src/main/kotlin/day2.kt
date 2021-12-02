import java.io.File

private fun task1(): Int {
    return File("data/day2/input1.txt")
        .readLines()
        .map { it.split(" ") }
        .groupBy({ it.first() }, { it.last().toInt() })
        .map { it.key to it.value.sum() }
        .sortedBy { it.first.length }
        .map { it.second }
        .reduceIndexed { idx, acc, x ->
            if (idx > 1) {
                acc * x
            } else {
                x - acc
            }
        }
}

private fun task2(): Int {
    var aim = 0
    var hPos = 0
    var depth = 0

    File("data/day2/input1.txt")
        .readLines()
        .map { it.split(" ") }
        .forEach {
            val x = it.last().toInt()
            when (it.first()) {
                "up" -> aim -= x
                "down" -> aim += x
                "forward" -> {
                    hPos += x
                    depth += aim * x
                }
            }
        }

    return hPos * depth
}

fun main() {
    println("### TASK 1 ###\n${task1()}")

    println("### TASK 2 ###\n${task2()}")
}