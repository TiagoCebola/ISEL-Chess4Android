package pt.isel.pdm.chess4android.common

import android.app.Application
import android.util.Log
import androidx.room.Room
import androidx.work.*
import pt.isel.pdm.chess4android.daily.DayPuzzleService
import pt.isel.pdm.chess4android.daily.DownloadDailyPuzzleWorker
import pt.isel.pdm.chess4android.history.HistoryDataBase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/*
private val gameTest: Game =  Game("nZyFAzDd", "e4 d6 f4 Nf6 Nc3 c5 Nf3 Nc6 a3 g6 Bc4 Bg7 O-O O-O d4 cxd4 Nxd4 Qb6 Ne2 Nxe4 Be3 e5 Nf5 Qxb2 Nxg7 Kxg7")
private  val puzzleTest : Puzzle = Puzzle("M3qHz", 25,
    listOf( "a1a2","e4c3","a2b2","c3d1","f1d1"))


     /*
      val historyDB : HistoryDataBase by lazy {
        Room.inMemoryDatabaseBuilder(this, HistoryDataBase::class.java).build()
    }
    override fun onCreate() {
        super.onCreate()
        callbackAfterAsync({}) {
            historyDB.getHistoryPuzzleDao().insert(PuzzleEntity(game = gameTest, puzzle = puzzleTest, id = "1"))
            historyDB.getHistoryPuzzleDao().insert(PuzzleEntity(game = gameTest, puzzle = puzzleTest, id = "2"))
            historyDB.getHistoryPuzzleDao().insert(PuzzleEntity(game = gameTest, puzzle = puzzleTest, id = "3"))
        }
    }
    */
*/

const val APP_TAG = "DailyPuzzle"

/**
 * Global class for the API service
 */
class PuzzleOfDayApplication : Application() {

    init {
        Log.v(APP_TAG, "PuzzleOfDayApplication.init")
    }

    val puzzleOfDayService : DayPuzzleService by lazy {
        Retrofit.Builder()
            .baseUrl("https://lichess.org/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DayPuzzleService::class.java)
    }

    val historyDB : HistoryDataBase by lazy {
        Room.databaseBuilder(this, HistoryDataBase::class.java, "history_db").build()
    }

    /**
     * Called each time the application process is loaded
     */
    override fun onCreate() {
        super.onCreate()
        val workRequest = PeriodicWorkRequestBuilder<DownloadDailyPuzzleWorker>(1, TimeUnit.DAYS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresBatteryNotLow(true)
                    .setRequiresStorageNotLow(true)
                    .build()
            )
            .build()

        WorkManager
            .getInstance(this)
            .enqueueUniquePeriodicWork(
                "DownloadDailyPuzzle",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
    }

}

