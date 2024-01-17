package pt.isel.pdm.chess4android.history

import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import pt.isel.pdm.chess4android.databinding.ActivityHistoryBinding
import pt.isel.pdm.chess4android.history.PuzzleActivity.Companion.buildIntent

/**
 * Shows if we are alredy in a Activity.
 */
var flagActivity : Boolean = false;

/**
 * The screen used to display the list of daily puzzles stored locally.
 */

class HistoryActivity : AppCompatActivity() {

    private val binding by lazy { ActivityHistoryBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<HistoryActivityViewModel>()

    /**
     * Sets up the screen look and behaviour
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.puzzleList.layoutManager = LinearLayoutManager(this)

        // Get the list of puzzles, if we haven't fetched it yet
        val dataSource = viewModel.history
        if(dataSource == null) {
            viewModel.loadHistory().observe(this) {
                flagActivity = true
                binding.puzzleList.adapter = HistoryAdapter(it) { puzzleDto ->
                    startActivity(buildIntent(this, puzzleDto))

                }
            }
        }
        else {
            dataSource.observe(this) {
                flagActivity = true
                binding.puzzleList.adapter = HistoryAdapter(it) { puzzleDto ->
                    startActivity(buildIntent(this, puzzleDto))
                }
            }
        }
    }


    override fun onStart() {
        super.onStart()
        if(flagActivity) {
            viewModel.loadHistory().observe(this) {
                binding.puzzleList.adapter = HistoryAdapter(it) { puzzleDto ->
                    startActivity(buildIntent(this, puzzleDto))
                }
            }
        }
    }
}