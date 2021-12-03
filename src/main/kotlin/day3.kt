import java.io.File

private fun task1(): Int {
    var gamma = ""
    var epsilon = ""

    val codeList = File("data/day3/input1.txt").readLines()

    for (idx in 0 until codeList.first().length) {
        val zeroCount = codeList.count { it[idx] == '0' }

        if (zeroCount > codeList.size / 2) {
            gamma += 0
            epsilon += 1
        } else {
            gamma += 1
            epsilon += 0
        }
    }
    return gamma.toInt(2) * epsilon.toInt(2)
}

private fun task2(): Int {
    val reduceListToValue =
        { codeList: List<String>, reducer: (codes: List<String>, index: Int, zeroCount: Int, oneCount: Int) -> List<String> ->
            var idx = 0
            var codes = codeList
            while (codes.size > 1) {
                val zeroCount = codes.count { it[idx] == '0' }
                val oneCount = codes.size - zeroCount
                codes = reducer(codes, idx, zeroCount, oneCount)
                idx++
            }
            codes.first().toInt(2)
        }

    val oxygenReducer = { codes: List<String>, index: Int, zeroCount: Int, oneCount: Int ->
        val filterChar = if (zeroCount > oneCount) '0' else '1'
        codes.filter { it[index] == filterChar }
    }

    val co2Reducer = { codes: List<String>, index: Int, zeroCount: Int, oneCount: Int ->
        val filterChart = if (zeroCount < oneCount || zeroCount == oneCount) '0' else '1'
        codes.filter { it[index] == filterChart }
    }

    val codeList = File("data/day3/input1.txt").readLines()

    val oxygenValue = reduceListToValue(codeList, oxygenReducer)
    val co2Value = reduceListToValue(codeList, co2Reducer)

    return oxygenValue * co2Value
}

fun main() {
    println("### TASK 1 ###\n${task1()}")

    println("### TASK 2 ###\n${task2()}")
}