package pt.isel.pdm.chess4android.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pt.isel.pdm.chess4android.common.PuzzleOfDayApplication
import pt.isel.pdm.chess4android.common.callbackAfterAsync
import pt.isel.pdm.chess4android.daily.DayPuzzleDTO

/**
 * The actual execution host behind the quote history screen (i.e. the [HistoryActivity]).
 */
class HistoryActivityViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * Holds a [LiveData] with the list of puzzles, or null if it has not yet been requested by
     * the [HistoryActivity] through a call to [loadHistory]
     */
    var history: LiveData<List<DayPuzzleDTO>>? = null
        private set

    private val historyDAO : HistoryPuzzleDao by lazy {
        getApplication<PuzzleOfDayApplication>().historyDB.getHistoryPuzzleDao()
    }
    /**
     * Gets the puzzles list (history) from the DB.
     */
    fun loadHistory(): LiveData<List<DayPuzzleDTO>> {
        val publish = MutableLiveData<List<DayPuzzleDTO>>()
        history = publish
        callbackAfterAsync(
            asyncAction = {
                // Gets a PuzzleEntity from the database and transformes into a PuzzleDTO
                historyDAO.getAll().map {
                    DayPuzzleDTO(game = it.game, puzzle = it.puzzle,  timestamp = it.timestamp, status = it.status, resolvedBoard = it.resolvedBoard)
                }
            },
            callback = { result ->
                result.onSuccess { publish.value = it }
                result.onFailure { publish.value = emptyList() }
            }
        )
        return publish
    }
}