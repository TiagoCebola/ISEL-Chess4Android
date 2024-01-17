package pt.isel.pdm.chess4android.history

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import pt.isel.pdm.chess4android.R
import pt.isel.pdm.chess4android.common.*
import pt.isel.pdm.chess4android.daily.DayPuzzleDTO
import pt.isel.pdm.chess4android.databinding.ActivityPuzzleBinding

private const val PUZZLE_EXTRA = "PuzzleActivityLegacy.Extra.Puzzle"
private const val BOARD_EXTRA = "PuzzleActivityLegacy.Extra.Board"
/**
 * The screen used to display the a Puzzle.
 */
class PuzzleActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityPuzzleBinding.inflate(layoutInflater)
    }

    private val viewModel : PuzzleActivityViewModel by viewModels()

    companion object {
        fun buildIntent(origin: Activity, puzzleDto: DayPuzzleDTO): Intent {

            var msg = Intent(origin, PuzzleActivity::class.java)
            var boardState = boardStateToStringArray(puzzleDto.resolvedBoard)
            msg.putExtra(PUZZLE_EXTRA, puzzleDto)
            msg.putExtra(BOARD_EXTRA, boardState)
            return msg
        }
    }

    /**
     * Sets up the screen look and behaviour
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.restartButton.isVisible = false;


        /* Gets the information from another activity */
        val puzzleDTO = intent.getParcelableExtra<DayPuzzleDTO>(PUZZLE_EXTRA)
        val boardDTO = intent.getStringArrayExtra(BOARD_EXTRA)

        /* Transformes the string with the boardInfo from Intent to a boardState */
        if(boardDTO != null) puzzleDTO?.resolvedBoard = stringArrayToBoardState(boardDTO)

        var boardResult = BoardState()
        if (puzzleDTO != null) {
            if(!puzzleDTO.status) binding.resolvedPuzzle.isVisible = false

            binding.restartButton.setOnClickListener {
                recreate()
            }

            /* Update view parameters by seeing the board result */
            binding.resolvedPuzzle.setOnClickListener{
                binding.boardView.model = puzzleDTO.resolvedBoard
                binding.chessResolution.text = resources.getText(R.string.resolution_title)
                binding.boardView.tileClickedListener = null
                binding.restartButton.isVisible = true
            }

            boardResult = createModel(puzzleDTO)
            binding.boardView.model = boardResult
        }

        // Game Validations
        if ( getPlayingArmy().equals(model.Army.BLACK.name, true) ) {
            binding.chessResolution.text = resources.getText(R.string.chess_moveBlack_title)
        }
        else binding.chessResolution.text = resources.getText(R.string.chess_moveWhite_title)

        binding.boardView.tileClickedListener = { tile: Tile, row: Int, column: Int ->

            var piece : Pair<model.Army, model.Piece>? = tile.piece

            // Updates the board with the player moves
            binding.boardView.model = updateModel(boardResult, row, column, piece)
            var status = getPlayVerifier()

            // Verify if the destination to the piece is the right one
            if(getPiece() == null) {
                if(status == 0) {
                    Toast.makeText(applicationContext, "Bad Play", Toast.LENGTH_SHORT).show()
                }
                else if(status == 1) {
                    Toast.makeText(applicationContext, "Nice Play", Toast.LENGTH_SHORT).show()
                }
            }
            // Puzzle End
            if(status == -1) {
                binding.chessResolution.text = resources.getText(R.string.chess_sucess_title)
                binding.restartButton.isVisible = true
                binding.resolvedPuzzle.isVisible = true
                binding.boardView.tileClickedListener = null

                // Updates the DB
                if (puzzleDTO != null && !puzzleDTO.status) {
                    puzzleDTO.resolvedBoard = boardResult
                    puzzleDTO.status = true
                    viewModel.updatePuzzle(puzzleDTO)
                }
            }

        }
    }

}