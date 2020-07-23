package minesweeper.fieldTypes

enum class FieldTypes(val sym: Char) {
    BOMB('X'), PLAIN('.'), MARK('*'), FREE('/');

    companion object {
        fun isBomb(sym: Char): Boolean = sym == BOMB.sym
        fun isPlain(sym: Char): Boolean = sym == PLAIN.sym
        fun isMarked(sym: Char): Boolean = sym == MARK.sym
        fun isFree(sym: Char): Boolean = sym == FREE.sym
    }
}