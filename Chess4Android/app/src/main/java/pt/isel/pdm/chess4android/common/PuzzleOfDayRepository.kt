package pt.isel.pdm.chess4android.common

import android.util.Log
import pt.isel.pdm.chess4android.daily.*
import pt.isel.pdm.chess4android.history.HistoryPuzzleDao
import pt.isel.pdm.chess4android.history.PuzzleEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

/**
 * Extension function of [PuzzleEntity] to conveniently convert it to a [DayPuzzleDTO] instance.
 */
private fun PuzzleEntity.toPuzzleOfDayDTO() = DayPuzzleDTO(
    game = Game(game.id, game.pgn),
    puzzle = Puzzle(puzzle.id, puzzle.initialPly, puzzle.solution),
    status = status,
    timestamp = timestamp,
    resolvedBoard = resolvedBoard
)

/**
 * Class repository that represents a SERVICE and a DAO
 */
class PuzzleOfDayRepository(
    private val puzzleOfDayService: DayPuzzleService,
    private val historyPuzzleDao: HistoryPuzzleDao
) {

    private val todayTimeStamp: Date = Date.from(Instant.now().truncatedTo(ChronoUnit.DAYS))
    /**
     * Asynchronously gets the daily puzzle from the local DB, if available.
     * @param callback the function to be called to signal the completion of the
     * asynchronous operation, which is called in the MAIN THREAD.
     */
    private fun asyncMaybeGetTodayPuzzleFromDB(callback: (Result<PuzzleEntity?>) -> Unit) {
        callbackAfterAsync(callback, asyncAction = {
            // GetLast is a synchronous function, runs in an alternative THREAD

            val mostRecentPuzzle = historyPuzzleDao.getLast(1).get(0)
            if(todayTimeStamp == mostRecentPuzzle.timestamp) {
                mostRecentPuzzle
            }
            else null
        })
    }

    /**
     * Asynchronously gets the daily puzzle from the remote API.
     * @param callback the function to be called to signal the completion of the
     * asynchronous operation, which is called in the MAIN THREAD.
     */
    private fun asyncGetTodayPuzzleFromAPI(callback: (Result<DayPuzzleDTO>) -> Unit) {
        puzzleOfDayService.getDayPuzzle().enqueue(
                object: Callback<DayPuzzleDTO> {
                override fun onResponse(call: Call<DayPuzzleDTO>, response: Response<DayPuzzleDTO>) {
                    Log.v(APP_TAG, "Thread ${Thread.currentThread().name}: onResponse ")
                    val dailyPuzzle : DayPuzzleDTO? = response.body()
                    val result =
                        if(response.isSuccessful && dailyPuzzle != null) {
                            Result.success(dailyPuzzle)
                        }

                        else if(response.code() == 401) {
                            Log.e( APP_TAG,"Your session has expired. Try again.")
                            Result.failure(ServiceUnavailable())
                        }

                        else {
                            Log.e( APP_TAG,"Failed to retrieve items")
                            Result.failure(ServiceUnavailable())
                        }
                    callback(result)
                }

                override fun onFailure(call: Call<DayPuzzleDTO>, error: Throwable) {
                    Log.v(APP_TAG, "Thread ${Thread.currentThread().name}: onFailure ")
                    callback(Result.failure(ServiceUnavailable(cause = error)))
                }

            })
    }

    /**
     * Asynchronously saves the daily quote to the local DB.
     * @param callback the function to be called to signal the completion of the
     * asynchronous operation, which is called in the MAIN THREAD.
     */
    private fun asyncSaveToDB(dto: DayPuzzleDTO, callback: (Result<Unit>) -> Unit = { }) {
        callbackAfterAsync(callback) {
            historyPuzzleDao.insert(
                PuzzleEntity(id = dto.puzzle.id, game = dto.game, puzzle = dto.puzzle)
            )
        }
    }

    /**
     * Asynchronously gets the quote of day, either from the local DB, if available, or from
     * the remote API.
     *
     * @param mustSaveToDB  indicates if the operation is only considered successful if all its
     * steps, including saving to the local DB, succeed. If false, the operation is considered
     * successful regardless of the success of saving the quote in the local DB (the last step).
     * @param callback the function to be called to signal the completion of the
     * asynchronous operation, which is called in the MAIN THREAD
     *
     * Using a boolean to distinguish between both options is a questionable design decision.
     */
    fun fetchPuzzleOfDay(mustSaveToDB: Boolean = false, callback: (Result<DayPuzzleDTO>) -> Unit ) {
        // 1: Check if in DB
        asyncMaybeGetTodayPuzzleFromDB { maybeEntity ->
            val maybePuzzle = maybeEntity.getOrNull()
            if(maybePuzzle != null) {
                Log.v(APP_TAG, "Thread ${Thread.currentThread().name}: Got daily puzzle from local DB")
                callback( Result.success(maybePuzzle.toPuzzleOfDayDTO()) )
            }
            else {
                // 2: Get PUZZLE from API
                asyncGetTodayPuzzleFromAPI { apiResult ->
                    apiResult.onSuccess { puzzleDTO ->
                        Log.v(APP_TAG, "Thread ${Thread.currentThread().name}: Got daily puzzle from API")
                        // Save to DB
                        asyncSaveToDB(puzzleDTO) { saveToDBResult ->
                            saveToDBResult
                                .onSuccess {
                                    Log.v(APP_TAG, "Thread ${Thread.currentThread().name}: Saved daily puzzle to local DB")
                                    callback(Result.success(puzzleDTO))
                                }
                                .onFailure {
                                    Log.e(APP_TAG, "Thread ${Thread.currentThread().name}: Failed to save daily puzzle to local DB", it)
                                    callback( if(mustSaveToDB) Result.failure(it) else Result.success(puzzleDTO) )
                                }
                        }
                    }
                    callback(apiResult)
                }
            }
        }
    }


    /**
     * Asynchronously saves the daily quote to the local DB.
     * @param callback the function to be called to signal the completion of the
     * asynchronous operation, which is called in the MAIN THREAD.
     */
    fun asyncUpdateDB(dto: DayPuzzleDTO, callback: (Result<Unit>) -> Unit = { }) {
        callbackAfterAsync(callback) {
            historyPuzzleDao.update(
                PuzzleEntity(id = dto.puzzle.id, game = dto.game, puzzle = dto.puzzle, status = dto.status, resolvedBoard = dto.resolvedBoard)
            )
        }
    }

}