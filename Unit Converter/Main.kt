package converter

import java.util.*

fun main() {
    val scanner = Scanner(System.`in`)
    val converter = Converter()

    while(scanner.hasNextLine()) {
        println("Enter what you want to convert (or exit):")
        val input = scanner.nextLine()
        if (input == "exit") break
        if (converter.parser.parseInput(input.toLowerCase()) != -1) {
            converter.convert()
            converter.printer.prettyPrintResult()
        }
    }
}
