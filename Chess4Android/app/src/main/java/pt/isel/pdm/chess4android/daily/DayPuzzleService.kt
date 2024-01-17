package pt.isel.pdm.chess4android.daily

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import pt.isel.pdm.chess4android.common.BoardState
import retrofit2.Call
import retrofit2.http.GET
import java.util.*

@Parcelize
data class Game(val id: String, val pgn: String) : Parcelable

@Parcelize
data class Puzzle(val id: String, val initialPly : Int, val solution : List<String>) : Parcelable

@Parcelize
data class DayPuzzleDTO(val game: Game, val puzzle: Puzzle, val timestamp: Date, var status: Boolean, var resolvedBoard : BoardState) : Parcelable

interface DayPuzzleService {
    @GET("puzzle/daily")
    fun getDayPuzzle() : Call<DayPuzzleDTO>
}


/**
 * Represents errors while accessing the remote API. Instead of tossing around Retrofit errors,
 * we can use this exception to wrap them up.
 */
class ServiceUnavailable(message: String = "", cause: Throwable? = null) : Exception(message, cause)