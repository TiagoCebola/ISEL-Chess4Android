package pt.isel.pdm.chess4android.common

import kotlin.math.abs

    // Variable that checks if the King hasn't moved
    private var ROQUE_REQUIREMENT = true

    /**
     * Generates a random number that corresponds to a piece to switch a Pawn to another Piece
     */
    private fun randPiece() : model.Piece {
        val piece = (0..39).random()
        return when(piece) {
            0, 4, 16, 2, 37, 15, 13, 18, 1, 35 -> model.Piece.ROOK
            3, 24, 26, 32, 19, 21, 14, 9, 33, 39  -> model.Piece.KNIGHT
            36, 31, 20, 28, 25, 11, 10, 6, 34, 7   -> model.Piece.BISHOP
            else -> model.Piece.QUEEN
        }
    }

    /**
     * Makes the movements for the KNIGHT piece
     */
    fun knightMoveLocal(board: Array<Array<Pair<model.Army, model.Piece>?>>, move: String, pieceId: Pair<model.Army, model.Piece>) : Array<Array<Pair<model.Army, model.Piece>?>>{

        // Initial position of the piece
        var initialY = move.get(1).digitToInt()
        var initialX = move.get(2).digitToInt()

        // Final position for the piece
        var destinyY = move.get(3).digitToInt()
        var destinyX = move.get(4).digitToInt()

        // Checks if the sum of the modules between the final coordinates and initial coordinates corresponds to the horse move L = (3 squares)
        if( (abs(destinyX-initialX) + abs(destinyY-initialY)) == 3 ){
            // Removes the piece from the previous position
            board[initialX][initialY] = null
            // Puts the piece in the new position
            board[destinyX][destinyY] = pieceId
        }

        return board;
    }

    /**
     * Makes the movements for the ROOK piece
     */
    fun towerMoveLocal(board: Array<Array<Pair<model.Army, model.Piece>?>>, move: String, pieceId: Pair<model.Army, model.Piece>) : Array<Array<Pair<model.Army, model.Piece>?>>{

        // Initial position of the piece
        var initialY = move.get(1).digitToInt()
        var initialX = move.get(2).digitToInt()

        // Final position for the piece
        var destinyY = move.get(3).digitToInt()
        var destinyX = move.get(4).digitToInt()

        // Flag that indicates if there is any piece that may interrupt the movement
        var validator = 0

        // Coordinate used to move in the tile and interrupt movement if we are in the same tile were we began
        var position : Int

        // Checks if we are in the same row
        if(initialX == destinyX){
            if(initialY < destinyY) {
                position = initialY + 1 // Avoid starting in initial position
                while(position < destinyY && validator == 0){
                    if(board[destinyX][position] != null) validator = 1 // Checks if there is any piece on the way
                    position++
                }
            }
            else {
                position = initialY - 1
                while (position > destinyY && validator == 0) {
                    if (board[destinyX][position] != null) validator = 1
                    position--
                }
            }

        }

        // Checks if we are in the same column
        else if(initialY == destinyY){
            if(initialX < destinyX){
                position = initialX + 1
                while(position < destinyX && validator == 0){
                    if(board[position][destinyY] != null) validator = 1
                    position++
                }
            }
            else {
                position = initialX - 1
                while (position > destinyX && validator == 0) {
                    if (board[position][destinyY] != null) validator = 1
                    position--
                }
            }

        }

        // If we aren't in the same row or column it's an invalid move
        else validator = 1

        // If there isn't any piece on the way and it was a valid move,
        // remove the piece from the previous location and put it on the destiny
        if(validator == 0){
            board[initialX][initialY] = null
            board[destinyX][destinyY] = pieceId
        }

        return board;
    }

    /**
     * Makes the movements for the QUEEN piece
     */
    fun queenMoveLocal(board: Array<Array<Pair<model.Army, model.Piece>?>>, move: String, pieceId: Pair<model.Army, model.Piece>) : Array<Array<Pair<model.Army, model.Piece>?>> {

        // Initial position of the piece
        var initialY = move.get(1).digitToInt()
        var initialX = move.get(2).digitToInt()

        // Final position for the piece
        var destinyY = move.get(3).digitToInt()
        var destinyX = move.get(4).digitToInt()

        // Flag that indicates if there is any piece that may interrupt the movement
        var validator = 0

        // Coordinate used to move in the tile and interrupt movement if we are in the same tile were we began
        var position : Int

        // Coordinate used to move in the tile and interrupt movement if we there is any other piece in the diagonal
        var positionX: Int
        var positionY: Int

        // Checks if we are in the same row
        if(initialX == destinyX){
            if(initialY < destinyY) {
                position = initialY + 1 // Avoid starting in initial position
                while(position < destinyY && validator == 0){
                    if(board[destinyX][position] != null) validator = 1 // Checks if there is any piece on the way
                    position++
                }
            }
            else {
                position = initialY - 1
                while (position > destinyY && validator == 0) {
                    if (board[destinyX][position] != null) validator = 1
                    position--
                }
            }

        }

        // Checks if we are in the same column
        else if(initialY == destinyY){
            if(initialX < destinyX){
                position = initialX + 1
                while(position < destinyX && validator == 0){
                    if(board[position][destinyY] != null) validator = 1
                    position++
                }
            }
            else {
                position = initialX - 1
                while (position > destinyX && validator == 0) {
                    if (board[position][destinyY] != null) validator = 1
                    position--
                }
            }
        }

        // Checks if the modules between the final coordinates and initial coordinates are equal
        // ( Ex: 4 moves in diagonal = 4 moves in row + 4 moves in column )
        else if( abs(destinyX-initialX) == abs(destinyY-initialY) ) {

            // Left side bottom movement
            if (destinyX < initialX && destinyY < initialY) {
                // Change coordinates to check if there is any piece on the diagonal
                positionX = initialX - 1
                positionY = initialY - 1
                while (positionX > destinyX && positionY > destinyY) {
                    if (board[positionX][positionY] != null) validator = 1
                    positionX--
                    positionY--
                }

            }
            // Right side top movement
            else if (destinyX > initialX && destinyY > initialY) {
                positionX = initialX + 1
                positionY = initialY + 1
                while (positionX < destinyX && positionY < destinyY) {
                    if (board[positionX][positionY] != null) validator = 1
                    positionX++
                    positionY++
                }
            }
            // Right side bottom movement
            else if (destinyX > initialX && destinyY < initialY) {
                positionX = initialX + 1
                positionY = initialY - 1
                while (positionX < destinyX && positionY > destinyY) {
                    if (board[positionX][positionY] != null) validator = 1
                    positionX++
                    positionY--
                }

            }
            // Left side top movement
            else if (destinyX < initialX && destinyY > initialY) {
                positionX = initialX - 1
                positionY = initialY + 1
                while (positionX > destinyX && positionY < destinyY) {
                    if (board[positionX][positionY] != null) validator = 1
                    positionX--
                    positionY++
                }

            }
        }

        // If the other movements failed it's an invalid move
        else validator = 1

        // If there isn't any piece on the way and it was a valid move,
        // remove the piece from the previous location and put it on the destiny
        if(validator == 0){
            board[initialX][initialY] = null
            board[destinyX][destinyY] = pieceId
        }

        return board;
    }

    /**
     * Makes the movements for the KING piece
     */
    fun kingMoveLocal(board: Array<Array<Pair<model.Army, model.Piece>?>>, move: String, pieceId: Pair<model.Army, model.Piece>) : Array<Array<Pair<model.Army, model.Piece>?>> {

        // Initial position of the piece
        var initialY = move.get(1).digitToInt()
        var initialX = move.get(2).digitToInt()

        // Final position for the piece
        var destinyX = move.get(4).digitToInt()
        var destinyY = move.get(3).digitToInt()

        // Checks if we are in the same row or column
        if(initialX == destinyX || initialY == destinyY){

            // Verify if there is one tile movement in the row or in the column direction
            if( (abs(destinyX-initialX) + abs(destinyY-initialY)) == 1 ){
                ROQUE_REQUIREMENT = false // If the king moved from the initial position he can't make the ROQUE anymore
                board[initialX][initialY] = null
                board[destinyX][destinyY] = pieceId
            }
        }
        // Verify if is one tile movement in the diagonal
        // ( Ex: 1 diagonal = 1 row + 1 column )
        else if( (abs(destinyX-initialX) + abs(destinyY-initialY)) == 2 ){
            ROQUE_REQUIREMENT = false
            board[initialX][initialY] = null
            board[destinyX][destinyY] = pieceId
        }

        return board;
    }

    /**
     * Makes the movements for the BISHOP piece
     */
    fun bishopMoveLocal(board: Array<Array<Pair<model.Army, model.Piece>?>>, move: String, pieceId: Pair<model.Army, model.Piece>) : Array<Array<Pair<model.Army, model.Piece>?>>{

        // Initial position of the piece
        var initialY = move.get(1).digitToInt()
        var initialX = move.get(2).digitToInt()

        // Final position for the piece
        var destinyY = move.get(3).digitToInt()
        var destinyX = move.get(4).digitToInt()

        // Flag that indicates if there is any piece that may interrupt the movement
        var validator = 0

        // Coordinate used to move in the tile and interrupt movement if we there is any other piece in the diagonal
        var positionX: Int
        var positionY: Int

        // Checks if the modules between the final coordinates and initial coordinates are equal
        // ( Ex: 4 moves in diagonal = 4 moves in row + 4 moves in column )
        if( abs(destinyX-initialX) == abs(destinyY-initialY) ) {

            // Left side bottom movement
            if (destinyX < initialX && destinyY < initialY) {
                // Change coordinates to check if there is any piece on the diagonal
                positionX = initialX - 1
                positionY = initialY - 1
                while (positionX > destinyX && positionY > destinyY) {
                    if (board[positionX][positionY] != null) validator = 1
                    positionX--
                    positionY--
                }

            }
            // Right side top movement
            else if (destinyX > initialX && destinyY > initialY) {
                positionX = initialX + 1
                positionY = initialY + 1
                while (positionX < destinyX && positionY < destinyY) {
                    if (board[positionX][positionY] != null) validator = 1
                    positionX++
                    positionY++
                }
            }
            // Right side bottom movement
            else if (destinyX > initialX && destinyY < initialY) {
                positionX = initialX + 1
                positionY = initialY - 1
                while (positionX < destinyX && positionY > destinyY) {
                    if (board[positionX][positionY] != null) validator = 1
                    positionX++
                    positionY--
                }

            }
            // Left side top movement
            else if (destinyX < initialX && destinyY > initialY) {
                positionX = initialX - 1
                positionY = initialY + 1
                while (positionX > destinyX && positionY < destinyY) {
                    if (board[positionX][positionY] != null) validator = 1
                    positionX--
                    positionY++
                }

            }
        }

        // If the other movements failed it's an invalid move
        else validator = 1

        // If there isn't any piece on the way and it was a valid move,
        // remove the piece from the previous location and put it on the destiny
        if(validator == 0){
            board[initialX][initialY] = null
            board[destinyX][destinyY] = pieceId
        }

        return board;
    }

    /**
     * Makes the movements for the ROQUE
     */
    fun swapMoveLocal(board: Array<Array<Pair<model.Army, model.Piece>?>>, move: String, pieceId1: Pair<model.Army, model.Piece>, pieceId2: Pair<model.Army, model.Piece>) : Array<Array<Pair<model.Army, model.Piece>?>>{

        // Initial position of the piece
        var initialY = move.get(1).digitToInt()
        var initialX = move.get(2).digitToInt()

        // Final position for the piece
        var destinyX = move.get(4).digitToInt()
        var destinyY = move.get(3).digitToInt()

        // Flag that indicates if there is any piece that may interrupt the movement
        var validator = 0

        // Coordinate used to move in the tile and interrupt movement if we are in the same tile were we began
        var position : Int

        // Checks if we are in the first row/column or the last row/column, if its the king,
        // if the destiny row is the same and the initial and if the king hasn't moved.
        if( (initialX == 0 || initialX == 7) && (destinyY == 0 || destinyY == 7) && initialY == 4 && (initialX == destinyX) && ROQUE_REQUIREMENT) {
            // Left side movement
            if(destinyY < initialY) {
                position = initialY - 1 // Avoid starting in the initial position
                while (position > destinyY) {
                    if (board[destinyX][position] != null) validator = 1  // Checks if there is any piece on the way
                    position--
                }
                // Makes the swap
                if(validator == 0) {
                    board[destinyX][3] = pieceId2
                    board[destinyX][2] = pieceId1
                    board[destinyX][0] = null
                    board[destinyX][4] = null
                }
            }
            // Right side movement
            else {
                position = initialY + 1
                while (position < destinyX) {
                    if (board[destinyX][position] != null) validator = 1
                    position++
                }
                // Makes the swap
                if(validator == 0) {
                    board[destinyX][5] = pieceId2
                    board[destinyX][6] = pieceId1
                    board[destinyX][7] = null
                    board[destinyX][4] = null
                }
            }
        }

        return board;
    }

    /**
     * Makes the movements for the black PAWN
     */
    fun blackPawnMoveLocal(board: Array<Array<Pair<model.Army, model.Piece>?>>, move: String, pieceId: Pair<model.Army, model.Piece>) : Array<Array<Pair<model.Army, model.Piece>?>> {

        // Initial position of the piece
        var initialY = move.get(0).digitToInt()
        var initialX = move.get(1).digitToInt()

        // Final position for the piece
        var destinyX = move.get(3).digitToInt()
        var destinyY = move.get(2).digitToInt()

        // Flag that indicates if there is any piece that may interrupt the movement
        var validator = 1

        // Checks if the pawn movement is on the final row
        var piece = pieceId
        if(destinyX == 7) {
            piece = Pair(pieceId.first, randPiece()) // Swaps the pawn to a random piece
        }

        // Movement in the same column
        if( abs(initialY-destinyY) == 0 ) {
            // First move
            if(initialX == 1 && (destinyX - initialX == 1 || destinyX-initialX == 2) && board[destinyX][destinyY] == null) validator = 0

            else if(destinyX-initialX == 1  && board[destinyX][destinyY] == null) validator = 0
        }

        // Movement in the diagonal
        else if( abs(initialY-destinyY) == 1 ){
            if(destinyX-initialX == 1  && board[destinyX][destinyY] != null) validator = 0
        }

        if(validator == 0){
            board[initialX][initialY] = null
            board[destinyX][destinyY] = piece
        }

        return board;
    }

    /**
     * Makes the movements for the white PAWN
     */
    fun whitePawnMoveLocal(board: Array<Array<Pair<model.Army, model.Piece>?>>, move: String, pieceId: Pair<model.Army, model.Piece>) : Array<Array<Pair<model.Army, model.Piece>?>> {

        // Initial position of the piece
        var initialY = move.get(0).digitToInt()
        var initialX = move.get(1).digitToInt()

        // Final position for the piece
        var destinyX = move.get(3).digitToInt()
        var destinyY = move.get(2).digitToInt()

        // Flag that indicates if there is any piece that may interrupt the movement
        var validator = 1

        // Checks if the pawn movement is on the final row
        var piece = pieceId
        if(destinyX == 0) {
            piece = Pair(pieceId.first, randPiece()) // Swaps the pawn to a random piece
        }

        // Movement in the same column
        if( abs(initialY-destinyY) == 0 ) {
            // First move
            if(initialX == 6 && (initialX - destinyX == 1 || initialX - destinyX== 2) && board[destinyX][destinyY] == null) validator = 0

            else if(initialX-destinyX == 1  && board[destinyX][destinyY] == null) validator = 0
        }

        // Movement in the diagonal
        else if(abs(initialY-destinyY) == 1){
            if(initialX-destinyX == 1  && board[destinyX][destinyY] != null) validator = 0
        }

        if(validator == 0){
            board[initialX][initialY] = null
            board[destinyX][destinyY] = piece
        }

        return board;
    }

