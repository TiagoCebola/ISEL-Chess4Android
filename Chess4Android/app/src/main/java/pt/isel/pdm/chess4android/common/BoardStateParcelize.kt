package pt.isel.pdm.chess4android.common


fun boardStateToStringArray(boardState : BoardState) : Array<String> {
    var pieces : MutableList<String> = ArrayList()
    var board = boardState.getBoard()
    board.map { pairArray ->
        pairArray.map { pair ->
            pieces.add(pair.toString())
        }
    }
    return pieces.toTypedArray()
}

private fun getArmyEntry(stringArmy: String) : model.Army {
    return when (stringArmy) {
        "BLACK" -> model.Army.BLACK
        else -> model.Army.WHITE
    }
}

private fun getPieceEntry(stringPiece: String) : model.Piece {
    return when (stringPiece) {
        " ROOK" -> model.Piece.ROOK
        " BISHOP" -> model.Piece.BISHOP
        " KNIGHT" -> model.Piece.KNIGHT
        " KING" -> model.Piece.KING
        " QUEEN" -> model.Piece.QUEEN
        else -> model.Piece.PAWN
    }
}

fun stringToPair(pairString : String) : Pair<model.Army, model.Piece>? {
    if(pairString.equals("null")) return null
    val replaced = pairString.replace("(", "")
            .replace(")", "")
            .split(",")

    return Pair(getArmyEntry(replaced[0]), getPieceEntry(replaced[1]))
}

fun stringArrayToBoardState(boardArray: Array<String>) : BoardState {
    var columnIdx = 0
    var lineIdx = 0
    var idx = 0
    var line : Array<Pair<model.Army, model.Piece>?> = arrayOfNulls(8)
    var board : MutableList<Array<Pair<model.Army, model.Piece>?>> = mutableListOf< Array<Pair<model.Army, model.Piece>?>>()

    while ( columnIdx < 8
    ) {
        while (lineIdx < 8 ) {
            line[lineIdx] = stringToPair(boardArray[idx])
            lineIdx ++
            idx ++
        }
        board.add(columnIdx, line)
        columnIdx ++
        lineIdx = 0
        line = arrayOfNulls(8)
    }

    val boardState = BoardState()
    return boardState.setBoard(board.toTypedArray())
}