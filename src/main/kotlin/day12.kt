import java.io.File

data class CaveNetwork(
    private val start: Cave,
    private val end: Cave
) {

    fun findNumberOfAllPaths(allowVisitingTwice: Boolean = false): Int {
        val allPaths = mutableListOf<MutableList<String>>()
        findPathsRecursive(start, mutableListOf(), allPaths, allowVisitingTwice)
        return allPaths.count()
    }

    private fun findPathsRecursive(
        startCave: Cave,
        pathList: MutableList<String>,
        allPaths: MutableList<MutableList<String>>,
        allowVisitingTwice: Boolean,
        hasVisitedOneTwice: Boolean = false
    ) {
        val currentPathList = pathList.toMutableList()
        currentPathList.add(startCave.name)

        if (startCave.name == END_NAME) {
            allPaths.add(currentPathList)
            return
        }

        startCave.visitedNTimes++

        for (cave in startCave.connections) {
            if (cave.isSmallCave && cave.visitedInCurrentStep) {
                val canVisitTwice = allowVisitingTwice && !hasVisitedOneTwice && cave.name != START_NAME
                if (canVisitTwice) {
                    findPathsRecursive(cave, currentPathList, allPaths, true, true)
                } else {
                    continue
                }
            } else {
                findPathsRecursive(cave, currentPathList, allPaths, allowVisitingTwice, hasVisitedOneTwice)
            }
        }
        startCave.visitedNTimes--
    }

    companion object {
        private const val START_NAME = "start"
        private const val END_NAME = "end"

        fun readCaveNetworkFromFile(path: String = "data/day12/input1.txt"): CaveNetwork {
            val caveMap = mutableMapOf<String, Cave>()
            val caveConnections = File(path).readLines().map { it.split("-") }
            caveConnections.flatten().distinct().forEach { caveMap[it] = Cave(it) }
            caveConnections.forEach {
                val fromCave = caveMap[it.first()]
                val toCave = caveMap[it.last()]
                fromCave?.connections?.add(toCave!!)
                toCave?.connections?.add(fromCave!!)
            }
            return CaveNetwork(caveMap[START_NAME]!!, caveMap[END_NAME]!!)
        }
    }

    class Cave(
        val name: String,
        val connections: MutableSet<Cave> = mutableSetOf(),
    ) {
        val isSmallCave = name.all(Char::isLowerCase)
        var visitedNTimes = 0
        val visitedInCurrentStep
            get() = visitedNTimes > 0
    }
}

private fun task1(): Int {
    return CaveNetwork.readCaveNetworkFromFile().findNumberOfAllPaths()
}

private fun task2(): Int {
    return CaveNetwork.readCaveNetworkFromFile().findNumberOfAllPaths(allowVisitingTwice = true)
}

fun main() {
    println("### TASK 1 ###\n${task1()}")

    println("### TASK 2 ###\n${task2()}")
}