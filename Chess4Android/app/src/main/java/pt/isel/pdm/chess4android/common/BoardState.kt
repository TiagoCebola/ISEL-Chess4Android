package pt.isel.pdm.chess4android.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import pt.isel.pdm.chess4android.common.model.Army
import pt.isel.pdm.chess4android.common.model.Piece

private fun createPieceEntry(army: Army, piece: Piece) = Pair(army, piece)

private val whiteRook = createPieceEntry(Army.WHITE, Piece.ROOK)
private val whiteBishop = createPieceEntry(Army.WHITE, Piece.BISHOP)
private val whiteKnight = createPieceEntry(Army.WHITE, Piece.KNIGHT)
private val whiteKing = createPieceEntry(Army.WHITE, Piece.KING)
private val whiteQueen = createPieceEntry(Army.WHITE, Piece.QUEEN)
private val whitePawn = createPieceEntry(Army.WHITE, Piece.PAWN)

private val blackRook = createPieceEntry(Army.BLACK, Piece.ROOK)
private val blackBishop = createPieceEntry(Army.BLACK, Piece.BISHOP)
private val blackKnight = createPieceEntry(Army.BLACK, Piece.KNIGHT)
private val blackKing = createPieceEntry(Army.BLACK, Piece.KING)
private val blackQueen = createPieceEntry(Army.BLACK, Piece.QUEEN)
private val blackPawn = createPieceEntry(Army.BLACK, Piece.PAWN)


 @Parcelize
 class BoardState() : Parcelable {

     /**
      * Initial board state
      */

    private var board  : Array<Array<Pair<Army,Piece>?>> = arrayOf(
        arrayOf(blackRook, blackKnight, blackBishop, blackQueen, blackKing, blackBishop, blackKnight,  blackRook),
        arrayOf(blackPawn, blackPawn, blackPawn, blackPawn, blackPawn, blackPawn, blackPawn, blackPawn),
        arrayOfNulls(8),
        arrayOfNulls(8),
        arrayOfNulls(8),
        arrayOfNulls(8),
        arrayOf(whitePawn, whitePawn, whitePawn, whitePawn, whitePawn, whitePawn, whitePawn, whitePawn),
        arrayOf(whiteRook,whiteKnight, whiteBishop, whiteQueen, whiteKing, whiteBishop, whiteKnight,  whiteRook)
    )

     /**
      * Changes the board by the play made in API game mode
      */
    fun doMove(move : String, idx : Int) {

        if(move[0].equals('N')) {
            if(idx % 2 == 0) {
                board = knightMove(board, move, whiteKnight)
            }
            else board = knightMove(board, move, blackKnight)
        }

        else if(move[0].equals('B')) {
            if(idx % 2 == 0) {
                board = bishopMove(board, move, whiteBishop)
            }
            else board = bishopMove(board, move, blackBishop)
        }


        else if(move[0].equals('R')) {
            if(idx % 2 == 0) {
                board = towerMove(board, move, whiteRook)
            }
            else  board = towerMove(board, move, blackRook)
        }

        else if(move[0].equals('K')) {
            if(idx % 2 == 0) {
                board = RoyalityMove(board, move, whiteKing)
            }
            else board = RoyalityMove(board, move, blackKing)
        }

        else if(move[0].equals('Q')) {
            if(idx % 2 == 0) {
                board = RoyalityMove(board, move, whiteQueen)
            }
            else board = RoyalityMove(board, move, blackQueen)
        }

        else if(move[0].equals('O')) {
            if(idx % 2 == 0) {
                board = swapMove(board, move, whiteKing, whiteRook)
            }
            else board = swapMove(board, move, blackKing, blackRook)
        }

        else if(idx % 2 == 0) {
            board = pawnMove(board, move, whitePawn)
        }
        else board = pawnMove(board, move, blackPawn)

    }


     /**
      * Changes the board by the play made in LOCAL game mode
      */
     fun doMoveLocal(move : String, idx : Int) {

         if(move[0].equals('N')) {
             if(idx % 2 == 0) {
                 board = knightMoveLocal(board, move, whiteKnight)
             }
             else board = knightMoveLocal(board, move, blackKnight)
         }

         else if(move[0].equals('B')) {
             if(idx % 2 == 0) {
                 board = bishopMoveLocal(board, move, whiteBishop)
             }
             else board = bishopMoveLocal(board, move, blackBishop)
         }


         else if(move[0].equals('R')) {
             if(idx % 2 == 0) {
                 board = towerMoveLocal(board, move, whiteRook)
             }
             else  board = towerMoveLocal(board, move, blackRook)
         }

         else if(move[0].equals('K')) {
             if(idx % 2 == 0) {
                 board = kingMoveLocal(board, move, whiteKing)
             }
             else board = kingMoveLocal(board, move, blackKing)
         }

         else if(move[0].equals('Q')) {
             if(idx % 2 == 0) {
                 board = queenMoveLocal(board, move, whiteQueen)
             }
             else board = queenMoveLocal(board, move, blackQueen)
         }

         else if(move[0].equals('O')) {
             if(idx % 2 == 0) {
                 board = swapMoveLocal(board, move, whiteKing, whiteRook)
             }
             else board = swapMoveLocal(board, move, blackKing, blackRook)
         }

         else if(idx % 2 == 0) {
             board = whitePawnMoveLocal(board, move, whitePawn)
         }
         else board = blackPawnMoveLocal(board, move, blackPawn)

     }

     /**
      * Returns the board
      */
     fun getBoard() : Array<Array<Pair<Army,Piece>?>> {
         return board
     }

     /**
      * Defines a board
      */
     fun setBoard(pairArray: Array<Array<Pair<Army,Piece>?>>) : BoardState {
         board = pairArray
         return this
     }
}

