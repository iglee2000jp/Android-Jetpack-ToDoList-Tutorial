package com.example.jetpacktodo.source.local

import androidx.room.TypeConverter
import java.util.*

class TaskTypeConverter {

    @TypeConverter
    fun dateToLong(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun longToDate(time: Long?): Date? {
        return time?.let {
            Date(time)
        }
    }
}