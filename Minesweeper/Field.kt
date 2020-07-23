package minesweeper

import minesweeper.fieldTypes.FieldTypes
import kotlin.random.Random

class Field {
    private lateinit var field: CharArray
    private lateinit var playerField: CharArray
    private var fieldWidth: Int = 0
    private var fieldHeight: Int = 0
    private var fieldSize: Int = 0

    private var nBombs: Int = 0

    fun initField(_height: Int, _width: Int) {
        this.fieldWidth = _width
        this.fieldHeight = _height
        this.fieldSize = this.fieldWidth * this.fieldHeight
        this.field = CharArray(this.fieldSize)
        this.playerField = CharArray(this.fieldSize)
        this.field.fill(FieldTypes.PLAIN.sym)
        this.playerField.fill(FieldTypes.PLAIN.sym)
    }

    fun fieldSize(): Int {
        return this.fieldSize
    }

    fun initBombs(_nBombs: Int) {
        this.nBombs = _nBombs
        printPlayerField()
    }

    fun setBombs(avoid_i: Int, avoid_j: Int) {
        val bombIndices = IntArray(this.nBombs)
        randomizeWithoutDuplicates(avoid_i * fieldWidth + avoid_j, bombIndices, this.fieldSize)

        for (idx in bombIndices) {
            this.field[idx] = FieldTypes.BOMB.sym
        }

        cntBombs()
    }

    private fun isLeftUpperCorner(i: Int, j: Int): Boolean = (i == fieldHeight - 1 && j == 0)
    private fun isLeftLowerCorner(i: Int, j: Int): Boolean = (i == 0 && j == 0)
    private fun isRightUpperCorner(i: Int, j: Int): Boolean = (i == fieldHeight - 1 && j == fieldWidth - 1)
    private fun isRightLowerCorner(i: Int, j: Int): Boolean = (i == 0 && j == fieldWidth - 1)
    private fun isCorner(i: Int, j: Int): Boolean = isLeftLowerCorner(i, j) || isLeftUpperCorner(i, j) || isRightUpperCorner(i, j) || isRightLowerCorner(i, j)

    private fun isLeftSide(i: Int, j: Int): Boolean = (i in 1..(fieldHeight - 2) && j == 0)
    private fun isRightSide(i: Int, j: Int): Boolean = (i in 1..(fieldHeight - 2) && j == fieldWidth - 1)
    private fun isUpperSide(i: Int, j: Int): Boolean = (i == 0 && j in 1..(fieldWidth - 2))
    private fun isLowerSide(i: Int, j: Int): Boolean = (i == fieldHeight - 1 && j in 1..(fieldWidth - 2))
    private fun isSide(i: Int, j: Int): Boolean = (isLeftSide(i, j) || isRightSide(i, j) || isUpperSide(i, j) || isLowerSide(i, j))

