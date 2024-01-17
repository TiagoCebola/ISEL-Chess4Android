package pt.isel.pdm.chess4android.history

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import pt.isel.pdm.chess4android.common.PuzzleOfDayApplication
import pt.isel.pdm.chess4android.common.PuzzleOfDayRepository
import pt.isel.pdm.chess4android.daily.DayPuzzleDTO
import pt.isel.pdm.chess4android.daily.ServiceUnavailable


class PuzzleActivityViewModel(application : Application) : AndroidViewModel(application) {

        init {
            Log.v("APP_TAG", "PuzzleActivityViewModel.init()")
        }

        fun updatePuzzle(puzzledto : DayPuzzleDTO) {

            val app = getApplication<PuzzleOfDayApplication>()
            val repo = PuzzleOfDayRepository(app.puzzleOfDayService, app.historyDB.getHistoryPuzzleDao())

            repo.asyncUpdateDB(puzzledto) { result ->
                result
                    .onSuccess { puzzleDTO ->
                        Result.success(puzzleDTO)
                    }
                    .onFailure {
                        ServiceUnavailable(cause = it)
                    }
            }

        }

}