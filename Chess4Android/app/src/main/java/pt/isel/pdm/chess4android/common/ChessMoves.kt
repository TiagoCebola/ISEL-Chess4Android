package pt.isel.pdm.chess4android.common

import pt.isel.pdm.chess4android.common.model.Army
import pt.isel.pdm.chess4android.common.model.Piece

/**
 * Correspondes a letter to a board index
 */
fun charToIntMove(move : Char) : Int{
    return when(move) {
        'h', '1' -> 7
        'g', '2' -> 6
        'f', '3' -> 5
        'e', '4' -> 4
        'd', '5' -> 3
        'c', '6' -> 2
        'b', '7' -> 1
        'a', '8'  -> 0
        else -> move.digitToInt() - 1
    }
}

/**
 * Correspondes a letter to a Piece
 */
fun charToPiece(move : Char) : Piece {
    return when(move) {
        'Q' -> Piece.QUEEN
        'R' -> Piece.ROOK
        'B' -> Piece.BISHOP
        else -> Piece.KNIGHT
    }
}


/**
 * See if the move is outside the TILE
 */
fun checkOutOfBounds(y : Int, x : Int) : Boolean {
    if(x < 0 || y < 0 || x > 7 || y > 7) return false
    return true
}

fun knightMove(board: Array<Array<Pair<Army, Piece>?>>, move: String, pieceId: Pair<Army, Piece>) : Array<Array<Pair<Army, Piece>?>>{

    // Busca os dois ultimos caracteres que correspondem ao destino da peça
    var x = charToIntMove(move.get(move.length-2))
    var y = charToIntMove(move.get(move.length-1))
    var validator = 0

    var distinguishPlay = 'x' // Jogada que representa comer outra peça
    if(move.length == 4) distinguishPlay = move.get(1)

    if(move.length <= 4 && distinguishPlay == 'x') {

            if(checkOutOfBounds(y-2, x-1) && board[y-2][x-1]?.equals(pieceId) == true) {
                validator = 1
                board[y-2][x-1] = null
            }

            else if(checkOutOfBounds(y-2, x+1) && board[y-2][x+1]?.equals(pieceId) == true) {
                validator = 1
                board[y-2][x+1] = null
            }

            else if(checkOutOfBounds(y-1, x-2) && board[y-1][x-2]?.equals(pieceId) == true) {
                validator = 1
                board[y-1][x-2] = null
            }

            else if(checkOutOfBounds(y-1, x+2) && board[y-1][x+2]?.equals(pieceId) == true) {
                validator = 1
                board[y-1][x+2] = null
            }

            else if(checkOutOfBounds(y+1, x-2) && board[y+1][x-2]?.equals(pieceId) == true) {
                validator = 1
                board[y+1][x-2] = null
            }

            else if(checkOutOfBounds(y+1, x+2) && board[y+1][x+2]?.equals(pieceId) == true) {
                validator = 1
                board[y+1][x+2] = null
            }

            else if(checkOutOfBounds(y+2, x-1) && board[y+2][x-1]?.equals(pieceId) == true) {
                validator = 1
                board[y+2][x-1] = null
            }

            else if ( checkOutOfBounds(y+2, x+1) ) {
                validator = 1
                board[y + 2][x + 1] = null
            }
    }

    else {
        var origin = charToIntMove(move.get(1));
        var i = 0;
        while(i < 8) {
            if(board[i][origin]?.equals(pieceId) == true) {
                validator = 1
                board[i][origin] = null
            }
            i++
        }
    }

    if(validator == 1) board[y][x] = pieceId;

    return board;
}

fun towerMove(board: Array<Array<Pair<Army, Piece>?>>, move: String, pieceId: Pair<Army, Piece>) : Array<Array<Pair<Army, Piece>?>>{
    // Busca os dois ultimos caracteres que correspondem ao destino da peça
    var x = charToIntMove(move.get(move.length-2))
    var y = charToIntMove(move.get(move.length-1))
    var validator = 0
    var distinguishPlay = 'x'

    if(move.length == 4) {
        distinguishPlay = move.get(1)
    }

    if(move.length <= 4 && distinguishPlay == 'x') {
        var i = x;
        var clear = 0

        while (i < 8 && clear != 1) {
            if(board[y][i]?.equals(pieceId) == true) {
                validator = 1
                board[y][i] = null
                clear++
            }
            i++
        }

        i = x
        while (i >= 0 && clear != 1) {
            if(board[y][i]?.equals(pieceId) == true) {
                validator = 1
                board[y][i] = null
                clear++
            }
            i--
        }

        i = y
        while (i < 8 && clear != 1) {
            if(board[i][x]?.equals(pieceId) == true) {
                validator = 1
                board[i][x] = null
                clear++
            }
            i++;
        }

        i = y
        while (i >= 0 && clear != 1) {
            if(board[i][x]?.equals(pieceId) == true) {
                validator = 1
                board[i][x] = null
                clear++
            }
            i--;
        }
    }

    // Move.length >= 4
    else {
        var origin = charToIntMove(move.get(1))
        if(board[y][origin]?.equals(pieceId) == true) {
            validator = 1
            board[y][origin] = null
        }
        else {
            validator = 1
            board[origin][x] = null
        }
    }

    if(validator == 1) board[y][x] = pieceId;
    return board;
}

