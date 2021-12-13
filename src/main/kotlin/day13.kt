import java.io.File

class GridAndInstructions(
    private var grid: MutableList<MutableList<Boolean>>,
    private val instructions: List<List<String>>
) {
    private var nFold = 0

    val nVisibleDots
        get() = grid.flatten().count { it }

    val isFoldPossible
        get() = nFold < instructions.size

    fun performFold() {
        if (isFoldPossible) {
            val newSize = instructions[nFold].last().toInt()
            when (instructions[nFold].first()) {
                "x" -> {
                    for (y in grid.indices) {
                        for (x in (newSize + 1) until grid.first().size) {
                            val oppositeX = 2 * newSize - x
                            grid[y][oppositeX] = grid[y][x] || grid[y][oppositeX]
                        }
                    }
                    grid = grid.map { it.subList(0, newSize) }.toMutableList()
                }
                "y" -> {
                    for (y in (newSize + 1) until grid.size) {
                        for (x in grid.first().indices) {
                            val oppositeY = 2 * newSize - y
                            grid[oppositeY][x] = grid[y][x] || grid[oppositeY][x]
                        }
                    }
                    grid = grid.subList(0, newSize)
                }
            }
            nFold++
        }
    }

    override fun toString(): String {
        val stringGrid = grid.map {
            it.map {
                when (it) {
                    false -> "$COLOR_YELLOW###$COLOR_RESET"
                    true -> "$COLOR_BLACK###$COLOR_RESET"
                }
            }
        }
        return stringGrid.map { it.reduce { acc, s -> acc + s } }.reduce { acc, s -> acc + "\n" + s }
    }

    companion object {
        const val COLOR_RESET = "\u001B[0m"
        const val COLOR_BLACK = "\u001B[30m"
        const val COLOR_YELLOW = "\u001B[33m"

        fun readGridAndInstructionsFromFile(path: String = "data/day13/input1.txt"): GridAndInstructions {
            val fileContent = readFileContent(path)
            val instructionsList = fileContent.filter { it.first().startsWith("fold") }
            val instructions = mapToInstructions(instructionsList)
            val coordinates = mapToCoordinates(fileContent - instructionsList)
            val maxX = coordinates.maxOf { it.first() }
            val maxY = coordinates.maxOf { it.last() }
            val grid = createGrid(maxX + 1, maxY + 1, coordinates)

            return GridAndInstructions(grid, instructions)
        }

        private fun readFileContent(path: String): List<List<String>> {
            return File(path).readLines().map { it.split("\n") }.filter { it.first().isNotEmpty() }
        }

        private fun mapToInstructions(instructionList: List<List<String>>): List<List<String>> {
            return instructionList.flatten()
                .map { it.split("=") }
                .map { listOf(it.first().last().toString(), it.last()) }
        }

        private fun mapToCoordinates(coordinatesList: List<List<String>>): List<List<Int>> {
            return coordinatesList.map { it.flatMap { it.split(",").map(String::toInt) } }
        }

        private fun createGrid(xSize: Int, ySize: Int, coordinates: List<List<Int>>): MutableList<MutableList<Boolean>> {
            val grid = MutableList(ySize) { MutableList(xSize) { false } }
            coordinates.forEach { grid[it.last()][it.first()] = true }
            return grid
        }
    }
}

private fun task1(): Int {
    val gridAndInstructions = GridAndInstructions.readGridAndInstructionsFromFile()
    gridAndInstructions.performFold()
    return gridAndInstructions.nVisibleDots
}

private fun task2(): String {
    val gridAndInstructions = GridAndInstructions.readGridAndInstructionsFromFile()
    while (gridAndInstructions.isFoldPossible) {
        gridAndInstructions.performFold()
    }
    return gridAndInstructions.toString()
}

fun main() {
    println("### TASK 1 ###\n${task1()}")

    println("### TASK 2 ###\n${task2()}")
}