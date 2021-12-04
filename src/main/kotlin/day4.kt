import java.io.File

private data class BoardElement(
    val value: Int,
) {
    var marked: Boolean = false

    fun setMarkedIfPlayed(playedNumber: Int) {
        if (value == playedNumber) marked = true
    }
}

private data class BingoBoard(
    val board: List<List<BoardElement>>
) {
    fun setAsMarkedIfContained(playedNumber: Int) = board.forEach { it.forEach { it.setMarkedIfPlayed(playedNumber) } }

    fun sumOfUnmarkedElements(): Int = board.sumOf { it.filter { !it.marked }.sumOf { it.value } }

    fun hasWon(): Boolean {
        var columnResult = false
        for (idx in board.indices) {
            columnResult = columnResult || board.all { it[idx].marked }
        }
        val rowResult = board.any { it.all { it.marked } }
        return rowResult || columnResult
    }
}

private data class GameData(
    val gameSequence: List<Int>,
    val bingoBoards: List<BingoBoard>
)

private fun setUpSequenceAndBoards(): GameData {
    val fileContent = File("data/day4/input1.txt")
        .readText()
        .split("\n")
        .toMutableList()

    val gameSequence = fileContent[0]
        .splitToSequence(",")
        .map { it.toInt() }
        .toList()

    fileContent.removeAt(0)
    val bingoBoards = fileContent
        .filterIndexed { idx, _ -> idx % 6 != 0 }
        .windowed(5, 5)
        .map {
            it.map {
                it.trim().split("  ", " ")
                    .map { it.toInt() }
                    .map { BoardElement(it) }
            }
        }
        .map { BingoBoard(it) }

    return GameData(gameSequence, bingoBoards)
}

private fun task1(): Int {
    val (gameSequence, bingoBoards) = setUpSequenceAndBoards()
    var result = 0

    for (number in gameSequence) {
        bingoBoards.forEach { it.setAsMarkedIfContained(number) }
        val winner = bingoBoards.filter { it.hasWon() }
        if (winner.isNotEmpty()) {
            result = winner.first().sumOfUnmarkedElements() * number
            break
        }
    }
    return result
}

private fun task2(): Int {
    val (gameSequence, bingoBoards) = setUpSequenceAndBoards()
    var lastWinner: BingoBoard? = null
    var result = 0

    for (number in gameSequence) {
        bingoBoards.forEach { it.setAsMarkedIfContained(number) }
        val looserList = bingoBoards.filter { !it.hasWon() }
        if (looserList.size == 1 && lastWinner == null) {
            lastWinner = looserList.first()
        } else if (looserList.isEmpty() && lastWinner != null) {
            result = lastWinner.sumOfUnmarkedElements() * number
            break
        }
    }
    return result
}

fun main() {
    println("### TASK 1 ###\n${task1()}")

    println("### TASK 2 ###\n${task2()}")
}