fun RoyalityMove(board: Array<Array<Pair<Army, Piece>?>>, move: String, pieceId: Pair<Army, Piece>) : Array<Array<Pair<Army, Piece>?>> {
    // Busca os dois ultimos caracteres que correspondem ao destino da peça
    var x = charToIntMove(move.get(move.length-2))
    var y = charToIntMove(move.get(move.length-1))

    var k = 0
    var idx = 0
    var boardNumber = 0
    board.iterator().forEach { a ->
        if(a.indexOf(pieceId) >= 0 && a.indexOf(pieceId) <= 7) {
            k = a.indexOf(pieceId)
            idx = boardNumber
        }
        boardNumber++
    }
    board[idx][k] = null
    board[y][x] = pieceId;

    return board;
}

fun bishopMove(board: Array<Array<Pair<Army, Piece>?>>, move: String, pieceId: Pair<Army, Piece>) : Array<Array<Pair<Army, Piece>?>>{
    // Busca os dois ultimos caracteres que correspondem ao destino da peça
    var x = charToIntMove(move.get(move.length-2))
    var y = charToIntMove(move.get(move.length-1))
    var validator = 0
    var i = 0;
    while (i < 8) {

        if(checkOutOfBounds(y+i, x+i) && board[y+i][x+i]?.equals(pieceId) == true) {
            validator = 1
            board[y+i][x+i] = null
        }
        else if(checkOutOfBounds(y+i, x-i) && board[y+i][x-i]?.equals(pieceId) == true) {
            validator = 1
            board[y+i][x-i] = null
        }

        else if(checkOutOfBounds(y-i, x+i) && board[y-i][x+i]?.equals(pieceId) == true) {
            validator = 1
            board[y-i][x+i] = null
        }

        else if(checkOutOfBounds(y-i, x-i) && board[y-i][x-i]?.equals(pieceId) == true) {
            validator = 1
            board[y-i][x-i] = null
        }
        i++;
    }

    if(validator == 1) board[y][x] = pieceId;
    return board;
}

fun swapMove(board: Array<Array<Pair<Army, Piece>?>>, move: String, pieceId1: Pair<Army, Piece>, pieceId2: Pair<Army, Piece>) : Array<Array<Pair<Army, Piece>?>>{

    if(move.length == 5) {
        if(pieceId1.first.equals(Army.WHITE)) {
            board[7][3] = pieceId2
            board[7][2] = pieceId1
            board[7][0] = null
            board[7][4] = null
        }

        if(pieceId1.first.equals(Army.BLACK)) {
            board[0][2] = pieceId1
            board[0][3] = pieceId2
            board[0][0] = null
            board[0][4] = null
        }
    }

    else if (move.length == 3) {
        if(pieceId1.first.equals(Army.WHITE)) {
            board[7][5] = pieceId2
            board[7][6] = pieceId1
            board[7][7] = null
            board[7][4] = null
        }

        if(pieceId1.first.equals(Army.BLACK)) {
            board[0][6] = pieceId1
            board[0][5] = pieceId2
            board[0][7] = null
            board[0][4] = null
        }

    }

    return board;
}

// TEMOS DE ASSUMIR A JOGADA QUANDO O PAWN CHEGA NO OUTRO LADO DO TABULEIRO E TROCA DE PEÇA
fun pawnMove(board: Array<Array<Pair<Army, Piece>?>>, move: String, pieceId: Pair<Army, Piece>) : Array<Array<Pair<Army, Piece>?>> {
    var x : Int
    var y : Int

    var validator = 0
    var piece = pieceId
    var checkPromotion = move.get(move.length-2)

    if(checkPromotion.equals('=')) {
        piece = Pair(pieceId.first, charToPiece(move.get(move.length-1)))
        x = charToIntMove(move.get(move.length-4))
        y = charToIntMove(move.get(move.length-3))
    }
    else {
        x = charToIntMove(move.get(move.length-2))
        y = charToIntMove(move.get(move.length-1))
    }

    if(move.length == 2) {
        if(checkOutOfBounds(y+1, x) && board[y+1][x]?.equals(pieceId) == true) {
            board[y+1][x] = null
            validator = 1
        }
        else if(checkOutOfBounds(y+2, x) && board[y+2][x]?.equals(pieceId) == true) {
            validator = 1
            board[y+2][x] = null
        }
        else if(checkOutOfBounds(y-1, x) && board[y-1][x]?.equals(pieceId) == true) {
            validator = 1
            board[y-1][x] = null
        }
        else if(checkOutOfBounds(y-2, x) && board[y-2][x]?.equals(pieceId) == true){
            validator = 1
            board[y-2][x] = null
        }
    }

    else {
        var k = charToIntMove(move.get(0))
        if(checkOutOfBounds(y+1, k) && board[y+1][k]?.equals(pieceId) == true) {
            validator = 1
            board[y+1][k] = null
        }
        else if(checkOutOfBounds(y-1, k) && board[y-1][k]?.equals(pieceId) == true) {
            validator = 1
            board[y-1][k] = null
        }

        if(checkOutOfBounds(y+2, k) && board[y+2][k]?.equals(pieceId) == true) {
            validator = 1
            board[y+2][k] = null
        }
        else if(checkOutOfBounds(y-2, k) && board[y-2][k]?.equals(pieceId) == true) {
            validator = 1
            board[y-2][k] = null
        }

    }

    if(validator == 1)   board[y][x] = piece;

    return board;
}