package pt.isel.pdm.chess4android

import android.app.Application
import com.google.gson.Gson
import pt.isel.pdm.chess4android.challenges.ChallengesRepository
import pt.isel.pdm.chess4android.game.GamesRepository

/**
 * Tag to be used in all the application's log messages
 */
const val TAG = "Chess"

/**
 * Contains the globally accessible objects
 */
class ChessApplication : Application() {

    private val mapper: Gson by lazy { Gson() }

    /**
     * The challenges' repository
     */
    val challengesRepository: ChallengesRepository by lazy { ChallengesRepository() }

    /**
     * The chess repository
     */
    val gamesRepository: GamesRepository by lazy { GamesRepository(mapper) }
}
