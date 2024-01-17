package pt.isel.pdm.chess4android.resolution

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import pt.isel.pdm.chess4android.common.*
import pt.isel.pdm.chess4android.daily.ServiceUnavailable

private const val RESOLUTION_ACTIVITY_VIEW_STATE = "ResolutionActivity.ViewState"

class ResolutionView(application : Application, private val state: SavedStateHandle) : AndroidViewModel(application)  {

    init {
        Log.v("APP_TAG", "ResolutionView.init()")
    }

    private val _puzzleOfDay : MutableLiveData<BoardState> = MutableLiveData()
    val puzzleOfDay : LiveData<BoardState> = _puzzleOfDay

    /**
     * Asynchronous operation to fetch the quote of the day from the remote server. The operation
     * result (if successful) is published to the associated [LiveData] instance, [puzzleOfDay].
     */
    fun getPuzzleOfDay() {
        Log.v(APP_TAG, "Thread : ${Thread.currentThread().name}: Fetching ... ")
        val app = getApplication<PuzzleOfDayApplication>()
        val repo = PuzzleOfDayRepository(app.puzzleOfDayService, app.historyDB.getHistoryPuzzleDao())
        repo.fetchPuzzleOfDay { result ->
            result
                .onSuccess { puzzleDTO ->
                    _puzzleOfDay.value = createModel(puzzleDTO)
                    state.set(RESOLUTION_ACTIVITY_VIEW_STATE, result.getOrThrow())
                }
                .onFailure {
                   ServiceUnavailable(cause = it)
                    //result.exceptionOrNull()
                }
        }
        Log.v(APP_TAG, "Thread : ${Thread.currentThread().name}: Return from fetching puzzle")
    }

}