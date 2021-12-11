import java.io.File

class OctopusEnvironment(
    private val energyLvlMap: List<MutableList<Octopus>>
) {
    var nStep = 0
    var nFlashes = 0
    var allHaveFlashedSimultaneously: Boolean? = null

    fun step(updateAllHaveFlashedSimultaneously: Boolean = false) {
        energyLvlMap.map { it.map { it.energyLvl++ } }
        do {
            val justFlashed = mutableListOf<Octopus>()
            for ((rowIdx, row) in energyLvlMap.withIndex()) {
                for ((colIdx, octopus) in row.withIndex()) {
                    if (octopus.energyLvl > MAX_NORMAL_ENERGY_LVL && !octopus.visitedInCurrentStep) {
                        increaseAdjacentEnergyLvl(rowIdx, colIdx)
                        octopus.visitedInCurrentStep = true
                        justFlashed.add(octopus)
                    }
                }
            }
        } while (justFlashed.isNotEmpty())
        if (updateAllHaveFlashedSimultaneously) {
            checkAllHaveFlashedSimultaneously()
        }
        resetFlashedOctopuses()
        nStep++
    }

    private fun increaseAdjacentEnergyLvl(rowIdx: Int, colIdx: Int) {
        val adjacentCoordinates = finAdjacentCoordinates(rowIdx, colIdx)
        for ((aRowIdx, aColIdx) in adjacentCoordinates) {
            if (aRowIdx in energyLvlMap.indices && aColIdx in energyLvlMap.first().indices) {
                energyLvlMap[aRowIdx][aColIdx].energyLvl++
            }
        }
    }

    private fun resetFlashedOctopuses() {
        energyLvlMap.forEach {
            it.forEach {
                if (it.energyLvl > MAX_NORMAL_ENERGY_LVL) {
                    nFlashes++
                    it.visitedInCurrentStep = false
                    it.energyLvl = MIN_NORMAL_ENERGY_LVL
                }
            }
        }
    }

    private fun finAdjacentCoordinates(rowIdx: Int, colIdx: Int): List<Pair<Int, Int>> {
        return listOf(
            Pair(rowIdx, colIdx + 1),
            Pair(rowIdx, colIdx - 1),
            Pair(rowIdx + 1, colIdx),
            Pair(rowIdx + 1, colIdx + 1),
            Pair(rowIdx + 1, colIdx - 1),
            Pair(rowIdx - 1, colIdx),
            Pair(rowIdx - 1, colIdx + 1),
            Pair(rowIdx - 1, colIdx - 1),
        )
    }

    private fun checkAllHaveFlashedSimultaneously() {
        allHaveFlashedSimultaneously = energyLvlMap.all { it.all { it.energyLvl > MAX_NORMAL_ENERGY_LVL } }
    }

    companion object {
        const val MIN_NORMAL_ENERGY_LVL = 0
        const val MAX_NORMAL_ENERGY_LVL = 9

        fun readOctopusEnvironmentFromFile(path: String = "data/day11/input1.txt"): OctopusEnvironment {
            return OctopusEnvironment(
                File(path).readLines().map { it.map { Octopus(it.digitToInt()) }.toMutableList() }
            )
        }
    }

    data class Octopus(var energyLvl: Int) {
        var visitedInCurrentStep: Boolean = false
    }
}

private fun task1(): Int {
    val octopusEnv = OctopusEnvironment.readOctopusEnvironmentFromFile()
    (1..100).forEach { octopusEnv.step() }
    return octopusEnv.nFlashes
}

private fun task2(): Int {
    val octopusEnv = OctopusEnvironment.readOctopusEnvironmentFromFile()
    while (octopusEnv.allHaveFlashedSimultaneously == null || !octopusEnv.allHaveFlashedSimultaneously!!) {
        octopusEnv.step(updateAllHaveFlashedSimultaneously = true)
    }
    return octopusEnv.nStep
}

fun main() {
    println("### TASK 1 ###\n${task1()}")

    println("### TASK 2 ###\n${task2()}")
}