    private fun cntBombs() {
        fun cntBombsCorner(i: Int, j: Int): Int {
            var bombCounter = 0
            when {
                isLeftLowerCorner(i, j) -> {
                    if (FieldTypes.isBomb(field[(i + 1) * fieldWidth + j])) ++bombCounter
                    if (FieldTypes.isBomb(field[i * fieldWidth + (j + 1)])) ++bombCounter
                    if (FieldTypes.isBomb(field[(i + 1) * fieldWidth + (j + 1)])) ++bombCounter
                }
                isLeftUpperCorner(i, j) -> {
                    if (FieldTypes.isBomb(field[(i - 1) * fieldWidth + j])) ++bombCounter
                    if (FieldTypes.isBomb(field[(i - 1) * fieldWidth + (j + 1)])) ++bombCounter
                    if (FieldTypes.isBomb(field[i * fieldWidth + (j + 1)])) ++bombCounter
                }
                isRightLowerCorner(i, j) -> {
                    if (FieldTypes.isBomb(field[(i + 1) * fieldWidth + j])) ++bombCounter
                    if (FieldTypes.isBomb(field[i * fieldWidth + (j - 1)])) ++bombCounter
                    if (FieldTypes.isBomb(field[(i + 1) * fieldWidth + (j - 1)])) ++bombCounter
                }
                isRightUpperCorner(i, j) -> {
                    if (FieldTypes.isBomb(field[i * fieldWidth + (j - 1)])) ++bombCounter
                    if (FieldTypes.isBomb(field[(i - 1) * fieldWidth + j])) ++bombCounter
                    if (FieldTypes.isBomb(field[(i - 1) * fieldWidth + (j - 1)])) ++bombCounter
                }
            }
            return bombCounter
        }

        fun cntBombsSide(i: Int, j: Int): Int {
            var bombCounter = 0
            when {
                isLeftSide(i, j) -> {
                    if (FieldTypes.isBomb(field[(i + 1) * fieldWidth + j])) ++bombCounter
                    if (FieldTypes.isBomb(field[(i - 1) * fieldWidth + j])) ++bombCounter
                    if (FieldTypes.isBomb(field[(i + 1) * fieldWidth + (j + 1)])) ++bombCounter
                    if (FieldTypes.isBomb(field[i * fieldWidth + (j + 1)])) ++bombCounter
                    if (FieldTypes.isBomb(field[(i - 1) * fieldWidth + (j + 1)])) ++bombCounter
                }
                isRightSide(i, j) -> {
                    if (FieldTypes.isBomb(field[(i + 1) * fieldWidth + j])) ++bombCounter
                    if (FieldTypes.isBomb(field[(i - 1) * fieldWidth + j])) ++bombCounter
                    if (FieldTypes.isBomb(field[(i + 1) * fieldWidth + (j - 1)])) ++bombCounter
                    if (FieldTypes.isBomb(field[i * fieldWidth + (j - 1)])) ++bombCounter
                    if (FieldTypes.isBomb(field[(i - 1) * fieldWidth + (j - 1)])) ++bombCounter
                }
                isUpperSide(i, j) -> {
                    if (FieldTypes.isBomb(field[i * fieldWidth + (j - 1)])) ++bombCounter
                    if (FieldTypes.isBomb(field[i * fieldWidth + (j + 1)])) ++bombCounter
                    if (FieldTypes.isBomb(field[(i + 1) * fieldWidth + (j - 1)])) ++bombCounter
                    if (FieldTypes.isBomb(field[(i + 1) * fieldWidth + j])) ++bombCounter
                    if (FieldTypes.isBomb(field[(i + 1) * fieldWidth + (j + 1)])) ++bombCounter
                }
                isLowerSide(i, j) -> {
                    if (FieldTypes.isBomb(field[i * fieldWidth + (j - 1)])) ++bombCounter
                    if (FieldTypes.isBomb(field[i * fieldWidth + (j + 1)])) ++bombCounter
                    if (FieldTypes.isBomb(field[(i - 1) * fieldWidth + (j - 1)])) ++bombCounter
                    if (FieldTypes.isBomb(field[(i - 1) * fieldWidth + j])) ++bombCounter
                    if (FieldTypes.isBomb(field[(i - 1) * fieldWidth + (j + 1)])) ++bombCounter
                }
            }
            return bombCounter
        }

        fun cntBombsCenter(i: Int, j: Int): Int {
            var bombCounter = 0
            if (FieldTypes.isBomb(field[(i + 1) * fieldWidth + j])) ++bombCounter
            if (FieldTypes.isBomb(field[(i + 1) * fieldWidth + (j - 1)])) ++bombCounter
            if (FieldTypes.isBomb(field[(i + 1) * fieldWidth + (j + 1)])) ++bombCounter
            if (FieldTypes.isBomb(field[(i - 1) * fieldWidth + j])) ++bombCounter
            if (FieldTypes.isBomb(field[(i - 1) * fieldWidth + (j - 1)])) ++bombCounter
            if (FieldTypes.isBomb(field[(i - 1) * fieldWidth + (j + 1)])) ++bombCounter
            if (FieldTypes.isBomb(field[i * fieldWidth + (j + 1)])) ++bombCounter
            if (FieldTypes.isBomb(field[i * fieldWidth + (j - 1)])) ++bombCounter
            return bombCounter
        }

        for (i in 0 until fieldHeight) {
            for (j in 0 until fieldWidth) {
                if (!FieldTypes.isBomb(field[i * fieldWidth + j])) {
                    val nBombsAround: Int = when {
                        isCorner(i, j) -> cntBombsCorner(i, j)
                        isSide(i, j) -> cntBombsSide(i, j)
                        else -> cntBombsCenter(i, j)
                    }
                    if (nBombsAround != 0) {
                        field[i * fieldWidth + j] = (nBombsAround + '0'.toInt()).toChar()
                    }
                }
            }
        }
    }

