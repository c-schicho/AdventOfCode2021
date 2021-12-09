import java.io.File

private data class HeightMap(
    val map: List<List<HeightMapField>>
) {
    fun getLeftOrMaxHeight(x: Int, y: Int) = if (x > 0) map[y][x - 1].height else MAX_HEIGHT

    fun getRightOrMaxHeight(x: Int, y: Int) = if (x + 1 < map[y].size) map[y][x + 1].height else MAX_HEIGHT

    fun getAboveOrMaxHeight(x: Int, y: Int) = if (y > 0) map[y - 1][x].height else MAX_HEIGHT

    fun getBelowOrMaxHeight(x: Int, y: Int) = if (y + 1 < map.size) map[y + 1][x].height else MAX_HEIGHT

    fun isLocalMinimum(x: Int, y: Int): Boolean {
        val fieldHeight = map[y][x].height
        return getLeftOrMaxHeight(x, y) > fieldHeight && getRightOrMaxHeight(x, y) > fieldHeight &&
                getAboveOrMaxHeight(x, y) > fieldHeight && getBelowOrMaxHeight(x, y) > fieldHeight
    }

    fun findLowPoints(): List<HeightMapField> {
        val lowPoints = mutableListOf<HeightMapField>()
        for ((y, row) in map.withIndex()) {
            for ((x, current) in row.withIndex()) {
                if (isLocalMinimum(x, y)) {
                    lowPoints.add(current)
                }
            }
        }
        return lowPoints
    }

    fun findBasins(): List<List<HeightMapField>> {
        val basinsList = mutableListOf<MutableList<HeightMapField>>()
        for (y in map.indices) {
            for (x in map.first().indices) {
                val basinList = mutableListOf<HeightMapField>()
                findBasinsRecursive(x, y, basinList)
                if (basinList.isNotEmpty()) {
                    basinsList.add(basinList)
                }
            }
        }
        return basinsList
    }

    private fun findBasinsRecursive(x: Int, y: Int, basinList: MutableList<HeightMapField>) {
        val currentField = map[y][x]

        if (currentField.checked || currentField.height == 9) return

        basinList.add(currentField)
        currentField.checked = true
        // search above
        if (y > 0) findBasinsRecursive(x, y - 1, basinList)
        // search right
        if (x < map.first().size - 1) findBasinsRecursive(x + 1, y, basinList)
        // search below
        if (y < map.size - 1) findBasinsRecursive(x, y + 1, basinList)
        // search left
        if (x > 0) findBasinsRecursive(x - 1, y, basinList)
    }

    companion object {
        const val MAX_HEIGHT = 9

        fun readHeightMapFromFile(path: String = "data/day9/input1.txt"): HeightMap {
            return HeightMap(File(path).readLines().map { it.map { HeightMapField(it.digitToInt()) } })
        }

        fun calculateRisk(lowPoints: List<HeightMapField>): Int = lowPoints.sumOf { it.height + 1 }

        fun multiplyAreaOfThreeLargestBasins(basins: List<List<HeightMapField>>): Int {
            return basins.map { it.count() }.sortedDescending().take(3).reduce { acc, area -> acc * area }
        }
    }

    private data class HeightMapField(val height: Int) {
        var checked = false
    }
}

private fun task1(): Int {
    return HeightMap.calculateRisk(
        HeightMap.readHeightMapFromFile().findLowPoints()
    )
}

private fun task2(): Int {
    return HeightMap.multiplyAreaOfThreeLargestBasins(
        HeightMap.readHeightMapFromFile().findBasins()
    )
}

fun main() {
    println("### TASK 1 ###\n${task1()}")

    println("### TASK 2 ###\n${task2()}")
}