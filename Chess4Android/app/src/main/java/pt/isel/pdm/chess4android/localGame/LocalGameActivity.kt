package pt.isel.pdm.chess4android.localGame

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import pt.isel.pdm.chess4android.R
import pt.isel.pdm.chess4android.common.*
import pt.isel.pdm.chess4android.databinding.ActivityLocalGameBinding

class LocalGameActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLocalGameBinding.inflate(layoutInflater)
    }

    /**
     * In the end of the turn swap the player
     */
    private fun switchPlayerTurn(playerTurn : String) : String {
        if(playerTurn == "WHITE") return "BLACK"
        return "WHITE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // The game starts with the white playing
        binding.chessResolution.text = resources.getText(R.string.white_piece_turn)

        // Initialize variables
        var boardResult = BoardState()
        var playerTurn = "WHITE"
        var playerFlag : Int

        // Shows the initial board
        binding.boardView.model = boardResult

        // Button that sees if a player gave up and ends the game
        binding.forfeitButton.setOnClickListener {
            if(playerTurn == "WHITE") binding.chessResolution.text = resources.getText(R.string.white_surrender).toString() + "\n" +
                                                                          resources.getText(R.string.black_chess_winner).toString()

            else binding.chessResolution.text = resources.getText(R.string.black_surrender).toString() + "\n" +
                                                             resources.getText(R.string.white_chess_winner).toString()
            // Disable clicks on the tile
            binding.boardView.tileClickedListener = null
        }

        // Game Validations
        binding.boardView.tileClickedListener = { tile: Tile, row: Int, column: Int ->

            // Gets the piece that was clicked
            var piece : Pair<model.Army, model.Piece>? = tile.piece

            if (playerTurn == "WHITE") playerFlag = 0
            else playerFlag = 1

            // Checks if it is the first click and updates the piece in evidence
            if(piece?.first?.name == playerTurn && getPiece() == null) {
                selectPiece(piece, row, column)
            }

            // Checks if it is the second click and avoids eating the own pieces then updates the board
            else if (piece?.first?.name != playerTurn && getPiece() != null ) {
                binding.boardView.model = updateModelLocalMultiplayer(boardResult, row, column, playerFlag)
            }

            // Checks if it is a ROQUE move (selected piece = KING and destiny tile has a ROOK with the same color) then updates the board
            else if(getPiece() == "K" && piece?.second == model.Piece.ROOK && piece.first.name == playerTurn) {
                roque("O")
                binding.boardView.model = updateModelLocalMultiplayer(boardResult, row, column, playerFlag)
            }

            // Checks if the player selected a wrong space (when try to eat the own piece)
            else  {
                Toast.makeText(applicationContext, "Cannot make that movement", Toast.LENGTH_SHORT).show()
                selectPiece(null, 0, 0)
            }

            // Get the status of the play
            var status = getPlayVerifier()

            // Switch to another Player
            if (status == 1) {
                playerTurn = switchPlayerTurn(playerTurn)
                if (playerTurn == "WHITE") binding.chessResolution.text = resources.getText(R.string.white_piece_turn)
                else  binding.chessResolution.text = resources.getText(R.string.black_piece_turn)
            }

            // Invalid move
            else if (status==2) Toast.makeText(applicationContext, "Cannot make that movement", Toast.LENGTH_SHORT).show()

            // Sees if the KING is in CHECK
            else if (status == 3) {
                playerTurn = switchPlayerTurn(playerTurn)
                if(playerTurn == "WHITE") binding.chessResolution.text = resources.getText(R.string.white_king_checked)
                else  binding.chessResolution.text = resources.getText(R.string.black_king_checked)
            }

            // End Game
            else if (status == -1) {
                binding.forfeitButton.isVisible = false
                binding.boardView.tileClickedListener = null
                if (playerTurn == "WHITE") binding.chessResolution.text = resources.getText(R.string.white_chess_winner)
                else  binding.chessResolution.text = resources.getText(R.string.black_chess_winner)
            }

        }
    }
}