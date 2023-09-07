package ua.od.acros.matusi.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ua.od.acros.matusi.data.db.dao.ParentDao
import ua.od.acros.matusi.data.model.ParentDto

@Database(entities = [ParentDto::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun parentDao(): ParentDao
}