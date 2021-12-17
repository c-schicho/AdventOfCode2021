import java.io.File

class OceanTrenchEnvironment(
    private val xMin: Int,
    private val xMax: Int,
    private val yMin: Int,
    private val yMax: Int
) {
    private var startVelocity = VALUE_RESET
    private var currentVelocity = VALUE_RESET
    private var startPosition = START_RESET
    private var currentPosition = START_RESET
    private var bestCombination = VALUE_RESET
    var nPossibleCombinations = 0

    private val isTargetReached
        get() = currentPosition.first in xMin..xMax && currentPosition.second in yMin..yMax

    private val isTargetReachable
        get() = currentPosition.first <= xMax && currentPosition.second >= yMin

    fun findVelocitiesMaximizingY(): Pair<Int, Int> {
        resetAll()
        for (xVel in 0..xMax) {
            for (yVel in -xMax..xMax) {
                startVelocity = Pair(xVel, yVel)
                currentVelocity = startVelocity
                startFromCurrentPosition()
                resetLastCalculation()
            }
        }
        return bestCombination
    }

    fun calculatedMaxReachedY(initialVelocity: Pair<Int, Int>): Int {
        var maxReachedY = 0
        resetAll()
        startVelocity = initialVelocity
        currentVelocity = startVelocity
        while (!isTargetReached) {
            calculateNextPosition()
            if (currentPosition.second > maxReachedY) {
                maxReachedY = currentPosition.second
            }
        }
        return maxReachedY
    }

    private fun startFromCurrentPosition() {
        while (isTargetReachable) {
            calculateNextPosition()
            if (isTargetReached) {
                nPossibleCombinations++
                if (startVelocity.second > bestCombination.second) {
                    bestCombination = startVelocity
                }
                return
            }
        }
    }

    private fun calculateNextPosition() {
        val currentXVelocity = currentVelocity.first
        val currentYVelocity = currentVelocity.second
        val nextX = currentPosition.first + currentXVelocity
        val nextY = currentPosition.second + currentYVelocity
        currentPosition = Pair(nextX, nextY)

        val newXVelocity = if (currentXVelocity > 0) currentXVelocity.dec() else 0
        currentVelocity = Pair(newXVelocity, currentYVelocity.dec())
    }

    private fun resetLastCalculation() {
        startVelocity = VALUE_RESET
        currentVelocity = VALUE_RESET
        startPosition = START_RESET
        currentPosition = START_RESET
    }

    private fun resetAll() {
        resetLastCalculation()
        bestCombination = VALUE_RESET
        nPossibleCombinations = 0
    }

    companion object {
        val START_RESET = Pair(0, 0)
        val VALUE_RESET = Pair(-1, -1)

        fun readFromFile(path: String = "data/day17/input1.txt"): OceanTrenchEnvironment {
            val values = File(path).readText()
                .drop(13)
                .split(", ")
                .map { it.drop(2) }
                .flatMap { it.split("..") }
                .map(String::toInt)

            return OceanTrenchEnvironment(values[0], values[1], values[2], values[3])
        }
    }
}

private fun task1(): Int {
    val environment = OceanTrenchEnvironment.readFromFile()
    val bestCombination = environment.findVelocitiesMaximizingY()
    return environment.calculatedMaxReachedY(bestCombination)
}

private fun task2(): Int {
    val environment = OceanTrenchEnvironment.readFromFile()
    environment.findVelocitiesMaximizingY()
    return environment.nPossibleCombinations
}

fun main() {
    println("### TASK 1 ###\n${task1()}")

    println("### TASK 2 ###\n${task2()}")
}
