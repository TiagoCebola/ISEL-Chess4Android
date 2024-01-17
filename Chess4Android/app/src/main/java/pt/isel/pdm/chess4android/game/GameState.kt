package pt.isel.pdm.chess4android.game

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import pt.isel.pdm.chess4android.common.BoardState
import pt.isel.pdm.chess4android.model.Player
import pt.isel.pdm.chess4android.common.boardStateToStringArray
import pt.isel.pdm.chess4android.common.stringArrayToBoardState
import pt.isel.pdm.chess4android.model.Board

/**
 * Data type used to represent the game state externally, that is, when the game state crosses
 * process boundaries and device boundaries.
 */
@Parcelize
data class GameState(
    val id: String,
    val turn: String?,
    val board: Array<String>
): Parcelable

/**
 * Extension to create a [GameState] instance from this [Board].
 */
fun Board.toGameState(gameId: String, boardState: BoardState): GameState {
    return GameState(id = gameId, turn = turn?.name, board = boardStateToStringArray(boardState))
}

/**
 * Extension to create a [Board] instance from this [GameState].
 */
fun GameState.toBoard() = Board(
    turn = if (turn != null) Player.valueOf(turn) else null,
    board = stringArrayToBoardState(board)
)

