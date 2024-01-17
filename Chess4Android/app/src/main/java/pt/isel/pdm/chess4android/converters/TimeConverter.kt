package pt.isel.pdm.chess4android.converters

import androidx.room.TypeConverter
import java.util.*

class TimeConverter {
   @TypeConverter
   fun fromTimeStamp(value : Long) = Date(value)

    @TypeConverter
    fun dateToTimeStamp(date: Date) = date.time
}