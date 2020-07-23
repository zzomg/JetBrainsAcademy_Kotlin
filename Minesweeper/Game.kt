package minesweeper

import java.util.*

class Game {
    private var field: Field = Field()
    private var firstTurn = true

    fun greet() {
        println("This is a simple MineSweeper game.\n" +
                "Enter coordinates of a cell and then type in what you want to do with the cell:\n" +
                "1. claim the cell is \"free\"\n" +
                "2. mark the cell as \"mine\"\n" +
                "For example, like this: 3 3 free, 4 5 mine\n" +
                "PS: First coordinate is a column, second is a row\n" +
                "Good luck!")
    }

    fun play() {
        val scanner = Scanner(System.`in`)

        initField()

        var playerWon = 0
        while(playerWon != -1) {
            println("Set/unset mine marks or claim a cell as free:")
            val col = scanner.nextInt() - 1
            val row = scanner.nextInt() - 1
            val mark = scanner.next()

            if (col !in 0 until field.fieldSize() || row !in 0 until field.fieldSize()) {
                println("Invalid coordinates!")
                continue
            }

            if (mark != "free" && mark != "mine") {
                println("Invalid command!")
                continue
            }

            playerWon = takeTurn(row, col, mark)
        }
    }

    private fun initField() {
        val scanner = Scanner(System.`in`)

        println("Enter desirable size of the field (a number between 3 and 100):")
        var fieldSize = scanner.nextInt()
        while (fieldSize !in 3..100) {
            println("Enter valid field size")
            fieldSize = scanner.nextInt()
        }

        this.field.initField(fieldSize, fieldSize)

        println("How many mines do you want on the field?")
        var nBombs = scanner.nextInt()
        while (nBombs !in 1 until fieldSize * fieldSize) {
            println("Enter valid number of bombs")
            nBombs = scanner.nextInt()
        }

        this.field.initBombs(nBombs)
    }

    private fun takeTurn(i: Int, j: Int, mark: String): Int {
        if (firstTurn && mark == "free") {
            firstTurn = false
            field.setBombs(i, j)
        }
        if (!field.setMarked(i, j, mark)) return -1
        field.printPlayerField()
        return if (checkPlayerWon()) 1 else 0
    }

    private fun checkPlayerWon(): Boolean {
        if (field.checkAllMinesMarked() || field.foundAllSafeCells()) {
            println("Congratulations! You found all the mines!")
            return true
        }
        return false
    }
}
