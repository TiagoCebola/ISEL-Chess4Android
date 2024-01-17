package pt.isel.pdm.chess4android.game

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pt.isel.pdm.chess4android.ChessApplication
import pt.isel.pdm.chess4android.TAG
import pt.isel.pdm.chess4android.common.BoardState
import pt.isel.pdm.chess4android.common.getPlayVerifier
import pt.isel.pdm.chess4android.common.model
import pt.isel.pdm.chess4android.common.updateModelLocalMultiplayer
import pt.isel.pdm.chess4android.model.Board
import pt.isel.pdm.chess4android.model.Player
import pt.isel.pdm.chess4android.model.stringToPlayer


/**
 * The Game screen view model
 */
class GameViewModel(
    app: Application,
    private val initialGameState: GameState,
    val localPlayer: Player
): AndroidViewModel(app) {

    private val _game: MutableLiveData<Result<Board>> by lazy {
        MutableLiveData(Result.success(initialGameState.toBoard()))
    }

    val game: LiveData<Result<Board>> = _game

    private val gameSubscription = getApplication<ChessApplication>()
        .gamesRepository.subscribeToGameStateChanges(
            challengeId = initialGameState.id,
            onSubscriptionError = { _game.value = Result.failure(it) },
            onGameStateChange = { _game.value = Result.success(it.toBoard()) }
        )

    /**
     * Makes a move at the given position. Publishes the result in [game] live data
     */
    fun makeMove(board: BoardState, row: Int, column: Int, piece: Pair<model.Army, model.Piece>?, turn: String?) : BoardState {
            Log.v(TAG, "Making move at $row , $column")
            val player = stringToPlayer(turn)
            if (player == localPlayer) {
                var idx : Int
                if(turn == "WHITE") idx = 0
                else idx = 1
                val newBoardState = updateModelLocalMultiplayer(board,row,column, idx) // ,piece) ,idx)
                var status = getPlayVerifier()

                // Puzzle End
                if (status == 1) {
                    Log.v(TAG, "Board became ${Board(turn=player).toGameState(initialGameState.id,newBoardState)}")
                    var newBoard = Board(player,newBoardState)
                    _game.value = Result.success(newBoard)
                    getApplication<ChessApplication>().gamesRepository.updateGameState(
                        gameState = Board(turn=player).toGameState(initialGameState.id,newBoardState),
                        onComplete = { result ->
                            result.onFailure { _game.value = Result.failure(it) }
                        }
                    )
                }
                return newBoardState
            }
            return board
    }

    /**
     * View model is destroyed
     */
    override fun onCleared() {
        super.onCleared()
        getApplication<ChessApplication>().gamesRepository.deleteGame(
            challengeId = initialGameState.id,
            onComplete = { }
        )
        gameSubscription.remove()
    }
}