    private fun exploreCellsAround(i: Int, j: Int) {
        if (FieldTypes.isFree(playerField[i * fieldWidth + j]) || playerField[i * fieldWidth + j].isDigit()) return
        if (field[i * fieldWidth + j].isDigit()) {
            playerField[i * fieldWidth + j] = field[i * fieldWidth + j]
            return
        }
        playerField[i * fieldWidth + j] = FieldTypes.FREE.sym
        when {
            isCorner(i, j) -> {
                when {
                    isLeftLowerCorner(i, j) -> {
                        exploreCellsAround(i + 1, j)
                        exploreCellsAround(i, j + 1)
                        exploreCellsAround(i + 1, j + 1)
                    }
                    isLeftUpperCorner(i, j) -> {
                        exploreCellsAround(i - 1, j)
                        exploreCellsAround(i - 1, j + 1)
                        exploreCellsAround(i, j + 1)
                    }
                    isRightLowerCorner(i, j) -> {
                        exploreCellsAround(i + 1, j)
                        exploreCellsAround(i, j - 1)
                        exploreCellsAround(i + 1, j - 1)
                    }
                    isRightUpperCorner(i, j) -> {
                        exploreCellsAround(i, j - 1)
                        exploreCellsAround(i - 1, j)
                        exploreCellsAround(i - 1, j - 1)
                    }
                }
            }
            isSide(i, j) -> {
                when {
                    isLeftSide(i, j) -> {
                        exploreCellsAround(i + 1, j)
                        exploreCellsAround(i - 1, j)
                        exploreCellsAround(i + 1, j + 1)
                        exploreCellsAround(i, j + 1)
                        exploreCellsAround(i - 1, j + 1)
                    }
                    isRightSide(i, j) -> {
                        exploreCellsAround(i + 1, j)
                        exploreCellsAround(i - 1, j)
                        exploreCellsAround(i + 1, j - 1)
                        exploreCellsAround(i, j - 1)
                        exploreCellsAround(i - 1, j - 1)
                    }
                    isUpperSide(i, j) -> {
                        exploreCellsAround(i, j - 1)
                        exploreCellsAround(i, j + 1)
                        exploreCellsAround(i + 1, j - 1)
                        exploreCellsAround(i + 1, j)
                        exploreCellsAround(i + 1, j + 1)
                    }
                    isLowerSide(i, j) -> {
                        exploreCellsAround(i, j - 1)
                        exploreCellsAround(i, j + 1)
                        exploreCellsAround(i - 1, j - 1)
                        exploreCellsAround(i - 1, j)
                        exploreCellsAround(i - 1, j + 1)
                    }
                }
            }
            else -> {
                exploreCellsAround(i + 1, j)
                exploreCellsAround(i + 1, j - 1)
                exploreCellsAround(i + 1, j + 1)
                exploreCellsAround(i - 1, j)
                exploreCellsAround(i - 1, j - 1)
                exploreCellsAround(i - 1, j + 1)
                exploreCellsAround(i, j + 1)
                exploreCellsAround(i, j - 1)
            }
        }
    }

