package pt.isel.pdm.chess4android.history

import androidx.room.*
import pt.isel.pdm.chess4android.common.BoardState
import pt.isel.pdm.chess4android.converters.BoardStateConverter
import pt.isel.pdm.chess4android.converters.GameConverter
import pt.isel.pdm.chess4android.converters.PuzzleConverter
import pt.isel.pdm.chess4android.converters.TimeConverter
import pt.isel.pdm.chess4android.daily.Game
import pt.isel.pdm.chess4android.daily.Puzzle
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Entity(tableName = "history_puzzle")
data class PuzzleEntity(
    @PrimaryKey val id: String,

    @TypeConverters(GameConverter::class)
    var game: Game,

    @TypeConverters(PuzzleConverter::class)
    val puzzle: Puzzle,

    @ColumnInfo(name = "status")
    val status: Boolean = false,

    @TypeConverters(BoardStateConverter::class)
    val resolvedBoard: BoardState = BoardState(),

    @TypeConverters(TimeConverter::class)
    val timestamp: Date = Date.from(Instant.now().truncatedTo(ChronoUnit.DAYS)),

    ) {
    fun isTodayPuzzle() : Boolean = timestamp.toInstant().compareTo(Instant.now().truncatedTo(ChronoUnit.DAYS)) == 0
}

/**
 * The abstraction containing the supported data access operations. The actual implementation is
 * provided by the Room compiler. We can have as many DAOs has our design mandates.
 */
@Dao
interface HistoryPuzzleDao {
    @Insert
    fun insert(puzzle: PuzzleEntity)

    @Delete
    fun delete(puzzle: PuzzleEntity)

    @Update
    fun update(puzzle: PuzzleEntity)

    @Query("SELECT * FROM history_puzzle ORDER BY id DESC LIMIT 100")
    fun getAll(): List<PuzzleEntity>

    @Query("SELECT * FROM history_puzzle ORDER BY id DESC LIMIT :count")
    fun getLast(count: Int): List<PuzzleEntity>
}

/**
 * The abstraction that represents the DB itself. It is also used as a DAO factory: one factory
 * method per DAO.
 */
@Database(entities = [PuzzleEntity::class], version = 1)
@TypeConverters(GameConverter::class, PuzzleConverter::class, TimeConverter::class, BoardStateConverter::class)
abstract class HistoryDataBase : RoomDatabase() {
    abstract fun getHistoryPuzzleDao(): HistoryPuzzleDao
}