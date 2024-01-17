package pt.isel.pdm.chess4android.resolution

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import pt.isel.pdm.chess4android.R
import pt.isel.pdm.chess4android.common.*
import pt.isel.pdm.chess4android.databinding.ActivityResolutionBinding

class ResolutionActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityResolutionBinding.inflate(layoutInflater)
    }

    // Se não existe intancia de ViewModel até este momento cria, caso contrário obtém a instancia
    private val viewModel : ResolutionView by viewModels()

    private fun displayError() {
        Toast.makeText(this, R.string.get_puzzle_error, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.restartButton.isVisible = false

        // Vai a API e cria um tabuleiro com o pgn
        viewModel.getPuzzleOfDay()

        // Fica a escuta de alterações na board
        viewModel.puzzleOfDay.observe(this) { boardResult ->

            Log.v("APP_TAG", "Board Alteration")
            binding.boardView.model = boardResult

            if ( getPlayingArmy().equals(model.Army.BLACK.name, true) ) {
                binding.chessResolution.text = resources.getText(R.string.chess_moveBlack_title)
            }
            else binding.chessResolution.text = resources.getText(R.string.chess_moveWhite_title)

            binding.boardView.tileClickedListener = { tile: Tile, row: Int, column: Int ->

                var piece : Pair<model.Army, model.Piece>? = tile.piece

                // Atualiza a board de acordo com o click do jogador
                binding.boardView.model = updateModel(boardResult, row, column, piece)
                var status = getPlayVerifier()

                // Apartir do destino escolhido a peça faz uma seleção
                if(getPiece() == null) {
                    if(status == 0) {
                        Toast.makeText(applicationContext, "Bad Play", Toast.LENGTH_SHORT).show()
                    }
                    else if(status == 1) {
                       Toast.makeText(applicationContext, "Nice Play", Toast.LENGTH_SHORT).show()
                    }
                }
                // Termino do puzzle
                if(status == -1) {
                    binding.chessResolution.text = resources.getText(R.string.chess_sucess_title)
                    binding.restartButton.isVisible = true
                    binding.restartButton.setOnClickListener {
                        recreate()
                    }
                }
            }
        }

    }

}