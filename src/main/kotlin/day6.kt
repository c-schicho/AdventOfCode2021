import java.io.File

private class LanternFish(
    var daysUntilBirth: Int
) {
    var firstLifeCycle = true

    fun simulateOneDayAfter() {
        if (daysUntilBirth == 0) {
            daysUntilBirth = 6
            firstLifeCycle = false
        } else {
            daysUntilBirth--
        }
    }

    fun hasJustBornNewFish() = daysUntilBirth == 6 && !firstLifeCycle

    companion object {
        fun readFromFile(path: String): List<LanternFish> =
            File(path).readText().split(",").map { LanternFish(it.toInt()) }
    }
}

private fun task1(): Int {
    val fishPopulation = LanternFish.readFromFile("data/day6/input1.txt").toMutableList()

    for (day in 1..80) {
        fishPopulation.forEach(LanternFish::simulateOneDayAfter)
        val nNewFish = fishPopulation.count { it.hasJustBornNewFish() }
        for (newFish in 1..nNewFish) {
            fishPopulation.add(LanternFish(8))
        }
    }
    return fishPopulation.size
}

private fun task2(): Long {
    val fishList = File("data/day6/input1.txt").readText().split(",").map { it.toInt() }
    val fishPopulation = MutableList<MutableList<Long>>(9) { mutableListOf() }
    fishPopulation.forEachIndexed { idx, list -> list.add(fishList.count { it == idx }.toLong()) }

    for (day in 1..256) {
        val nNewFish = fishPopulation.first().first()
        fishPopulation.removeAt(0)
        fishPopulation.add(8, mutableListOf(nNewFish))
        fishPopulation[6][0] += nNewFish
    }
    return fishPopulation.sumOf { it.first() }
}

fun main() {
    println("### TASK 1 ###\n${task1()}")

    println("### TASK 2 ###\n${task2()}")
}