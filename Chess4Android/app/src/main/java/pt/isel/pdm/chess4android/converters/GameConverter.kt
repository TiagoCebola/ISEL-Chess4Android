package pt.isel.pdm.chess4android.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import pt.isel.pdm.chess4android.daily.Game

class GameConverter {
    var gson : Gson = Gson()

    @TypeConverter
    fun gameItemToString(gameItem: Game): String {
        return gson.toJson(gameItem)
    }

    @TypeConverter
    fun stringToGameItem(data: String): Game {
        return gson.fromJson(data, Game::class.java)
    }
}