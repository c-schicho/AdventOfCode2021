import java.io.File

private class LanternFish(
    var daysUntilNewFishBirth: Int
) {
    var firstLifeCycle = true

    fun simulateOneDayAfter() {
        if (daysUntilNewFishBirth == 0) {
            daysUntilNewFishBirth = 6
            firstLifeCycle = false
        } else {
            daysUntilNewFishBirth--
        }
    }

    fun hasJustBornNewFish() = daysUntilNewFishBirth == 6 && !firstLifeCycle

    companion object {
        fun readFromFile(path: String): List<LanternFish> =
            File(path).readText().split(",").map { LanternFish(it.toInt()) }
    }
}

private fun task1(): Int {
    val fishPopulation = LanternFish.readFromFile("data/day6/input1.txt").toMutableList()

    for (day in 1..80) {
        fishPopulation.forEach(LanternFish::simulateOneDayAfter)
        val nNewFish = fishPopulation.count(LanternFish::hasJustBornNewFish)
        for (newFish in 1..nNewFish) {
            fishPopulation.add(LanternFish(8))
        }
    }
    return fishPopulation.size
}

private fun task2(): Long {
    val fishList = File("data/day6/input1.txt").readText().split(",").map(String::toInt)
    val fishPopulation = MutableList(9) { idx -> fishList.count { it == idx }.toLong() }

    for (day in 1..256) {
        val nNewFish = fishPopulation.first()
        fishPopulation.removeAt(0)
        fishPopulation.add(8, nNewFish)
        fishPopulation[6] += nNewFish
    }
    return fishPopulation.sum()
}

fun main() {
    println("### TASK 1 ###\n${task1()}")

    println("### TASK 2 ###\n${task2()}")
}