    private fun checkCellFree(i: Int, j: Int): Boolean {
        when {
            FieldTypes.isBomb(field[i * fieldWidth + j]) -> {
                println("You stepped on a mine and failed!")
                return false
            }
            FieldTypes.isPlain(field[i * fieldWidth + j]) -> {
                exploreCellsAround(i, j)
            }
            else -> {
                playerField[i * fieldWidth + j] = field[i * fieldWidth + j]
            }
        }
        return true
    }

    fun setMarked(i: Int, j: Int, mark: String): Boolean {
        when (mark) {
            "free" -> {
                if (!checkCellFree(i, j)) {
                    printFinalStateField()
                    return false
                }
            }
            "mine" -> {
                if (FieldTypes.isPlain(playerField[i * fieldWidth + j])
                        || FieldTypes.isMarked(playerField[i * fieldWidth + j])) {
                    playerField[i * fieldWidth + j] = if (FieldTypes.isMarked(playerField[i * fieldWidth + j]))
                        FieldTypes.PLAIN.sym else FieldTypes.MARK.sym
                }
            }
        }
        return true
    }

    fun checkAllMinesMarked(): Boolean {
        var allMinesMarked = true
        var marks = 0
        for (i in 0 until fieldHeight) {
            for (j in 0 until fieldWidth) {
                if (FieldTypes.isMarked(playerField[i * fieldWidth + j])) {
                    ++marks
                    allMinesMarked = allMinesMarked && FieldTypes.isBomb(field[i * fieldWidth + j])
                    if (!allMinesMarked) return false
                }
            }
        }
        if (marks < nBombs) return false
        return true
    }

    fun foundAllSafeCells(): Boolean {
        var foundAllSafeCells = true
        for (i in 0 until fieldHeight) {
            for (j in 0 until fieldWidth) {
                if (FieldTypes.isPlain(playerField[i * fieldWidth + j])) {
                    foundAllSafeCells = foundAllSafeCells && FieldTypes.isBomb(field[i * fieldWidth + j])
                    if (!foundAllSafeCells) return false
                }
            }
        }
        return true
    }

    private fun printFinalStateField() {
        print(" │ ")
        for (i in 0 until fieldWidth) {
            print(i + 1)
            print(" ")
        }
        println("│")

        print("—│ ")
        for (i in 0 until fieldWidth) {
            print("—")
            print(" ")
        }
        println("│")

        for (i in 0 until fieldHeight) {
            print(i + 1)
            print("| ")
            for (j in 0 until fieldWidth) {
                if (!FieldTypes.isBomb(field[i * fieldWidth + j])) {
                    print(playerField[i * fieldWidth + j])
                    print(" ")
                } else {
                    print(FieldTypes.BOMB.sym)
                    print(" ")
                }
            }
            print("|")
            println()
        }

        print("—│ ")
        for (i in 0 until fieldWidth) {
            print("—")
            print(" ")
        }
        println("│")
    }

    fun printPlayerField() {
        print(" │ ")
        for (i in 0 until fieldWidth) {
            print(i + 1)
            print(" ")
        }
        println("│")

        print("—│ ")
        for (i in 0 until fieldWidth) {
            print("—")
            print(" ")
        }
        println("│")

        for (i in 0 until fieldHeight) {
            print(i + 1)
            print("| ")
            for (j in 0 until fieldWidth) {
                print(playerField[i * fieldWidth + j])
                print(" ")
            }
            print("|")
            println()
        }

        print("—│ ")
        for (i in 0 until fieldWidth) {
            print("—")
            print(" ")
        }
        println("│")
    }

    companion object Utils {
        private fun randomizeWithoutDuplicates(avoid: Int, array: IntArray, maxRandom: Int) {
            for (i in array.indices) {
                var randomValue = Random.nextInt(0, maxRandom)
                while (array.contains(randomValue) || randomValue == avoid) {
                    randomValue = Random.nextInt(0, maxRandom)
                }
                array[i] = randomValue
            }
        }
    }
}

