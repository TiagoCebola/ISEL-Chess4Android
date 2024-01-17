package pt.isel.pdm.chess4android.daily

import android.content.Context
import android.util.Log
import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.google.common.util.concurrent.ListenableFuture
import pt.isel.pdm.chess4android.common.APP_TAG
import pt.isel.pdm.chess4android.common.PuzzleOfDayApplication
import pt.isel.pdm.chess4android.common.PuzzleOfDayRepository

/**
 * Definition of the background job that fetches the daily quote and stores it in the history DB.
 */
class DownloadDailyPuzzleWorker(appContext: Context, workerParams: WorkerParameters)
    : ListenableWorker(appContext, workerParams) {

    override fun startWork(): ListenableFuture<Result> {
        val app : PuzzleOfDayApplication = applicationContext as PuzzleOfDayApplication
        val repo = PuzzleOfDayRepository(app.puzzleOfDayService, app.historyDB.getHistoryPuzzleDao())

        Log.v(APP_TAG, "Thread ${Thread.currentThread().name}: Starting DownloadDailyQuoteWorker")

        return CallbackToFutureAdapter.getFuture { completer ->
            repo.fetchPuzzleOfDay(mustSaveToDB = true) { result ->
                result
                    .onSuccess {
                        Log.v(APP_TAG, "Thread ${Thread.currentThread().name}: DownloadDailyPuzzleWorker succeeded")
                        completer.set(Result.success())
                    }
                    .onFailure {
                        Log.v(APP_TAG, "Thread ${Thread.currentThread().name}: DownloadDailyPuzzleWorker failed")
                        completer.setException(it)
                    }
            }
        }
    }
}