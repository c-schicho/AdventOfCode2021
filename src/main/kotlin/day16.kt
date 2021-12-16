import java.io.File

data class Packet(
    val version: Int,
    val typeId: Int,
    var value: Long = -1,
    val subPackets: MutableList<Packet> = mutableListOf()
) {
    fun evaluate(): Long {
        return when(typeId) {
            0 -> subPackets.sumOf { it.evaluate() }
            1 -> subPackets.map { it.evaluate() }.reduce { acc, p -> acc * p }
            2 -> subPackets.minOf { it.evaluate() }
            3 -> subPackets.maxOf { it.evaluate() }
            4 -> value
            5 -> if(subPackets[0].evaluate() > subPackets[1].evaluate()) 1 else 0
            6 -> if(subPackets[0].evaluate() < subPackets[1].evaluate()) 1 else 0
            7 -> if(subPackets[0].evaluate() == subPackets[1].evaluate()) 1 else 0
            else -> error("Type id is invalid")
        }
    }

    fun calculateVersionIdSum(): Long {
        return when(typeId) {
            4 -> version.toLong()
            else -> subPackets.sumOf { it.calculateVersionIdSum() } + version
        }
    }
}

fun splitBinaryCodeIntoPackets(
    binaryCode: String,
    packets: MutableList<Packet>,
    packet: Packet? = null
): String {
    var binaryString = binaryCode
    if (binaryString.isBlank()) {
        return binaryCode
    }

    val drop = { n: Int -> binaryString = binaryString.drop(n) }

    val packetVersion = binaryString.takeAndDrop(3, drop).toInt(2)
    val packetTypeId = binaryString.takeAndDrop(3, drop).toInt(2)

    if (packetTypeId.isOperator()) {
        val lengthTypeId = binaryString.takeAndDrop(1, drop)
        val currentPacket = Packet(packetVersion, packetTypeId)
        packets.add(currentPacket)
        packet?.subPackets?.add(currentPacket)
        val len = if (lengthTypeId.isZero()) 15 else 11
        val lenPackets = binaryString.takeAndDrop(len, drop).toInt(2)

        if (lengthTypeId.isOne()) {
            for (i in 1..lenPackets) {
                binaryString = splitBinaryCodeIntoPackets(binaryString, packets, currentPacket)
            }
        } else {
            val endLength = binaryString.length - lenPackets
            while (endLength < binaryString.length) {
                binaryString = splitBinaryCodeIntoPackets(binaryString, packets, currentPacket)
            }
        }
    } else {
        var windowedCode = binaryString.windowed(5, 5)
        var packetCode = ""

        while (windowedCode.first().first().isNotZero()) {
            packetCode += windowedCode.first().substring(1)
            windowedCode = windowedCode.drop(1)
        }

        packetCode += windowedCode.first().substring(1)
        val lenToDrop = 5 * packetCode.length / 4
        binaryString = binaryString.drop(lenToDrop)

        packet!!.subPackets.add(Packet(packetVersion, packetTypeId, packetCode.toLong(2)))
        packets.add(Packet(packetVersion, packetTypeId, packetCode.toLong(2)))
    }
    return binaryString
}

fun readCodeFromFile(path: String = "data/day16/input1.txt") =
    File(path).readText().map(Char::hexadecimalToBinary).reduce { acc, s -> acc + s }

private fun Char.hexadecimalToBinary(): String {
    return when (this) {
        '0' -> "0000"
        '1' -> "0001"
        '2' -> "0010"
        '3' -> "0011"
        '4' -> "0100"
        '5' -> "0101"
        '6' -> "0110"
        '7' -> "0111"
        '8' -> "1000"
        '9' -> "1001"
        'A' -> "1010"
        'B' -> "1011"
        'C' -> "1100"
        'D' -> "1101"
        'E' -> "1110"
        'F' -> "1111"
        else -> error("Char is not part of the hexadecimal")
    }
}

private fun Char.isNotZero() = this != '0'
private fun String.isZero() = this == "0"
private fun String.isOne() = this == "1"
private fun Int.isOperator() = this != 4

private fun String.takeAndDrop(n: Int, action: (n: Int) -> Unit): String {
    val subString = this.take(n)
    action(n)
    return subString
}

private fun task1(): Long {
    val packets = mutableListOf<Packet>()
    splitBinaryCodeIntoPackets(readCodeFromFile(), packets)
    return packets.first().calculateVersionIdSum()
}

private fun task2(): Long {
    val packets = mutableListOf<Packet>()
    splitBinaryCodeIntoPackets(readCodeFromFile(), packets)
    return packets.first().evaluate()
}

fun main() {
    println("### TASK 1 ###\n${task1()}")

    println("### TASK 2 ###\n${task2()}")
}