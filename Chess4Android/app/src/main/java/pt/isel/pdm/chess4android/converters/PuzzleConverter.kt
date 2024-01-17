package pt.isel.pdm.chess4android.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import pt.isel.pdm.chess4android.daily.Puzzle

class PuzzleConverter {
    var gson : Gson = Gson()

    @TypeConverter
    fun puzzleItemToString(gameItem: Puzzle): String {
        return gson.toJson(gameItem)
    }

    @TypeConverter
    fun stringToPuzzleItem(data: String): Puzzle {
        return gson.fromJson(data, Puzzle::class.java)
    }
}