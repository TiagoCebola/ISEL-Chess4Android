package pt.isel.pdm.chess4android.game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pt.isel.pdm.chess4android.R
import pt.isel.pdm.chess4android.TAG
import pt.isel.pdm.chess4android.challenges.ChallengeInfo
import pt.isel.pdm.chess4android.common.BoardState
import pt.isel.pdm.chess4android.common.Tile
import pt.isel.pdm.chess4android.common.model
import pt.isel.pdm.chess4android.common.stringArrayToBoardState
import pt.isel.pdm.chess4android.databinding.ActivityGameBinding
import pt.isel.pdm.chess4android.model.Board
import pt.isel.pdm.chess4android.model.Player

private const val GAME_EXTRA = "GameActivity.GameInfoExtra"
private const val LOCAL_PLAYER_EXTRA = "GameActivity.PlayerExtra"

/**
 * The activity that displays the board.
 */
class GameActivity : AppCompatActivity() {

    companion object {
        fun buildIntent(
            origin: Context,
            turn: Player,
            local: Player,
            boardState: BoardState,
            challengeInfo: ChallengeInfo
        ) =
            Intent(origin, GameActivity::class.java)
                .putExtra(GAME_EXTRA, Board(turn = turn).toGameState(challengeInfo.id, boardState))
                .putExtra(LOCAL_PLAYER_EXTRA, local.name)
    }

    private val binding: ActivityGameBinding by lazy { ActivityGameBinding.inflate(layoutInflater) }

    private val localPlayer: Player by lazy {
        val local = intent.getStringExtra(LOCAL_PLAYER_EXTRA)
        if (local != null) Player.valueOf(local)
        else throw IllegalArgumentException("Mandatory extra $LOCAL_PLAYER_EXTRA not present")
    }

    private val initialState: GameState by lazy {
        intent.getParcelableExtra<GameState>(GAME_EXTRA)
            ?: throw IllegalArgumentException("Mandatory extra $GAME_EXTRA not present")
    }

    private val viewModel: GameViewModel by viewModels {
        @Suppress("UNCHECKED_CAST")
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return GameViewModel(application, initialState, localPlayer) as T
            }
        }
    }

    /**
     * Callback method that handles the activity initiation
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.v(TAG, "GameActivity.onCreate()")
        Log.v(TAG, "Local player is $localPlayer")
        Log.v(TAG, "Turn player is ${initialState.turn}")

        binding.forfeitButton.setOnClickListener {
            // Game End
            binding.boardView.tileClickedListener = null
        }

        viewModel.game.observe(this) {
            updateUI()
        }


    }

    private fun updateBoard() {
        binding.forfeitButton.isClickable =
            if (viewModel.game.value?.isSuccess == true)
                viewModel.localPlayer == viewModel.game.value?.getOrThrow()?.turn
            else false

        /* Gets the information from another activity */
        val game = intent.getParcelableExtra<GameState>(GAME_EXTRA)

        var boardState = BoardState()
        if (game != null) {
            /* Transformes the string with the boardInfo from Intent to a boardState */
            if (game.board != null) boardState = stringArrayToBoardState(game.board)

            binding.boardView.model = boardState

            // Game Validations

            binding.boardView.tileClickedListener = { tile: Tile, row: Int, column: Int ->

                var piece: Pair<model.Army, model.Piece>? = tile.piece

                // Updates the board with the player moves
                boardState = viewModel.makeMove(boardState, row, column, piece, game.turn)
                binding.boardView.model = boardState

            }
        }
    }

    private fun updateUI() {
        updateBoard()
    }
}

