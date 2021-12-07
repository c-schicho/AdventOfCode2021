import java.io.File
import kotlin.math.abs

private fun readCrabPositionsFromFileSorted(path: String = "data/day7/input1.txt"): List<Int> {
    return File(path).readText().split(",").map(String::toInt).sorted()
}

private fun calculateFuelConsumptionForPosition(
    positions: List<Int>,
    positionToCalculate: Int,
    doublingCosts: Boolean
): Int {
    var fuelConsumption = positions.map { abs(it - positionToCalculate) }
    if (doublingCosts) {
        fuelConsumption = fuelConsumption.map { (it * (it + 1)) / 2 }
    }
    return fuelConsumption.sum()
}

private fun findMinFuelConsumption(positions: List<Int>, doublingCosts: Boolean = false): Int {
    var minFuelConsumption = Int.MAX_VALUE

    for (position in positions.first()..positions.last()) {
        val fuelConsumption = calculateFuelConsumptionForPosition(positions, position, doublingCosts)
        if (fuelConsumption < minFuelConsumption) {
            minFuelConsumption = fuelConsumption
        }
    }
    return minFuelConsumption
}

private fun task1(): Int {
    return findMinFuelConsumption(readCrabPositionsFromFileSorted())
}

private fun task2(): Int {
    return findMinFuelConsumption(readCrabPositionsFromFileSorted(), true)
}

fun main() {
    println("### TASK 1 ###\n${task1()}")

    println("### TASK 2 ###\n${task2()}")
}