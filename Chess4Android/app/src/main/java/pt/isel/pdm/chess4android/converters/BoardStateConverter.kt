package pt.isel.pdm.chess4android.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import pt.isel.pdm.chess4android.common.BoardState

class BoardStateConverter {
    var gson : Gson = Gson()

    @TypeConverter
    fun boardStateItemToString(gameItem: BoardState): String {
        return gson.toJson(gameItem)
    }

    @TypeConverter
    fun stringToBoardStateItem(data: String): BoardState {
        return gson.fromJson(data, BoardState::class.java)
    }
}