import java.io.File

private fun task1(): Int {
    val calculateEndPosition = { idx: Int, acc: Int, x: Int ->
        if (idx > 1) {
            acc * x
        } else {
            x - acc
        }
    }

    return File("data/day2/input1.txt")
        .readLines()
        .map { it.split(" ") }
        .groupBy({ it.first() }, { it.last().toInt() })
        .map { it.key to it.value.sum() }
        .sortedBy { it.first.length }
        .map { it.second }
        .reduceIndexed(calculateEndPosition)
}

private fun task2(): Int {
    var aim = 0
    var hPos = 0
    var depth = 0

    val navigateSubmarine = { action: String, x: Int ->
        when (action) {
            "up" -> aim -= x
            "down" -> aim += x
            "forward" -> {
                hPos += x
                depth += aim * x
            }
        }
    }

    File("data/day2/input1.txt")
        .readLines()
        .map { it.split(" ") }
        .forEach { navigateSubmarine(it.first(), it.last().toInt())}

    return hPos * depth
}

fun main() {
    println("### TASK 1 ###\n${task1()}")

    println("### TASK 2 ###\n${task2()}")
}